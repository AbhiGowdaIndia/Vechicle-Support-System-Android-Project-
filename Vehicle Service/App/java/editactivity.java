package com.example.admin.vehicleservice3;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;
import java.util.Map;

public class editactivity extends AppCompatActivity {
    TextView fname,sname,pnum,email,t1,t2,t3,t4;
    CircularImageView prfimg;
    ConnectionDetector ConDet;
   // private FirebaseAuth mAuthe;
    FloatingActionButton editimg;

    String name1,name2,phnum,profilUrl;
    String mail="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editactivity);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        t1=(TextView)findViewById(R.id.t1);
        t2=(TextView)findViewById(R.id.t2);
        t3=(TextView)findViewById(R.id.t3);
        t4=(TextView)findViewById(R.id.t4);
        fname=(TextView)findViewById(R.id.fname);
        sname=(TextView)findViewById(R.id.sname);
        pnum=(TextView)findViewById(R.id.pnum);
        email=(TextView)findViewById(R.id.email);
        prfimg=(CircularImageView)findViewById(R.id.profileimg);
        editimg=(FloatingActionButton)findViewById(R.id.editimg);
        ConDet = new ConnectionDetector(this);

        loaddetails();


        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte=new Intent(editactivity.this,nameedit.class);
                Bundle extras = new Bundle();
                extras.putString("fname",name1);
                extras.putString("lname",name2);
                inte.putExtras(extras);
                startActivity(inte);
            }
        });
        fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte=new Intent(editactivity.this,nameedit.class);
                Bundle extras = new Bundle();
                extras.putString("fname",name1);
                extras.putString("lname",name2);
                inte.putExtras(extras);
                startActivity(inte);
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte=new Intent(editactivity.this,nameedit.class);
                startActivity(inte);
            }
        });
        sname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte1=new Intent(editactivity.this,nameedit.class);
                startActivity(inte1);
            }
        });

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte=new Intent(editactivity.this,phonenumedit.class);
                inte.putExtra("phnumber",phnum);
                startActivity(inte);
            }
        });
        pnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte2=new Intent(editactivity.this,phonenumedit.class);
                inte2.putExtra("phnumber",phnum);
                startActivity(inte2);
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte=new Intent(editactivity.this,emailedit.class);
                inte.putExtra("E-mail",mail);
                startActivity(inte);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte3=new Intent(editactivity.this,emailedit.class);
                inte3.putExtra("E-mail",mail);
                startActivity(inte3);
            }
        });
        prfimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent inte4=new Intent(editactivity.this,editimage.class);
                //startActivity(inte4);
            }
        });
        editimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte5=new Intent(editactivity.this,editimage.class);
                startActivity(inte5);
            }
        });
    }

    private void loaddetails() {
        if (ConDet.isConnected()) {
            String userId="";
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference CustDetailsref = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userId);
            CustDetailsref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {

                        Map<String,Object> map=(Map<String,Object>) dataSnapshot.getValue();

                        if(map.get("First name")!=null){
                            name1=map.get("First name").toString();
                            fname.setText(name1);
                        }
                        if(map.get("Last name")!=null){
                            name2=map.get("Last name").toString();
                            sname.setText(name2);
                        }
                        if(map.get("Mobile number")!=null){
                            phnum=map.get("Mobile number").toString();
                            pnum.setText(phnum);
                        }
                        if (dataSnapshot.child("E-mail").exists()) {
                            if (map.get("E-mail") != null) {
                                mail = map.get("E-mail").toString();
                                email.setText(mail);
                            }
                        }

                        if(map.get("ProfileImageurl")!=null){
                            profilUrl=map.get("ProfileImageurl").toString();
                           Glide.with(getApplication()).load(profilUrl).into(prfimg);
                        }
                      }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            Toast toast = new Toast(getApplicationContext());
            TextView tv = new TextView(editactivity.this);
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
