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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    ConnectionDetector ConDet;
  EditText email,pword;
  FloatingActionButton bcktomain,signin;
  FirebaseAuth mAuth;
  ProgressBar pb;
  TextView frgtpasswrd;
  String Email,Pword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ConDet = new ConnectionDetector(this);
        email=(EditText)findViewById(R.id.lemail);
        pword=(EditText)findViewById(R.id.lpword);
        bcktomain=(FloatingActionButton)findViewById(R.id.backtmain);
        signin=(FloatingActionButton)findViewById(R.id.signin);
        frgtpasswrd=(TextView)findViewById(R.id.frgtpswrd);
        pb=(ProgressBar)findViewById(R.id.progress);

        mAuth=FirebaseAuth.getInstance();

        bcktomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2=new Intent(login.this,MainActivity.class);
                startActivity(i2);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(pword.getText().toString())){

                    Toast toast2=new Toast(getApplicationContext());
                    TextView tv2= new TextView(login.this);
                    tv2.setBackgroundColor(Color.BLACK);
                    tv2.setTextColor(Color.WHITE);
                    tv2.setTextSize(16);
                    Typeface ty2= Typeface.create("serif",Typeface.NORMAL);
                    tv2.setTypeface(ty2);
                    tv2.setPadding(10,10,10,10);
                    tv2.setText("Please enter the fields. ");
                    toast2.setView(tv2);
                    toast2.show();
                }
                else{

                    Email=email.getText().toString().trim();
                    Pword=pword.getText().toString().trim();
                    pb.setVisibility(View.VISIBLE);
                    if (ConDet.isConnected()) {
                        mAuth.signInWithEmailAndPassword(Email, Pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                pb.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {

                                    if (mAuth.getCurrentUser().isEmailVerified()) {
                                        Intent i = new Intent(login.this, selectvehicletype.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    } else {
                                        pb.setVisibility(View.INVISIBLE);
                                        Toast toast = new Toast(getApplicationContext());
                                        TextView tv = new TextView(login.this);
                                        tv.setBackgroundColor(Color.BLACK);
                                        tv.setTextColor(Color.WHITE);
                                        tv.setTextSize(16);
                                        Typeface ty = Typeface.create("serif", Typeface.NORMAL);
                                        tv.setTypeface(ty);
                                        tv.setPadding(10, 10, 10, 10);
                                        tv.setText("Please check your email for verification.");
                                        toast.setView(tv);
                                        toast.show();
                                    }

                                } else {
                                    pb.setVisibility(View.INVISIBLE);

                                    Toast toast1 = new Toast(getApplicationContext());
                                    TextView tv1 = new TextView(login.this);
                                    tv1.setBackgroundColor(Color.BLACK);
                                    tv1.setTextColor(Color.WHITE);
                                    tv1.setTextSize(16);
                                    Typeface ty1 = Typeface.create("serif", Typeface.NORMAL);
                                    tv1.setTypeface(ty1);
                                    tv1.setPadding(10, 10, 10, 10);
                                    tv1.setText("Please register if your not yet registered.");
                                    toast1.setView(tv1);
                                    toast1.show();
                                }

                            }
                        });

                    }else{
                        pb.setVisibility(View.INVISIBLE);
                        Toast toast=new Toast(getApplicationContext());
                        TextView tv= new TextView(login.this);
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
        frgtpasswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    Toast toast3=new Toast(getApplicationContext());
                    TextView tv3= new TextView(login.this);
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
                    if (ConDet.isConnected()) {
                        Email = email.getText().toString().trim();
                        Intent i2 = new Intent(login.this, forgotpassword.class);
                        i2.putExtra("emailaddress", Email);
                        startActivity(i2);
                    }else{
                        pb.setVisibility(View.INVISIBLE);
                        Toast toast=new Toast(getApplicationContext());
                        TextView tv= new TextView(login.this);
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
