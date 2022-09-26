package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportActivity extends AppCompatActivity {
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "SD is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // creating folder if it does not exist
        checkDatabaseFolder();

        key = MainActivity.getPin();

        Button exportButton = findViewById(R.id.btnFileNameEx);
        exportButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String filename = ((EditText)findViewById(R.id.edFileNameEx)).getText().toString();
                exportDatabaseToFile(filename);


                Intent intent = new Intent(ExportActivity.this, MenuActivity.class);
                finish();

                startActivity(intent);
            }
        });

    }

    void exportDatabaseToFile(String filename) {

        String str = Environment.getExternalStorageDirectory().toString();

        File databaseFile = new File(Environment.getExternalStorageDirectory() + "/AppDatabases/", filename);
        BufferedWriter bw;

        try {
            bw = new BufferedWriter(new FileWriter(databaseFile));
        }
        catch(IOException e) {
            Toast.makeText(this, "FileWriter() failed", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            return;
        }

        DataBase database = new DataBase(this);
        database.setKey(key);

        String[] resources = database.getAllResources();

        int rofl = database.getItemCount();

        for(int i = 0; i < database.getItemCount(); i++) {
            writeEntryToFile(resources[i], bw, database);
        }

        database.close();

        try {
            bw.close();
        }
        catch(IOException e) {
            Toast.makeText(this, "bw.close() failed", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    void writeEntryToFile(String resource, BufferedWriter bw, DataBase database) {

        String login = database.getItemInfoByResourceName(resource, DBRequest.KEY_LOGIN);
        String password = database.getItemInfoByResourceName(resource, DBRequest.KEY_PASSWORD);
        String note = database.getItemInfoByResourceName(resource, DBRequest.KEY_NOTE);

        try {
            bw.write(encrypt(resource) + " " +
                    encrypt(login) + " " +
                    encrypt(password) + " " +
                    encrypt(note) + '\n');
        }
        catch(IOException e) {
            Toast.makeText(this, "bw.write() failed", Toast.LENGTH_SHORT).show();
            return;
        }

        return;
    }
    void checkDatabaseFolder() {
        File databaseFolder = new File(Environment.getExternalStorageDirectory() + "/AppDatabases/");
        if(!databaseFolder.exists())
            databaseFolder.mkdirs();
    }

    String encrypt(String openedText) {

        CryptoProvide cp = new CryptoProvide();
        String cipherText = "";

        try {
            cipherText = cp.encryptMessage(openedText, key);
        }
        catch (Exception e) {
            Toast.makeText(this, "encryptMessage failed", Toast.LENGTH_SHORT).show();
        }

        if(cipherText.length() == 0)
            return "";

        return cipherText.substring(0, cipherText.length() - 1);
    }
}