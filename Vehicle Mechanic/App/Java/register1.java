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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register1 extends AppCompatActivity {
    ConnectionDetector ConDet;
    //EditText Mid;
    EditText fname,lname,Pnum;
    FloatingActionButton bcktmain;
    Button vrfy;
    CheckBox chkbtn;
    private FirebaseAuth mAuth;
    String fn,ln,name,number,phonenumber;
    int k,j;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuth = FirebaseAuth.getInstance();
        bcktmain=(FloatingActionButton)findViewById(R.id.backtmain);
        //Mid=(EditText)findViewById(R.id.mid);
        fname=(EditText)findViewById(R.id.fname);
        lname=(EditText)findViewById(R.id.lname);
        Pnum=(EditText)findViewById(R.id.pnum);
        chkbtn=(CheckBox)findViewById(R.id.checkbtn);
        ConDet = new ConnectionDetector(this);
        vrfy=(Button)findViewById(R.id.verify);

        bcktmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(register1.this, MainActivity.class);
                startActivity(back);
            }
        });
        vrfy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(fname.getText().toString())||TextUtils.isEmpty(lname.getText().toString())||TextUtils.isEmpty(Pnum.getText().toString())){

                    Toast toast2=new Toast(getApplicationContext());
                    TextView tv2= new TextView(register1.this);
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
                    String name1 = fname.getText().toString();
                    String name2 = lname.getText().toString();
                    char[] mych = new char[name1.length()];
                    for (int i = 0; i < name1.length(); i++)
                    {
                        mych[i] = name1.charAt(i);
                        if (Character.isDigit(mych[i])) {
                            k += 1;
                        }
                    }
                    if (k > 0)
                    {
                        fname.setError("Enter valid name");
                        fname.requestFocus();
                        return;
                    }

                    char[] mych2 = new char[name2.length()];
                    for (int i = 0; i < name2.length(); i++)
                    {
                        mych2[i] = name2.charAt(i);
                        if (Character.isDigit(mych2[i])) {
                            j += 1;
                        }
                    }

                    if (j > 0)
                    {
                        lname.setError("Enter valid name");
                        lname.requestFocus();
                        return;
                    }
                    if(chkbtn.isChecked()) {
                        number=Pnum.getText().toString();
                        if(number.length()<10||number.length()>10){
                            Pnum.setError("Enter valid phone number");
                            Pnum.requestFocus();
                            return;
                        }

                        if (ConDet.isConnected()) {

                            phonenumber = "+91"+number;
                            fn=fname.getText().toString().trim();
                            ln=lname.getText().toString().trim();
                            Intent getotp = new Intent(register1.this, phoneverify.class);
                            Bundle extras = new Bundle();
                            extras.putString("fname", fn);
                            extras.putString("lname", ln);
                            extras.putString("phonenumber", phonenumber);
                            getotp.putExtras(extras);
                            startActivity(getotp);
                        }else{
                            Toast toast=new Toast(getApplicationContext());
                            TextView tv= new TextView(register1.this);
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

                    }else
                    {
                        Toast toast=new Toast(getApplicationContext());
                        TextView tv= new TextView(register1.this);
                        tv.setBackgroundColor(Color.BLACK);
                        tv.setTextColor(Color.WHITE);
                        tv.setTextSize(16);
                        Typeface ty= Typeface.create("serif",Typeface.NORMAL);
                        tv.setTypeface(ty);
                        tv.setPadding(10,10,10,10);
                        tv.setText("Please confirm by selecting the checkbox and then Continue.");
                        toast.setView(tv);
                        toast.show();

                    }
                }

            }
        });
    }
}
