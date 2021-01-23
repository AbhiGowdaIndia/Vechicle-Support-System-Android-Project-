package com.example.admin.vehicleservice3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;

import com.firebase.geofire.GeoFire;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class OnKilled extends Service {


    String MechId="";
    String userId="";
    boolean yes = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
         userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Todelete").child(userId);
        assignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Map<String,Object> map=(Map<String,Object>) dataSnapshot.getValue();
                    //MechId=map.get("MechanicId").toString();
                    MechId = dataSnapshot.child("MechanicId").getValue().toString();
                    DatabaseReference mechanicRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(MechId).child("Request");
                    mechanicRef.removeValue();
                    DatabaseReference mechanicRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(MechId).child("Service_id");
                    mechanicRef2.removeValue();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

           DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CustomerRequest");
           GeoFire geoFire = new GeoFire(ref);
           geoFire.removeLocation(userId);
        final DatabaseReference remove = FirebaseDatabase.getInstance().getReference().child("Users").child("Todelete").child(userId);
        remove.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    remove.removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
