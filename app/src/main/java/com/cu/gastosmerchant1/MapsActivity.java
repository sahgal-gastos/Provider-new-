package com.cu.gastosmerchant1;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.gastosmerchant.R;
import com.cu.gastosmerchant1.Data.Account_data;
import com.cu.gastosmerchant1.Data.Userinfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final String TAG ="tag123";
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(30.7691099, 76.5758537);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
   public static EditText upiedit,upiedit2,tranedit;
    Boolean check =false;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference ref;
    Account_data data;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);
//         mLastKnownLocation =new Location(Double.parseDouble(String.valueOf(mDefaultLocation.latitude)) ,Double.parseDouble(String.valueOf(mDefaultLocation.longitude)) );
//        mLastKnownLocation.setLatitude(Double.parseDouble(String.valueOf(mDefaultLocation.latitude)) );
//        mLastKnownLocation.setLongitude(Double.parseDouble(String.valueOf(mDefaultLocation.longitude)));
        button=(Button)findViewById(R.id.scanbtn);
        upiedit = findViewById(R.id.editText5);
        upiedit2 = findViewById(R.id.editText8);
//        paythmedit = findViewById(R.id.editText9);
//        paytmaditagin = findViewById(R.id.editText10);
        tranedit = findViewById(R.id.editText11);

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLastKnownLocation ==null){
                    getDeviceLocation();
                }
                else{
                    check=false;
                    Toast.makeText(MapsActivity.this,upiedit2.getText()+"  "+upiedit.getText(),Toast.LENGTH_SHORT).show();
                    if(upiedit.getText().toString().trim().equals("")){
                        upiedit.setError("Invalid Input");
                        check=true;

                    }
//                    if(paythmedit.getText().toString().trim().equals("")){
//                        paythmedit.setError("Invalid Input");
//                        check=true;
//
//                    }
                    if(!upiedit.getText().toString().trim().equals(upiedit2.getText().toString().trim())){
                        check=true;
                        upiedit.setError("UPI ID Doesn't Match");
                        upiedit2.setError("UPI ID Doesn't Match");
                    }
//                    if(!paythmedit.getText().toString().trim().equals(paytmaditagin.getText().toString().trim())){
//                        check=true;
//                        paytmaditagin.setError("Paytm No Doesn't Match");
//                        paythmedit.setError("Paytm No Doesn't Match");
//
//                    }
                    if(mLastKnownLocation.getLongitude()==0.0||mLastKnownLocation.getLatitude()==0.0 ){
                        check=true;
                        Toast.makeText(MapsActivity.this,"Location error",Toast.LENGTH_SHORT).show();

                    }
                    if(!check) {
                        data = new Account_data();

                        if (tranedit.getText().toString().trim().equals("")) {
                            data.setPaytm("0");
                        }

                        if (tranedit.getText().toString().length() == 24) {
                            data.setPaytm(tranedit.getText().toString().trim());
                        }

                        if (tranedit.getText().toString().length() < 24 &&!tranedit.getText().toString().trim().equals("")) {
                            tranedit.setError("Invalid");
                            check = true;
                        }

                        if (!check) {

                            data.setUpi(upiedit.getText().toString().trim());
                            data.setLatitude(mLastKnownLocation.getLatitude());
                            data.setLongitude(mLastKnownLocation.getLongitude());
                            auth = FirebaseAuth.getInstance();

                            database = FirebaseDatabase.getInstance();
                            ref = database.getReference("Merchant_data/" + auth.getUid() + "");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ref = database.getReference("Merchant_data/" + auth.getUid() + "/data");
                                    ref.setValue(data);
                                    ref.push();

                                    ref = database.getReference("Location-based/" + dataSnapshot.getValue(Userinfo.class).getLocation() + "/" + auth.getUid() + "/data");
                                    ref.setValue(data);
                                    ref.push();
                                    Toast.makeText(MapsActivity.this, "Finished", Toast.LENGTH_SHORT).show();

                                    ref = database.getReference("Merchant_search/" + data.getUpi());
                                    ref.setValue(auth.getUid());
                                    Intent it=new Intent(MapsActivity.this,Home.class);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                      //  Toast.makeText(MapsActivity.this, mLastKnownLocation.getLatitude() + "  " + mLastKnownLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
         mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
         SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //verifypermission();
                startActivity(new Intent(getApplicationContext(),Scan.class));

            }
        });


    }





    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);
                mLastKnownLocation.setLongitude(latLng.longitude);
                mLastKnownLocation.setLatitude(latLng.latitude);
                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title("Your Location");
                Toast.makeText(MapsActivity.this,latLng.latitude+"  "+latLng.longitude,Toast.LENGTH_SHORT).show();
                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }
        });


        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Task<Location> location=mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful())

                        {mMap.clear();
                            mLastKnownLocation =task.getResult();
                            Location location1 =task.getResult();
                            LatLng ll = new LatLng(location1.getLatitude(),location1.getLongitude());
                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 19);
                            mMap.animateCamera(update);
                            MarkerOptions markerOptions = new MarkerOptions();

                            // Setting the position for the marker
                            LatLng latLng = new LatLng(location1.getLatitude(),location1.getLongitude());

                            markerOptions.position(latLng);
                            mMap.addMarker(markerOptions);

                        }
                    }
                });

                return false;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI(false);

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));



                                MarkerOptions markerOptions = new MarkerOptions();

                                // Setting the position for the marker
                                LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());

                                markerOptions.position(latLng);

                                // Setting the title for the marker.
                                // This will be displayed on taping the marker
                                markerOptions.title("Your Location");
                                Toast.makeText(MapsActivity.this,latLng.latitude+"  "+latLng.longitude,Toast.LENGTH_SHORT).show();
                                // Clears the previously touched position
                                mMap.clear();

                                // Animating to the touched position
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                                // Placing a marker on the touched position
                                mMap.addMarker(markerOptions);
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));

                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }



    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        String[] permission ={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permission[0])
                == PackageManager.PERMISSION_GRANTED &&ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permission[1])==PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    permission,
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    updateLocationUI(true);

                }
            }
        }
        updateLocationUI(true);
    }


    private void updateLocationUI(Boolean bool ) {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                if(bool==true)
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }



}