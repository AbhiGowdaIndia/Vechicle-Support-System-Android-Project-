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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CustomerName extends AppCompatActivity {
    String fn,ln;
    ConnectionDetector ConDet;
    private FirebaseAuth mAuthe;
    EditText fname,lname;
    FloatingActionButton add;
    int k,j;
    DatabaseReference current_user_db;
    String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_name);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuthe=FirebaseAuth.getInstance();
        fname=(EditText)findViewById(R.id.fname);
        lname=(EditText)findViewById(R.id.lname);
        phonenumber=getIntent().getStringExtra("phonenumber");
        add=(FloatingActionButton)findViewById(R.id.add);
        ConDet = new ConnectionDetector(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(fname.getText().toString())||TextUtils.isEmpty(lname.getText().toString())){

                    Toast toast2=new Toast(getApplicationContext());
                    TextView tv2= new TextView(CustomerName.this);
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
                        fname.setError("Enter valid name");
                        fname.requestFocus();
                        return;
                    }
                    if (j > 0)
                    {
                        lname.setError("Enter valid name");
                        lname.requestFocus();
                        return;
                    }
                    if (ConDet.isConnected()) {
                        fn=fname.getText().toString().trim();
                        ln=lname.getText().toString().trim();
                        String Cust_Id=mAuthe.getCurrentUser().getUid();
                        current_user_db= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(Cust_Id);
                        current_user_db.setValue(true);
                        SaveMechanicInfo(fn,ln,phonenumber);
                        Intent getotp = new Intent(CustomerName.this,selectvehicletype.class);
                        startActivity(getotp);
                    }else{
                        Toast toast=new Toast(getApplicationContext());
                        TextView tv= new TextView(CustomerName.this);
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

    private void SaveMechanicInfo(String fn,String ln,String phonenumber) {
        Map MechInfo = new HashMap();
        MechInfo.put("First name",fn);
        MechInfo.put("Last name",ln);
        MechInfo.put("Mobile number",phonenumber);
        current_user_db.updateChildren(MechInfo);

    }
}
