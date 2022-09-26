package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class NewEntryActivity extends AppCompatActivity {

    Switch switchShowPass;

    EditText edLoginID;
    EditText edLogin;
    EditText edPassword;
    EditText edNote;

    Button btnCreate;
    Button btnCancel;

    DataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        //init global var

        database = new DataBase(this);
        database.setKey(MainActivity.getPin());

        switchShowPass = findViewById(R.id.switchShowPassCr);

        edLoginID = findViewById(R.id.edLoginIDCr);
        edLogin = findViewById(R.id.edLoginCr);
        edPassword = findViewById(R.id.edPasswordCr);
        edNote = findViewById(R.id.edNoteCr);

        btnCreate = findViewById(R.id.btnCreateCr);
        btnCancel = findViewById(R.id.btnCancelCr);

        //init listeners
        switchShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else{

                    edPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long result = database.createItem(
                        edLoginID.getText().toString(),
                        edLogin.getText().toString(),
                        edPassword.getText().toString(),
                        edNote.getText().toString()
                );
                //String temp=passwordText2.getText().toString();
                if(result < 0) {
                    Toast.makeText(NewEntryActivity.this, "[-] createItem returned -1", Toast.LENGTH_SHORT).show();
                }

                database.close();

                Intent intent = new Intent(NewEntryActivity.this, MenuActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewEntryActivity.this, MenuActivity.class);
                finish();

                startActivity(intent);
            }
        });
    }




}