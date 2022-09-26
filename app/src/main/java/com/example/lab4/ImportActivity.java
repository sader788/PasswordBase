package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ImportActivity extends AppCompatActivity {

    EditText edFileName;

    Button btnFileName;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        edFileName = findViewById(R.id.edFileNameIm);
        btnFileName = findViewById(R.id.btnFileNameIm);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "SD is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // creating folder if it does not exist
        checkDatabaseFolder();

        key = MainActivity.getPin();


        btnFileName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                importDatabaseFromFile(edFileName.getText().toString());

                Intent intent = new Intent(ImportActivity.this, MenuActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }


    void importDatabaseFromFile(String filename) {
        File databaseFile = new File(Environment.getExternalStorageDirectory() + "/AppDatabases/", filename);
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(databaseFile));
        }
        catch (IOException e) {
            Toast.makeText(this, "FileReader() failed", Toast.LENGTH_SHORT).show();
            return;
        }

        DataBase database = new DataBase(this);
        database.setKey(key);

        String[] resources = database.getAllResources();

        // deletion of all items from database
        for(int i = 0; i < database.getItemCount(); i++) {
            database.deleteItem(resources[i]);
        }

        String line;

        try {
            while((line = br.readLine()) != null) {

                String[] parameters = line.split(" ");
                if(parameters.length < 3) {
                    continue;
                }

                String resource = decrypt(parameters[0]);
                String login = decrypt(parameters[1]);
                String password = decrypt(parameters[2]);
                String note = "";
                if(parameters.length == 4)
                    note = decrypt(parameters[3]);

                database.createItem(resource, login, password, note);
            }
        }
        catch(IOException e) {
            Toast.makeText(this, "readLine() failed", Toast.LENGTH_SHORT).show();
            return;
        }

        database.close();

        try {
            br.close();
        }
        catch(IOException e) {
            Toast.makeText(this, "br.close() failed", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    String[] getFileListFromFolder() {
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File yourDir = new File(sdCardRoot, "AppDatabases");
        File[] filesArray = yourDir.listFiles();
        String[] list = new String[filesArray.length];

        for (int i = 0; i < filesArray.length; i++) {
            list[i] = filesArray[i].getName();
        }

        return list;
    }

    void checkDatabaseFolder() {
        File databaseFolder = new File(Environment.getExternalStorageDirectory() + "/AppDatabases/");
        if(!databaseFolder.exists())
            databaseFolder.mkdirs();
    }

    String decrypt(String cipherText) {

        CryptoProvide cp = new CryptoProvide();
        String openedText = "";

        try {
            openedText = cp.decryptMessage(cipherText, key);
        }
        catch (Exception e) {
            Toast.makeText(this, "decryptMessage failed", Toast.LENGTH_SHORT).show();
        }

        return openedText;
    }
}