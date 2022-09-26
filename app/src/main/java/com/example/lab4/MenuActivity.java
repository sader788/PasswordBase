package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.sax.Element;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    Button btnNew;
    Button btnChangePIN;
    Button btnExport;
    Button btnImport;

    String resources[];
    String checkedResource;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        listView = findViewById(R.id.resourceList);

        btnNew =  findViewById(R.id.btnNEW);
        btnChangePIN = findViewById(R.id.btnChangePIN);
        btnExport = findViewById(R.id.btnEXPORT);
        btnImport = findViewById(R.id.btnIMPORT);

        btnChangePIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(MenuActivity.this,NewPasswordActivity.class);
                startActivity(myintent);
            }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(MenuActivity.this,NewEntryActivity.class);
                startActivity(myintent);
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(MenuActivity.this,ExportActivity.class);
                startActivity(myintent);
            }
        });

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(MenuActivity.this,ImportActivity.class);
                startActivity(myintent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MenuActivity.this, EditEntryActivity.class);
                intent.putExtra("login_id", resources[position]);
                startActivity(intent);
            }
        });

        refreshResourcesList();
    }

    void refreshResourcesList() {
        DataBase database = new DataBase(this);
        database.setKey(MainActivity.getPin());
        resources = database.getAllResources();
        database.close();
        if(resources.length == 0) {
            Toast.makeText(MenuActivity.this, "Resource list is empty", Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, resources);
        listView.setAdapter(adapter);
    }
}