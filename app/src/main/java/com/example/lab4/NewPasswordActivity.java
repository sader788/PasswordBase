package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Base64;

public class NewPasswordActivity extends AppCompatActivity {

    Button btnNewPIN;

    EditText edNewPIN;

    DataBase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        btnNewPIN = findViewById(R.id.btnNewPIN);
        edNewPIN = findViewById(R.id.edNewPIN);

        database = new DataBase(this);
        database.setKey(MainActivity.getPin());

        btnNewPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sPref = getSharedPreferences("PIN_CODE", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("pin_code",encode(edNewPIN.getText().toString(),"test") );
                ed.commit();


                String previousCode = MainActivity.getPin();
                MainActivity.setPin(edNewPIN.getText().toString());
                String currentCode = MainActivity.getPin();

                int nResources = database.getItemCount();
                String[] resources = database.getAllResources();

                String[] logins = new String[nResources];
                String[] passwords = new String[nResources] ;
                String[] notes = new String[nResources];

                for (int i = 0; i < nResources; i++) {
                    // getting info about the current resource
                    database.setKey(previousCode);
                    logins[i] = database.getItemInfoByResourceName(resources[i], DBRequest.KEY_LOGIN);
                    passwords[i] = database.getItemInfoByResourceName(resources[i], DBRequest.KEY_PASSWORD);
                    notes[i] = database.getItemInfoByResourceName(resources[i], DBRequest.KEY_NOTE);

                    database.deleteItem(resources[i]);

                    database.setKey(currentCode);
                    database.createItem(resources[i], logins[i], passwords[i], notes[i]);
                }

                database.close();

                Intent intent = new Intent(NewPasswordActivity.this, MenuActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    public static String encode(String pText, String pKey) {
        byte[] txt = pText.getBytes();
        byte[] key = pKey.getBytes();
        byte[] res = new byte[pText.length()];

        for (int i = 0; i < txt.length; i++) {
            res[i] = (byte) (txt[i] ^ key[i % key.length]);
        }

        return Base64.getEncoder().encodeToString(res);
    }

}