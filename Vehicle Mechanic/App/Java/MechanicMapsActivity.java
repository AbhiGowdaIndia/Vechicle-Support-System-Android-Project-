package com.example.admin.vehiclemechanic;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MechanicMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    // private Button mLogout;
    private SupportMapFragment mapFragment;
    private LinearLayout CustInfo, dummy;
    private CircularImageView CustImage;
    private TextView CustName, CustNum,serviceid;
    ConnectionDetector ConDet;
    FloatingActionButton Account, CallCust;
    Button GoOnline, Gpson;
    Boolean online = false;
    boolean visiblity = false;
    String status = "";
    String Service_id="";
    private String customerId = "";
    private String ServiceFor = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //polylines = new ArrayList<>();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MechanicMapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            mapFragment.getMapAsync(this);
        }

        dummy = (LinearLayout) findViewById(R.id.dummy_info);
        CustInfo = (LinearLayout) findViewById(R.id.cust_info);
        CustImage = (CircularImageView) findViewById(R.id.p_image);
        CallCust = (FloatingActionButton) findViewById(R.id.call);
        CustNum = (TextView) findViewById(R.id.m_num);
        CustName = (TextView) findViewById(R.id.name);
        serviceid=(TextView)findViewById(R.id.S_id);
        ConDet = new ConnectionDetector(this);
        Account = (FloatingActionButton) findViewById(R.id.account);
        GoOnline = (Button) findViewById(R.id.goonline);
        Gpson = (Button) findViewById(R.id.gpson);
        dummy.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (visiblity) {
                    CustInfo.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });
        CustInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (visiblity) {
                    CustInfo.setVisibility(View.INVISIBLE);
                    dummy.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });

        CallCust.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                String s = "tel:"+CustNum.getText().toString();
                Intent intents = new Intent(Intent.ACTION_CALL);
                intents.setData(Uri.parse(s));
                startActivity(intents);
            }
        });

        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConDet.isConnected()) {
                    Intent intent1 = new Intent(MechanicMapsActivity.this, accountactivity.class);
                    intent1.putExtra("status",status);
                    startActivity(intent1);
                }else {
                    Toast toast = new Toast(getApplicationContext());
                    TextView tv = new TextView(MechanicMapsActivity.this);
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

        Gpson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean gpsvalue=isGPSEnabled(getApplicationContext());
                if (!gpsvalue){
                    Toast toast=new Toast(getApplicationContext());
                    TextView tv= new TextView(MechanicMapsActivity.this);
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
                    GoOnline.setVisibility(View.VISIBLE);
                }
            }
        });

        GoOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean gpsvalue=isGPSEnabled(getApplicationContext());
                if (!gpsvalue){
                    Toast toast=new Toast(getApplicationContext());
                    TextView tv= new TextView(MechanicMapsActivity.this);
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
                    if (ConDet.isConnected()){
                        GoOnline.setText("GO OFFLINE");
                        //GoOnline.setClickable(false);
                        if(online){
                            online=false;
                            status="off";
                            DisconnectMechanic();
                            GoOnline.setText("GO ONLINE");
                        }else{
                            online=true;
                            status="on";
                            ConnectMechanic();
                            getAssignedCustomer();
                        }
                    }
                }
            }
        });

    }

    private void ConnectMechanic(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }


    private void DisconnectMechanic() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MechanicsAvailable");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);

    }

    private void getAssignedCustomer() {
        String MechanicId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanics").child(MechanicId).child("Request");
        assignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                customerId = "";
                if (dataSnapshot.exists()) {
                    customerId = dataSnapshot.child("CustomerServiceId").getValue().toString();
                    ServiceFor = dataSnapshot.child("ServiceFor").getValue().toString();
                    Service_id = dataSnapshot.child("Service_id").getValue().toString();

                   // Toast.makeText(MechanicMapsActivity.this,"Cust Id: " +customerId,Toast.LENGTH_SHORT).show();

                    getAssignedCustomerPickupLocation();
                    getAssignedCustomerInfo();
                    visiblity = true;

                } else {
                    //erasePolylines();
                    customerId = "";
                    if (pickupMarker != null) {
                        pickupMarker.remove();
                    }
                    if (assignedCustomerPickupLocationRefListener != null) {
                        assignedCustomerPickupLocationRef.removeEventListener(assignedCustomerPickupLocationRefListener);
                    }
                    CustInfo.setVisibility(View.GONE);
                    visiblity = false;
                    CustName.setText("");
                    CustNum.setText("");
                    CustImage.setImageResource(R.mipmap.account_icon);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getAssignedCustomerInfo() {
        CustInfo.setVisibility(View.VISIBLE);
        DatabaseReference CustDetailsref = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(customerId);
        CustDetailsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {

                    Map<String,Object> map=(Map<String,Object>) dataSnapshot.getValue();

                    if(map.get("First name")!=null){
                        CustName.setText(map.get("First name").toString());
                    }
                    if(map.get("Mobile number")!=null){
                        CustNum.setText(map.get("Mobile number").toString());
                    }
                        serviceid.setText(Service_id);
                    if(map.get("ProfileImageurl")!=null){
                        Glide.with(getApplication()).load(map.get("ProfileImageurl").toString()).into(CustImage);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    LatLng ServiceLatLng;
    private Marker pickupMarker;
    private DatabaseReference assignedCustomerPickupLocationRef;
    private ValueEventListener assignedCustomerPickupLocationRefListener;
    private void getAssignedCustomerPickupLocation(){
        assignedCustomerPickupLocationRef = FirebaseDatabase.getInstance().getReference().child("CustomerRequest").child(customerId).child("l");
        assignedCustomerPickupLocationRefListener = assignedCustomerPickupLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&& !customerId.equals("")){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    if(map.get(0) != null){
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                     ServiceLatLng = new LatLng(locationLat,locationLng);
                    if(ServiceFor.equals("Two_Wheeler")) {
                        pickupMarker = mMap.addMarker(new MarkerOptions().position(ServiceLatLng).title("Customer location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_2w_icon)));
                    }
                    if(ServiceFor.equals("Three_Wheeler")) {
                        pickupMarker = mMap.addMarker(new MarkerOptions().position(ServiceLatLng).title("Customer location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_3w_icon)));
                    }
                    if(ServiceFor.equals("Four_Wheeler")) {
                        pickupMarker = mMap.addMarker(new MarkerOptions().position(ServiceLatLng).title("Customer location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_4w_icon)));
                    }
                    if(ServiceFor.equals("Six_wheeler")) {
                        pickupMarker = mMap.addMarker(new MarkerOptions().position(ServiceLatLng).title("Customer location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_6aw_icon)));
                    }
                    if(ServiceFor.equals("Six-axel_wheeler")) {
                        pickupMarker = mMap.addMarker(new MarkerOptions().position(ServiceLatLng).title("Customer location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_6bw_icon)));
                    }

                    //getRouteToMarker(ServiceLatLng);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

   /* private void getRouteToMarker(LatLng serviceLatLng) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()), serviceLatLng)
                .build();
        routing.execute();

    }*/


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MechanicMapsActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);

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
            if (ConDet.isConnected()) {
                mLastLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
               mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14),5000,null);
                //Gpson.setVisibility(View.INVISIBLE);
                GoOnline.setClickable(true);

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("MechanicsAvailable");
                DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("MechanicsWorking");
                GeoFire geoFireAvailable = new GeoFire(refAvailable);
                GeoFire geoFireWorking = new GeoFire(refWorking);

                switch (customerId) {
                    case "":
                        geoFireWorking.removeLocation(userId);
                        geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                        GoOnline.setClickable(true);
                        break;

                    default:
                        geoFireAvailable.removeLocation(userId);
                        geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                        GoOnline.setClickable(false);
                        break;
                }
            }else{
                Toast toast = new Toast(getApplicationContext());
                TextView tv = new TextView(MechanicMapsActivity.this);
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
            ActivityCompat.requestPermissions(MechanicMapsActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }



    @Override
    protected void onStop() {
        super.onStop();

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
    private boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
  /*  private List<Polyline> polylines;
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
        //builder.include(mLastLocation);
        builder.include(ServiceLatLng);
        LatLngBounds bounds=builder.build();

        int width=getResources().getDisplayMetrics().widthPixels;
        int padding=(int) (width*0.2);
        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngBounds(bounds,padding);
        mMap.animateCamera(cameraUpdate);
        mMap.addMarker(new MarkerOptions().position(ServiceLatLng).title("Customer location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_2w_icon)));

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

    }

    private void erasePolylines(){
        for(Polyline line: polylines){
            line.remove();
        }
        polylines.clear();

    }*/
}
