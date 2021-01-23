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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class register2 extends AppCompatActivity {

    ConnectionDetector ConDet;
    DatabaseReference current_user_db;
    private FirebaseAuth mAuthe;
    MechanicDetails MechD;
    EditText Email, Pass, Cpass;
    ProgressBar pBar2;
    Button vrfy2;
    FloatingActionButton Bcktoreg1;
    String pswrd, cpswrd, eml;
    String fname,lname,phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        mAuthe=FirebaseAuth.getInstance();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent1 = getIntent();
        Bundle extras = intent1.getExtras();
        fname = extras.getString("fname");
        lname = extras.getString("lname");
        phonenumber = extras.getString("phonenumber");
        pBar2=(ProgressBar)findViewById(R.id.progressbar);
        Email = (EditText) findViewById(R.id.email);
        Pass = (EditText) findViewById(R.id.pass);
        Cpass = (EditText) findViewById(R.id.cpass);
        ConDet = new ConnectionDetector(this);
        Bcktoreg1=(FloatingActionButton)findViewById(R.id.backtreg1);
        vrfy2=(Button)findViewById(R.id.verify);

        Bcktoreg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getotp3 = new Intent(register2.this, register1.class);
                startActivity(getotp3);
            }
        });

        vrfy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(Email.getText().toString()) || TextUtils.isEmpty(Pass.getText().toString()) || TextUtils.isEmpty(Cpass.getText().toString())) {
                    Toast toast2 = new Toast(getApplicationContext());
                    TextView tv2 = new TextView(register2.this);
                    tv2.setBackgroundColor(Color.BLACK);
                    tv2.setTextColor(Color.WHITE);
                    tv2.setTextSize(16);
                    Typeface ty2 = Typeface.create("serif", Typeface.NORMAL);
                    tv2.setTypeface(ty2);
                    tv2.setPadding(10, 10, 10, 10);
                    tv2.setText("Please enter the fields.");
                    toast2.setView(tv2);
                    toast2.show();
                } else {
                    eml = Email.getText().toString().trim();
                    pswrd = Pass.getText().toString().trim();
                    cpswrd = Cpass.getText().toString().trim();
                    if (!pswrd.equals(cpswrd)) {
                        Toast to = new Toast(getApplicationContext());
                        TextView tvs = new TextView(register2.this);
                        tvs.setBackgroundColor(Color.BLACK);
                        tvs.setTextColor(Color.WHITE);
                        tvs.setTextSize(16);
                        Typeface tys = Typeface.create("serif", Typeface.NORMAL);
                        tvs.setTypeface(tys);
                        tvs.setPadding(10, 10, 10, 10);
                        tvs.setText("Password not matched.");
                        to.setView(tvs);
                        to.show();
                    } else {

                        if (ConDet.isConnected()) {
                            pBar2.setVisibility(View.VISIBLE);

                            mAuthe.createUserWithEmailAndPassword(eml,pswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){

                                        mAuthe.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){
                                                    pBar2.setVisibility(View.INVISIBLE);

                                                    Toast toast=new Toast(getApplicationContext());
                                                    TextView tv= new TextView(register2.this);
                                                    tv.setBackgroundColor(Color.BLACK);
                                                    tv.setTextColor(Color.WHITE);
                                                    tv.setTextSize(16);
                                                    Typeface ty= Typeface.create("serif",Typeface.NORMAL);
                                                    tv.setTypeface(ty);
                                                    tv.setPadding(10,10,10,10);
                                                    tv.setText("Please check your email for verification.");
                                                    toast.setView(tv);
                                                    toast.show();
                                                    String Mechanic_Id=mAuthe.getCurrentUser().getUid();
                                                    current_user_db= FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(Mechanic_Id);
                                                    current_user_db.setValue(true);
                                                    MechD = new MechanicDetails(fname,lname,phonenumber,eml,pswrd);
                                                    SaveMechanicInfo();
                                                    Intent in1=new Intent(register2.this,login.class);
                                                    //Toast.makeText(register1.this, "Register successfull", Toast.LENGTH_SHORT).show();
                                                    Email.setText("");
                                                    Pass.setText("");
                                                    Cpass.setText("");
                                                    in1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(in1);
                                                }else{
                                                    pBar2.setVisibility(View.INVISIBLE);
                                                    Toast toast1=new Toast(getApplicationContext());
                                                    TextView tv1= new TextView(register2.this);
                                                    tv1.setBackgroundColor(Color.BLACK);
                                                    tv1.setTextColor(Color.WHITE);
                                                    tv1.setTextSize(16);
                                                    Typeface ty1= Typeface.create("serif",Typeface.NORMAL);
                                                    tv1.setTypeface(ty1);
                                                    tv1.setPadding(10,10,10,10);
                                                    tv1.setText("Please try again.");
                                                    toast1.setView(tv1);
                                                    toast1.show();
                                                }
                                            }
                                        });
                                    }else{
                                        pBar2.setVisibility(View.INVISIBLE);
                                        Toast toast=new Toast(getApplicationContext());
                                        TextView tv= new TextView(register2.this);
                                        tv.setBackgroundColor(Color.BLACK);
                                        tv.setTextColor(Color.WHITE);
                                        tv.setTextSize(16);
                                        Typeface ty= Typeface.create("serif",Typeface.NORMAL);
                                        tv.setTypeface(ty);
                                        tv.setPadding(10,10,10,10);
                                        tv.setText(task.getException().getMessage());
                                        toast.setView(tv);
                                        toast.show();
                                        //Toast.makeText(register1.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }

                                }
                            });


                        } else {
                            pBar2.setVisibility(View.INVISIBLE);
                            Toast toast=new Toast(getApplicationContext());
                            TextView tv= new TextView(register2.this);
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
            }
        });
    }

    private void SaveMechanicInfo() {
        Map MechInfo = new HashMap();
        MechInfo.put("First name",MechD.getFname());
        MechInfo.put("Last name",MechD.getLname());
        MechInfo.put("Mobile number",MechD.getMnum());
        MechInfo.put("E-mail",MechD.getEmail());
        MechInfo.put("Password",MechD.getPassword());
        current_user_db.updateChildren(MechInfo);

    }
}


