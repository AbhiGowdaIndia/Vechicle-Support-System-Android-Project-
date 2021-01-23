package com.example.admin.vehicleservice3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivityFirst extends FragmentActivity implements OnMapReadyCallback {
    ConnectionDetector ConDet;
    private GoogleMap mMap;
    FloatingActionButton Account,GPS;
    Button GoOnline;
    private static final int MY_PERMISSION_REQUESTCODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_first);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ConDet = new ConnectionDetector(this);
        Account=(FloatingActionButton)findViewById(R.id.account);
       // GPS=(FloatingActionButton)findViewById(R.id.gps);
        GoOnline=(Button)findViewById(R.id.goonline);
       // Setupgps();
        GoOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean gpsvalue=isGPSEnabled(getApplicationContext());
                if (!gpsvalue){

                    Toast toast=new Toast(getApplicationContext());
                    TextView tv= new TextView(MapsActivityFirst.this);
                    tv.setBackgroundColor(Color.BLACK);
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(16);
                    Typeface ty= Typeface.create("serif",Typeface.NORMAL);
                    tv.setTypeface(ty);
                    tv.setPadding(10,10,10,10);
                    tv.setText("Please turn on your location");
                    toast.setView(tv);
                    toast.show();

                }else{
                    if (ConDet.isConnected()) {
                        Intent intent = new Intent(MapsActivityFirst.this, CustomerMapsActivity.class);
                        startActivity(intent);
                    }else{
                        Toast toast=new Toast(getApplicationContext());
                        TextView tv= new TextView(MapsActivityFirst.this);
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

        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConDet.isConnected()) {
                    Intent intent1 = new Intent(MapsActivityFirst.this, accountactivity.class);
                    startActivity(intent1);
                }else {
                    Toast toast = new Toast(getApplicationContext());
                    TextView tv = new TextView(MapsActivityFirst.this);
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
            });

    }


    private boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*// Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

}
