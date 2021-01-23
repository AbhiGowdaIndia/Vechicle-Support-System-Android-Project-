package com.example.admin.vehicleservice3;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class selectvehicletype extends AppCompatActivity {

    CardView card2,card3,card4,card6,card6axe;
    ConnectionDetector ConDet;
    private String twowheel="Two_Wheeler";
    private String threewheel="Three_Wheeler";
    private String fourwheel="Four_Wheeler";
    private String sixwheel="Six_wheeler";
    private String sixaxe="Six-axel_wheeler";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectvehicletype);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ConDet = new ConnectionDetector(this);
        card2=(CardView)findViewById(R.id.twowheel);
        card3=(CardView)findViewById(R.id.threewheel);
        card4=(CardView)findViewById(R.id.fourwheel);
        card6=(CardView)findViewById(R.id.sixwheel);
        card6axe=(CardView)findViewById(R.id.sixaxelwheel);


        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card2.setCardBackgroundColor(Color.parseColor("#3c65cc"));
                SelectVehicle(twowheel);
                Intent inte=new Intent(selectvehicletype.this,CustomerMapsActivity.class);
               inte.putExtra("vehicletype", twowheel);
                inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(inte);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card3.setCardBackgroundColor(Color.parseColor("#3c65cc"));
                SelectVehicle(threewheel);
                Intent inte=new Intent(selectvehicletype.this,CustomerMapsActivity.class);
               inte.putExtra("vehicletype", threewheel);
                inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(inte);
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card4.setCardBackgroundColor(Color.parseColor("#3c65cc"));
                SelectVehicle(fourwheel);
                Intent inte=new Intent(selectvehicletype.this,CustomerMapsActivity.class);
               inte.putExtra("vehicletype", fourwheel);
                inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(inte);
            }
        });

        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card6.setCardBackgroundColor(Color.parseColor("#3c65cc"));
                SelectVehicle(sixwheel);
                Intent inte=new Intent(selectvehicletype.this,CustomerMapsActivity.class);
                inte.putExtra("vehicletype", sixwheel);
                inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(inte);
            }
        });

        card6axe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card6axe.setCardBackgroundColor(Color.parseColor("#3c65cc"));
                SelectVehicle(sixaxe);
                Intent inte=new Intent(selectvehicletype.this,CustomerMapsActivity.class);
               inte.putExtra("vehicletype", sixaxe);
                inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(inte);
            }
        });
    }
    private void SelectVehicle(String selectedvehicle) {

        if (ConDet.isConnected()) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference MechVehicle = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userId);
            MechVehicle.child("Vehicles").setValue(selectedvehicle);
        }else{
            Toast toast = new Toast(getApplicationContext());
            TextView tv = new TextView(selectvehicletype.this);
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
