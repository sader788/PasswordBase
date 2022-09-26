package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class EditEntryActivity extends AppCompatActivity {

    Switch switchShowPass;

    EditText edLoginID;
    EditText edLogin;
    EditText edPassword;
    EditText edNote;

    Button btnEdit;
    Button btnDelete;
    Button btnCancel;

    DataBase database;

    String strLoginID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        Bundle arguments = getIntent().getExtras();
        strLoginID = arguments.get("login_id").toString();

        database = new DataBase(this);
        database.setKey(MainActivity.getPin());

        switchShowPass = findViewById(R.id.switchShowPassEd);

        edLoginID = findViewById(R.id.edLoginIDEd);
        edLogin = findViewById(R.id.edLoginEd);
        edPassword = findViewById(R.id.edPasswordEd);
        edNote = findViewById(R.id.edNoteEd);

        btnEdit = findViewById(R.id.btnEditEd);
        btnDelete = findViewById(R.id.btnDeleteEd);
        btnCancel = findViewById(R.id.btnCancelEd);

        // set args
        edLoginID.setText(strLoginID);
        edLogin.setText(database.getItemInfoByResourceName(strLoginID, DBRequest.KEY_LOGIN));
        edPassword.setText(database.getItemInfoByResourceName(strLoginID, DBRequest.KEY_PASSWORD));
        edNote.setText(database.getItemInfoByResourceName(strLoginID, DBRequest.KEY_NOTE));

        // set listeners

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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.updateLogin(strLoginID, edLogin.getText().toString());
                database.updatePassword(strLoginID, edPassword.getText().toString());
                database.updateNote(strLoginID, edNote.getText().toString());
                database.updateResource(strLoginID, edLoginID.getText().toString());

                database.close();

                Intent intent = new Intent(EditEntryActivity.this, MenuActivity.class);
                finish();
                //intent.putExtra("password", "not_changed");
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.deleteItem(strLoginID);
                database.close();

                Intent intent = new Intent(EditEntryActivity.this, MenuActivity.class);
                finish();
                //intent.putExtra("password", "not_changed");
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEntryActivity.this, MenuActivity.class);
                finish();
                //intent.putExtra("password", "not_changed");
                startActivity(intent);
            }
        });


    }


}