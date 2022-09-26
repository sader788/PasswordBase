package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Base64;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText psd;
    public static String originalPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);


        SharedPreferences pref = getSharedPreferences("PIN_CODE", MODE_PRIVATE);
        String pin = pref.getString("pin_code", "N/A");

        if(pin.equals("N/A"))
        {
            originalPin="123";
        }
        else
        {
            originalPin=decode(Base64.getDecoder().decode(pin),"test");
        }

        psd=(EditText) findViewById(R.id.edPIN);
        btn=(Button) findViewById(R.id.btnPIN);



        btn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(psd.getText().toString().equals(originalPin))
                {
                    Intent myintent = new Intent(MainActivity.this,MenuActivity.class);
                    startActivity(myintent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Incorrect password!",Toast.LENGTH_LONG).show();
                }
            }
        }));
    }
    public static String getPin()
    {
        int length =32;
        String letter = "A";
        String adding="";
        if(originalPin.length()<=length)
        {
            int how_much=length-originalPin.length();
            for(int i=0;i<how_much;i++)
            {
                adding=adding.concat(letter);
            }
            return originalPin+adding;
        }
        return "";
    }

    public static void setPin(String new_code)
    {
        originalPin=new_code;
    }

    public static String decode(byte[] pText, String pKey) {
        byte[] res = new byte[pText.length];
        byte[] key = pKey.getBytes();

        for (int i = 0; i < pText.length; i++) {
            res[i] = (byte) (pText[i] ^ key[i % key.length]);
        }

        return new String(res);
    }
}