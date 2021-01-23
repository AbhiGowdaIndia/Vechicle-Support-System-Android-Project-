package com.example.admin.vehicleservice3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class custregister2 extends AppCompatActivity {
    ConnectionDetector ConDet;
    FloatingActionButton fabBack,fabNext;
    EditText editTextPhoneNum;
    CountryCodePicker ccp;
    String number,phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custregister2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fabBack=(FloatingActionButton)findViewById(R.id.floatbackbtn);
        fabNext=(FloatingActionButton)findViewById(R.id.floatfrntbtn);
        editTextPhoneNum=(EditText)findViewById(R.id.regPhoneNum) ;
        ConDet = new ConnectionDetector(this);
        ccp=(CountryCodePicker)findViewById(R.id.cpp);
        ccp.registerCarrierNumberEditText(editTextPhoneNum);
                fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backtocust=new Intent(custregister2.this,custregister.class);
                startActivity(backtocust);
            }
        });

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number=editTextPhoneNum.getText().toString();
                if(number.isEmpty()||number.length()<10||number.length()>10){
                    editTextPhoneNum.setError("Enter valid phone number");
                    editTextPhoneNum.requestFocus();
                    return;
                }
                if (ConDet.isConnected()) {
                    phonenumber = ccp.getFullNumberWithPlus();
                    Intent getotp = new Intent(custregister2.this, phoneverify.class);
                    getotp.putExtra("phonenumber", phonenumber);
                    startActivity(getotp);
                }
                else{
                    Toast toast=new Toast(getApplicationContext());
                    TextView tv= new TextView(custregister2.this);
                    tv.setBackgroundColor(Color.BLACK);
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(16);
                    Typeface ty= Typeface.create("serif",Typeface.NORMAL);
                    tv.setTypeface(ty);
                    tv.setPadding(10,10,10,10);
                    tv.setText("Something went wrong, please try again.");
                    toast.setView(tv);
                    toast.show();
                }
            }
        });

    }


}
