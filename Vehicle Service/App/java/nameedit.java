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

public class nameedit extends AppCompatActivity {
    FloatingActionButton back;
    ConnectionDetector ConDet;
    EditText fn,sn;
    String ffn,ssn;
    FirebaseAuth mAuth;
    Button save;
    int k,j;
    Timer timer;
    String fname,lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nameedit);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        back=(FloatingActionButton)findViewById(R.id.backbtn);
        fn=(EditText)findViewById(R.id.fn);
        sn=(EditText)findViewById(R.id.sn);
        save=(Button)findViewById(R.id.savename);
        ConDet = new ConnectionDetector(this);
        mAuth=FirebaseAuth.getInstance();
        Intent intent1 = getIntent();
        Bundle extras = intent1.getExtras();
        fname = extras.getString("fname");
        lname = extras.getString("lname");
        fn.setText(fname);
        sn.setText(lname);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(nameedit.this,editactivity.class);
                startActivity(i1);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(fn.getText().toString())||TextUtils.isEmpty(sn.getText().toString())){

                    Toast toast2=new Toast(getApplicationContext());
                    TextView tv2= new TextView(nameedit.this);
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
                    k=0;j=0;
                    String name1 = fn.getText().toString();
                    String name2 = sn.getText().toString();
                    char[] mych = new char[name1.length()];
                    for (int i = 0; i < name1.length(); i++)
                    {
                        mych[i] = name1.charAt(i);
                        if (Character.isDigit(mych[i])) {
                            k += 1;
                        }
                    }

                    char[] mych2 = new char[name2.length()];
                    for (int i = 0; i < name2.length(); i++)
                    {
                        mych2[i] = name2.charAt(i);
                        if (Character.isDigit(mych2[i])) {
                            j += 1;
                        }
                    }
                    if (k > 0)
                    {
                        fn.setError("Enter valid name");
                        fn.requestFocus();
                        return;
                    }
                    if (j > 0)
                    {
                        sn.setError("Enter valid name");
                        sn.requestFocus();
                        return;
                    }
                    if (ConDet.isConnected()) {
                        ffn=fn.getText().toString().trim();
                        ssn=sn.getText().toString().trim();
                        String Cust_Id=mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(Cust_Id);
                        current_user_db.child("First name").setValue(ffn);
                        current_user_db.child("Last name").setValue(ssn);
                        Toast toast=new Toast(getApplicationContext());
                        TextView tv= new TextView(nameedit.this);
                        tv.setBackgroundColor(Color.BLACK);
                        tv.setTextColor(Color.WHITE);
                        tv.setTextSize(16);
                        Typeface ty= Typeface.create("serif",Typeface.NORMAL);
                        tv.setTypeface(ty);
                        tv.setPadding(10,10,10,10);
                        tv.setText("Name Updated");
                        toast.setView(tv);
                        toast.show();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Intent getotp = new Intent(nameedit.this, editactivity.class);
                                startActivity(getotp);
                                finish();
                            }
                        },1200);
                    }else{
                        Toast toast=new Toast(getApplicationContext());
                        TextView tv= new TextView(nameedit.this);
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
