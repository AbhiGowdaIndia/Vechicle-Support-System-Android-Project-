package com.example.admin.vehicleservice3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class emailedit extends AppCompatActivity {

    EditText emailid;
    Button savemail;
    FloatingActionButton back;
    ConnectionDetector ConDet;
    FirebaseAuth mAuth;
    Timer timer;
    String mailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailedit);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        back=(FloatingActionButton)findViewById(R.id.backbtn);
        emailid=(EditText)findViewById(R.id.email);
        savemail=(Button)findViewById(R.id.saveemail);
        ConDet = new ConnectionDetector(this);
        mAuth=FirebaseAuth.getInstance();
        mailid=getIntent().getStringExtra("E-mail");
        emailid.setText(mailid);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(emailedit.this,editactivity.class);
                startActivity(i1);
            }
        });

        savemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(emailid.getText().toString())){
                    Toast toast2=new Toast(getApplicationContext());
                    TextView tv2= new TextView(emailedit.this);
                    tv2.setBackgroundColor(Color.BLACK);
                    tv2.setTextColor(Color.WHITE);
                    tv2.setTextSize(16);
                    Typeface ty2= Typeface.create("serif",Typeface.NORMAL);
                    tv2.setTypeface(ty2);
                    tv2.setPadding(10,10,10,10);
                    tv2.setText("Please enter the fields. ");
                    toast2.setView(tv2);
                    toast2.show();
                }else{
                    if (ConDet.isConnected()) {
                        String Mail=emailid.getText().toString().trim();
                        String Cust_Id=mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(Cust_Id);
                        current_user_db.child("E-mail").setValue(Mail);
                        Toast toast=new Toast(getApplicationContext());
                        TextView tv= new TextView(emailedit.this);
                        tv.setBackgroundColor(Color.BLACK);
                        tv.setTextColor(Color.WHITE);
                        tv.setTextSize(16);
                        Typeface ty= Typeface.create("serif",Typeface.NORMAL);
                        tv.setTypeface(ty);
                        tv.setPadding(10,10,10,10);
                        tv.setText("E-mail Updated");
                        toast.setView(tv);
                        toast.show();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Intent getotp = new Intent(emailedit.this, editactivity.class);
                                startActivity(getotp);
                                finish();
                                return;
                            }
                        },1200);
                    }else{
                        Toast toast=new Toast(getApplicationContext());
                        TextView tv= new TextView(emailedit.this);
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
            }
        });

    }
}
