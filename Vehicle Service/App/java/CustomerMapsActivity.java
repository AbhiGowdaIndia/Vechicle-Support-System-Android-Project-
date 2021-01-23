package com.example.admin.vehicleservice3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
//import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
//import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CustomerMapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private GoogleMap mMap;
    String custId;
    String Service_id="";
    private SupportMapFragment mapFragment;
    LinearLayout MechInfo, dummy, Ratinglayout;
    CircularImageView MechImage, Rimage;
    TextView MechName, MechNum, Rname,serviceid;
    GoogleApiClient mGoogleApiClient;
    RatingBar ratingBar, RatingView;
    Boolean requestBol = false;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    ConnectionDetector ConDet;
    protected Marker pickupMarker;
    FloatingActionButton Account, CallMech;
    Button GoOnline, mechget, Gpson, Complete, Rate;
    boolean visiblity = false;
    LatLng pickupLocation;
    LatLng mechanicLatLng;
    private float MechanicRating;
    String veh;
    String SlectdVeh = "";
    String twowheel = "Two_Wheeler";
    String threewheel = "Three_Wheeler";
    String fourwheel = "Four_Wheeler";
    String sixwheel = "Six_wheeler";
    String sixaxe = "Six-axel_wheeler";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_maps);
       // polylines = new ArrayList<>();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerMapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            mapFragment.getMapAsync(this);
        }
        veh = getIntent().getStringExtra("vehicletype");
        ConDet = new ConnectionDetector(this);
        MechInfo = (LinearLayout) findViewById(R.id.cust_info);
        dummy = (LinearLayout) findViewById(R.id.dummy_info);
        RatingView = (RatingBar) findViewById(R.id.ratingView);
        Ratinglayout = (LinearLayout) findViewById(R.id.rating);
        Rimage = (CircularImageView) findViewById(R.id.r_image);
        Rname = (TextView) findViewById(R.id.r_name);
        serviceid=(TextView)findViewById(R.id.S_id);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        MechImage = (CircularImageView) findViewById(R.id.p_image);
        CallMech = (FloatingActionButton) findViewById(R.id.call);
        MechNum = (TextView) findViewById(R.id.m_num);
        MechName = (TextView) findViewById(R.id.name);
        Account = (FloatingActionButton) findViewById(R.id.account);
        mechget = (Button) findViewById(R.id.getmech);
        Complete = (Button) findViewById(R.id.Completed);
        Rate = (Button) findViewById(R.id.rate);
        mechget.setText("Getting mechanic nearest to you...");
        GoOnline = (Button) findViewById(R.id.goonline);
        Gpson = (Button) findViewById(R.id.gpson);
        RatingView.setFocusable(false);

        if (veh.equals(twowheel)) {
            SlectdVeh = "2 Wheelers";
        }
        if (veh.equals(threewheel)) {
            SlectdVeh = "3 Wheelers";
        }
        if (veh.equals(fourwheel)) {
            SlectdVeh = "4 and 6 Wheelers";
        }
        if (veh.equals(sixwheel)) {
            SlectdVeh = "4 and 6 Wheelers";
        }
        if (veh.equals(sixaxe)) {
            SlectdVeh = "4 and 6 Wheelers";
        }

        Complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mechget.setVisibility(View.GONE);
                DisplayRatingInfo();
                DisplayRatingLayout();
            }
        });

        CallMech.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                String s = "tel:" + MechNum.getText().toString();
                Intent intents = new Intent(Intent.ACTION_CALL);
                intents.setData(Uri.parse(s));
                startActivity(intents);
            }
        });

        Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(ConDet.isConnected()) {
                  custId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                 DatabaseReference ratingRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechanicFoundId).child("Ratings");
                 ratingRef.child(Service_id).setValue(MechanicRating);
                 requestBol = false;
                 geoQuery.removeAllListeners();
                 MechName.setText("");
                 MechNum.setText("");
                 MechImage.setImageResource(R.mipmap.account_icon);
                 mechanicLocationRef.removeEventListener(mechanicLocationRefListener);
                 if (mechanicFoundId != null) {
                     DatabaseReference mechanicRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechanicFoundId).child("Request");
                     mechanicRef.removeValue();
                     DatabaseReference remove = FirebaseDatabase.getInstance().getReference().child("Users").child("Todelete").child(custId);
                     remove.removeValue();
                     mechanicFoundId = null;
                 }
                 mechanicFound = false;
                 String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                 DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CustomerRequest");
                 GeoFire geoFire = new GeoFire(ref);
                 geoFire.removeLocation(userId);

                 if (pickupMarker != null) {
                     pickupMarker.remove();
                     mMechanicMarker.remove();

                 }
                 Ratinglayout.setVisibility(View.GONE);
                 Rname.setText("");
                 Rimage.setImageResource(R.mipmap.account_icon);
                 mechget.setText("Getting mechanic nearest to you...");
                 mechget.setVisibility(View.INVISIBLE);
                 Complete.setVisibility(View.GONE);
                 GoOnline.setVisibility(View.VISIBLE);
                 GoOnline.setText("REQUEST");
             }else{

                 Toast toast=new Toast(getApplicationContext());
                 TextView tv= new TextView(CustomerMapsActivity.this);
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
        });


        Gpson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean gpsvalue=isGPSEnabled(getApplicationContext());
                if (!gpsvalue){
                    Toast toast=new Toast(getApplicationContext());
                    TextView tv= new TextView(CustomerMapsActivity.this);
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
                    Gpson.setVisibility(View.INVISIBLE);
                    //Toast.makeText(CustomerMapsActivity.this,"GPS On Invisble and GO ONline visible",Toast.LENGTH_SHORT).show();
                    GoOnline.setVisibility(View.VISIBLE);
                    GoOnline.setClickable(false);
                }
            }
        });
        dummy.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (visiblity){
                    MechInfo.setVisibility(View.VISIBLE);
                    dummy.setVisibility(View.INVISIBLE);
                }

                return false;
            }
        });
        MechInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (visiblity){
                    MechInfo.setVisibility(View.INVISIBLE);
                    dummy.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });


        GoOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean gpsvalue=isGPSEnabled(getApplicationContext());
                if (!gpsvalue){
                    Toast toast=new Toast(getApplicationContext());
                    TextView tv= new TextView(CustomerMapsActivity.this);
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
                        // GoOnline.setText("CANCEL REQUEST");
                        GoOnline.setText("Processing...");
                        GoOnline.setClickable(false);


                        mechget.setText("Getting mechanic nearest to you...");
                        mechget.setVisibility(View.INVISIBLE);
                        if(requestBol){
                            requestBol=false;
                            geoQuery.removeAllListeners();
                            MechInfo.setVisibility(View.GONE);
                            visiblity= false;
                            MechName.setText("");
                            MechNum.setText("");
                            MechImage.setImageResource(R.mipmap.account_icon);
                            mechanicLocationRef.removeEventListener(mechanicLocationRefListener);
                            if(mechanicFoundId != null){
                                DatabaseReference mechanicRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechanicFoundId).child("Request");
                                mechanicRef.removeValue();
                                DatabaseReference remove = FirebaseDatabase.getInstance().getReference().child("Users").child("Todelete").child(custId);
                                remove.removeValue();
                                mechanicFoundId = null;
                            }
                            mechanicFound=false;
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CustomerRequest");
                            GeoFire geoFire = new GeoFire(ref);
                            geoFire.removeLocation(userId);

                            if(pickupMarker != null){
                                pickupMarker.remove();
                                mMechanicMarker.remove();

                            }
                           /* if(mMechanicMarker != null){

                            }*/
                            GoOnline.setText("REQUEST");
                            GoOnline.setClickable(true);
                        }else{
                            requestBol=true;
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CustomerRequest");
                            GeoFire geoFire = new GeoFire(ref);
                            geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
                            pickupLocation = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                            if(veh.equals(twowheel)){
                                pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("I am here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_2w_icon)));

                            }
                            if(veh.equals(threewheel)){
                                pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("I am here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_3w_icon)));

                            }
                            if(veh.equals(fourwheel)){
                                pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("I am here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_4w_icon)));

                            }
                            if(veh.equals(sixwheel)){
                                pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("I am here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_6aw_icon)));

                            }
                            if(veh.equals(sixaxe)){
                                pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("I am here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_6bw_icon)));

                            }
                            mechget.setVisibility(View.VISIBLE);

                            getClosestMechanic();
                        }
                    }else{
                        Toast toast=new Toast(getApplicationContext());
                        TextView tv= new TextView(CustomerMapsActivity.this);
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
                    Intent intent1 = new Intent(CustomerMapsActivity.this, accountactivity.class);
                    startActivity(intent1);
                }else {
                    Toast toast = new Toast(getApplicationContext());
                    TextView tv = new TextView(CustomerMapsActivity.this);
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

    private void DisplayRatingInfo() {

        DatabaseReference rateDetailsref = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechanicFoundId);
        rateDetailsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {

                    Map<String,Object> map=(Map<String,Object>) dataSnapshot.getValue();

                    if(map.get("First name")!=null){
                        Rname.setText(map.get("First name").toString());
                    }

                    if(map.get("ProfileImageurl")!=null){
                        Glide.with(getApplication()).load(map.get("ProfileImageurl").toString()).into(Rimage);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void DisplayRatingLayout() {

        MechInfo.setVisibility(View.GONE);
        visiblity=false;
        dummy.setVisibility(View.GONE);
        Ratinglayout.setVisibility(View.VISIBLE);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                MechanicRating=rating;
            }
        });
    }

    private int radius = 1;
    protected Boolean mechanicFound = false;
    protected String mechanicFoundId="";
    GeoQuery geoQuery;
    private void getClosestMechanic(){
        DatabaseReference mechanicLocation = FirebaseDatabase.getInstance().getReference().child("MechanicsAvailable");

        GeoFire geoFire = new GeoFire(mechanicLocation);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!mechanicFound && requestBol){
                    DatabaseReference CustRef=FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(key);
                    CustRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                                Map<String,Object> mechmap=(Map<String,Object>) dataSnapshot.getValue();

                                if(mechanicFound){
                                    return;
                                }
                                if(mechmap.get("Vehicles").equals(SlectdVeh)){
                                    mechanicFound = true;
                                    mechanicFoundId = dataSnapshot.getKey();
                                    DatabaseReference mechanicRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechanicFoundId).child("Request");
                                    //DatabaseReference ratingRef1 = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechanicFoundId);
                                    Service_id=mechanicRef.push().getKey();
                                    mechanicRef.child("Service_id").setValue(Service_id);
                                    String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference justref=FirebaseDatabase.getInstance().getReference().child("Users").child("Todelete").child(customerId).child("MechanicId");
                                    justref.setValue(mechanicFoundId);
                                    HashMap map = new HashMap();
                                    map.put("CustomerServiceId", customerId);
                                    map.put("ServiceFor",veh);
                                    mechanicRef.updateChildren(map);
                                    getMechanicLocation();
                                    mechget.setText("Looking for mechanic Location....");

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!mechanicFound)
                {
                    radius++;
                    getClosestMechanic();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }
    protected Marker mMechanicMarker;
    DatabaseReference mechanicLocationRef;
    protected  ValueEventListener mechanicLocationRefListener;
    private void getMechanicLocation(){
        DatabaseReference mechanicRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechanicFoundId).child("Request");

        mechanicLocationRef = FirebaseDatabase.getInstance().getReference().child("MechanicsWorking").child(mechanicFoundId).child("l");
        mechanicLocationRefListener = mechanicLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && requestBol){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    mechget.setText("Mechanic Found");
                    getAssignedMechanicInfo(mechanicFoundId);
                    visiblity= true;
                    GoOnline.setText("CANCEL REQUEST");
                    GoOnline.setClickable(true);
                    if(map.get(0) != null){
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng mechanicLatLng = new LatLng(locationLat,locationLng);
                    if(mMechanicMarker != null){
                        mMechanicMarker.remove();
                    }
                    Location loc1 =  new Location("");
                    loc1.setLatitude(pickupLocation.latitude);
                    loc1.setLongitude(pickupLocation.longitude);

                    Location loc2 =  new Location("");
                    loc2.setLatitude(mechanicLatLng.latitude);
                    loc2.setLongitude(mechanicLatLng.longitude);

                    float distance = loc1.distanceTo(loc2);


                    if(distance<100){
                        mechget.setText("Mechanic is Here");
                        GoOnline.setVisibility(View.INVISIBLE);
                        Complete.setVisibility(View.VISIBLE);
                    }else{
                        mechget.setText("Mechanic Found:" +String.valueOf(distance));
                    }

                    mMechanicMarker = mMap.addMarker(new MarkerOptions().position(mechanicLatLng).title("your Mechanic").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_mechanic_icon)));
                   // getRouteToMarker(mechanicLatLng);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

   /* private void getRouteToMarker(LatLng mechanicLatLng) {
LatLng myloc=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(myloc,mechanicLatLng)
                .build();
        routing.execute();
    }*/

    private void getAssignedMechanicInfo(String mechid) {
        MechInfo.setVisibility(View.VISIBLE);

        DatabaseReference mechDetailsref = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(mechid);
        mechDetailsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {

                    Map<String,Object> map=(Map<String,Object>) dataSnapshot.getValue();

                    if(map.get("First name")!=null){
                        MechName.setText(map.get("First name").toString());
                    }
                    serviceid.setText(Service_id);
                    if(map.get("Mobile number")!=null){
                        MechNum.setText(map.get("Mobile number").toString());
                    }

                    if(map.get("ProfileImageurl")!=null){
                        Glide.with(getApplication()).load(map.get("ProfileImageurl").toString()).into(MechImage);
                    }
                    int ratingSum = 0;
                    float ratingCount = 0;
                    float ratingAvg=0;
                    for(DataSnapshot child:dataSnapshot.child("Ratings").getChildren()){
                        ratingSum=ratingSum + Integer.valueOf(child.getValue().toString());
                        ratingCount++;
                    }
                    if(ratingCount != 0){
                        ratingAvg=ratingSum / ratingCount;
                        RatingView.setRating(ratingAvg);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(12.3256906, 76.7007858))
                .title("Marker"));*/

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerMapsActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);

        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(getApplicationContext()!=null){
            mLastLocation = location;
            if (ConDet.isConnected()){
                LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                GoOnline.setClickable(true);

            }else{
                Toast toast = new Toast(getApplicationContext());
                TextView tv = new TextView(CustomerMapsActivity.this);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerMapsActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    final int LOCATION_REQUEST_CODE=1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){

            case LOCATION_REQUEST_CODE:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mapFragment.getMapAsync(this);
                }else{
                    Toast.makeText(getApplicationContext(),"Please provide the permission.",Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    private boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
/*
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};



    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        LatLngBounds.Builder builder =new LatLngBounds.Builder();
        builder.include(mechanicLatLng);
        builder.include(pickupLocation);
        LatLngBounds bounds=builder.build();

        int width=getResources().getDisplayMetrics().widthPixels;
        int padding=(int) (width*0.2);
        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngBounds(bounds,padding);
        mMap.animateCamera(cameraUpdate);
        //mMap.addMarker(new MarkerOptions().position(mechanicLatLng).title("Customer location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_mechanic_icon)));
        //mMap.addMarker(new MarkerOptions().position(pickupLocation).title("I am here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_2w_icon)));


        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }*/
}
