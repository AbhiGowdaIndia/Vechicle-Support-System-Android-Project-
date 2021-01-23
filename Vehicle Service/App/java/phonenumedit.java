package com.example.admin.vehicleservice3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.util.Timer;
import java.util.TimerTask;

public class phonenumedit extends AppCompatActivity {
    ConnectionDetector ConDet;
    FloatingActionButton back;
    EditText editTextPhoneNum;
    CountryCodePicker ccp;
    Button save;
    Timer timer;
    String number,phonenumber, ppnum,nmber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumedit);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ppnum=getIntent().getStringExtra("phnumber");
        back=(FloatingActionButton)findViewById(R.id.backbtn);
        editTextPhoneNum=(EditText)findViewById(R.id.PhoneNum) ;
        ConDet = new ConnectionDetector(this);
        ccp=(CountryCodePicker)findViewById(R.id.cpp);
        save=(Button)findViewById(R.id.savephone);
        ccp.registerCarrierNumberEditText(editTextPhoneNum);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(phonenumedit.this,editactivity.class);
                startActivity(i1);
            }
        });

       save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number=editTextPhoneNum.getText().toString();
                nmber="+91"+number;
                if(ppnum.equals(nmber)){
                    Toast toast=new Toast(getApplicationContext());
                    TextView tv= new TextView(phonenumedit.this);
                    tv.setBackgroundColor(Color.BLACK);
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(16);
                    Typeface ty= Typeface.create("serif",Typeface.NORMAL);
                    tv.setTypeface(ty);
                    tv.setPadding(10,10,10,10);
                    tv.setText("Number Updated");
                    toast.setView(tv);
                    toast.show();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent getotp = new Intent(phonenumedit.this, editactivity.class);
                            startActivity(getotp);
                            finish();
                        }
                    },1200);


                }
                else{

                if(number.isEmpty()||number.length()<10||number.length()>10){
                    editTextPhoneNum.setError("Enter valid phone number");
                    editTextPhoneNum.requestFocus();
                    return;
                }
                if (ConDet.isConnected()) {
                    phonenumber = ccp.getFullNumberWithPlus();
                    Intent getotp = new Intent(phonenumedit.this, editphoneverify.class);
                    getotp.putExtra("phonenumber", phonenumber);
                    startActivity(getotp);
                }
                else {
                    Toast toast = new Toast(getApplicationContext());
                    TextView tv = new TextView(phonenumedit.this);
                    tv.setBackgroundColor(Color.BLACK);
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(16);
                    Typeface ty = Typeface.create("serif", Typeface.NORMAL);
                    tv.setTypeface(ty);
                    tv.setPadding(10, 10, 10, 10);
                    tv.setText("Something went wrong, please try again.");
                    toast.setView(tv);
                    toast.show();
                }
                }
            }
        });
    }
}
