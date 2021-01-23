package com.example.admin.vehiclemechanic;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class forgotpassword extends AppCompatActivity {
    Timer timer;
    ConnectionDetector ConDet;
    FirebaseAuth mAuth;
    FloatingActionButton bcktologin;
    EditText forgemail;
    TextView backtoregister;
    ProgressBar pb;
    Button getpasswrd;
    String Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ConDet = new ConnectionDetector(this);
        bcktologin=(FloatingActionButton)findViewById(R.id.backtlogin);
        forgemail=(EditText)findViewById(R.id.femail);
        pb=(ProgressBar)findViewById(R.id.progress);
        backtoregister=(TextView)findViewById(R.id.backtoreg);
        getpasswrd=(Button)findViewById(R.id.getpswrd);
        String forgEmail=getIntent().getStringExtra("emailaddress");
        forgemail.setText(forgEmail);
        mAuth=FirebaseAuth.getInstance();

        bcktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2=new Intent(forgotpassword.this,login.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i2);
            }
        });

        backtoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3=new Intent(forgotpassword.this,registerimage.class);
                i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i3);
            }
        });

        getpasswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(forgemail.getText().toString())){
                    Toast toast3=new Toast(getApplicationContext());
                    TextView tv3= new TextView(forgotpassword.this);
                    tv3.setBackgroundColor(Color.BLACK);
                    tv3.setTextColor(Color.WHITE);
                    tv3.setTextSize(16);
                    Typeface ty3= Typeface.create("serif",Typeface.NORMAL);
                    tv3.setTypeface(ty3);
                    tv3.setPadding(10,10,10,10);
                    tv3.setText("Please enter the email address. ");
                    toast3.setView(tv3);
                    toast3.show();

                }else{
                    pb.setVisibility(View.VISIBLE);
                    if (ConDet.isConnected()) {
                        Email = forgemail.getText().toString().trim();
                        mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pb.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()){
                                    Toast toast=new Toast(getApplicationContext());
                                    TextView tv= new TextView(forgotpassword.this);
                                    tv.setBackgroundColor(Color.BLACK);
                                    tv.setTextColor(Color.WHITE);
                                    tv.setTextSize(16);
                                    Typeface ty= Typeface.create("serif",Typeface.NORMAL);
                                    tv.setTypeface(ty);
                                    tv.setPadding(10,10,10,10);
                                    tv.setText("Password sent to your email.");
                                    toast.setView(tv);
                                    toast.show();
                                    timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(forgotpassword.this,login.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    },1000);

                                }
                                else{
                                    Toast toast=new Toast(getApplicationContext());
                                    TextView tv= new TextView(forgotpassword.this);
                                    tv.setBackgroundColor(Color.BLACK);
                                    tv.setTextColor(Color.WHITE);
                                    tv.setTextSize(16);
                                    Typeface ty= Typeface.create("serif",Typeface.NORMAL);
                                    tv.setTypeface(ty);
                                    tv.setPadding(10,10,10,10);
                                    tv.setText(task.getException().getMessage());
                                    toast.setView(tv);
                                    toast.show();
                                }
                            }
                        });

                    }else{
                        pb.setVisibility(View.INVISIBLE);
                        Toast toast=new Toast(getApplicationContext());
                        TextView tv= new TextView(forgotpassword.this);
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
