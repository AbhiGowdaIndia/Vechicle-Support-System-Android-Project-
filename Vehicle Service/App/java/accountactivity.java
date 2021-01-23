package com.example.admin.vehicleservice3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
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

import java.util.Map;

public class accountactivity extends AppCompatActivity {
    TextView editinfo,signouttext,name;
    CircularImageView prfimg;
    ConnectionDetector ConDet;
    String name1,imgurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountactivity);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ConDet = new ConnectionDetector(this);
        editinfo=(TextView)findViewById(R.id.editinfo);
        name=(TextView)findViewById(R.id.name);
        signouttext=(TextView)findViewById(R.id.signout);
        prfimg=(CircularImageView) findViewById(R.id.profileimage);

        loadImageName();
        editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte=new Intent(accountactivity.this,editactivity.class);
                startActivity(inte);
            }
        });

        signouttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(accountactivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return;


            }
        });
    }

    private void loadImageName() {
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
                            name.setText(name1);
                        }
                        if(map.get("ProfileImageurl")!=null){
                            imgurl=map.get("ProfileImageurl").toString();
                            Glide.with(getApplication()).load(imgurl).into(prfimg);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            Toast toast = new Toast(getApplicationContext());
            TextView tv = new TextView(accountactivity.this);
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
