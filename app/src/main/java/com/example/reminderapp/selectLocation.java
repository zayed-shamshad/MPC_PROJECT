package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.location.Address;
import android.location.Geocoder;

import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;

import java.util.List;
import java.util.Locale;

public class selectLocation extends AppCompatActivity{
//this is a comment

    private Button mBtnLocation;
    EditText et_location, et_rem;
    FloatingActionButton btn_add_rem;
    String lat1s, long1s;
    String str_loc;
    private DBHandler dbHandler;

    public float distance[];

    public String showToasts=" ";

    private String spotLatitude;
    private String spotLongitude,Address;

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(selectLocation.this);
        builder.setMessage("You will have to re-fill the form");
        builder.setTitle("Are you sure?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(selectLocation.this, WelcomePage.class));
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        distance = new float[2];

        et_rem = findViewById(R.id.et_rem); //USER SPECIFIED REMINDER
        //et_distance = findViewById(R.id.et_distance);
        btn_add_rem = findViewById(R.id.btn_add_rem);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            spotLatitude = bundle.getString("Latitude_req");
            spotLongitude = bundle.getString("Longitude_req");
            Toast.makeText(this, spotLatitude, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, spotLongitude, Toast.LENGTH_SHORT).show();
            Address = bundle.getString("address");

        }

        dbHandler = new DBHandler(selectLocation.this);
        mBtnLocation = findViewById(R.id.location_button);


        //checkMyPermissions(); //location settings permission (access or deny)

        mBtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent2 = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent2);
            }
        });


//
//        if (isPermissionGranted) {
//            if (checkGooglePlayServices()) {
//                System.out.println("Google Play Services are available.");
//                //Toast.makeText(this, "Google PlayServices are available", Toast.LENGTH_SHORT).show();
//                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_map);
//                supportMapFragment.getMapAsync(this);
//            } else {
//                System.err.println("Google Play Services are not available.");
//                //Toast.makeText(this, "Google PlayServices are not available", Toast.LENGTH_SHORT).show();
//            }
//        }

        //actions for after search button for location is clicked
//        img_search_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String location = et_location.getText().toString();
//                //float distance = Float.valueOf(et_distance.getText().toString());
//                if (location == null) {
//                    Toast.makeText(selectLocation.this, "Type a valid location!", Toast.LENGTH_SHORT).show();
//                }
////                else if(distance == 0){
////                    Toast.makeText(selectLocation.this, "Distance set as 250 meters", Toast.LENGTH_SHORT).show();
////                }
//                else {
//                    Geocoder geocoder = new Geocoder(selectLocation.this, Locale.getDefault());
//                    try {
//                        List<Address> listAddress = geocoder.getFromLocationName(location, 1);
//                        if (listAddress.size() > 0) {
//                            LatLng latLng = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());
//
//                            lat1 = listAddress.get(0).getLatitude(); //LATITUDE OF THE REMINDER LOCATION
//                            long1 = listAddress.get(0).getLongitude(); //LONGITUDE OF THE REMINDER LOCATION
//
//                            lat1s = Double.toString(lat1); //for toast msg
//                            long1s = Double.toString(long1); //for toast msg
//                            Geocoder geocoder2 = new Geocoder(selectLocation.this, Locale.getDefault());
//                            try{
//                                List<Address>addressList1 = geocoder2.getFromLocation(lat1, long1,1);
//                                if(addressList1.size()>0){
//                                    str_loc = et_location.getText().toString() + " " + addressList1.get(0).getThoroughfare() + " " + addressList1.get(0).getSubThoroughfare() + " " + addressList1.get(0).getLocality() + " " + addressList1.get(0).getSubLocality() + " " + addressList1.get(0).getFeatureName()  + " " + addressList1.get(0).getCountryName() + " " + addressList1.get(0).getPostalCode();
//                                    Toast.makeText(selectLocation.this, "You have entered the location: " + str_loc, Toast.LENGTH_SHORT).show();
//                                }
//                            }catch(Exception e){
//                                e.printStackTrace();
//                                Toast.makeText(selectLocation.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                            }
//
//
//                            if (mHere != null) {
//                                mHere.remove();
//                            }
//                            MarkerOptions markerOptions = new MarkerOptions();
//                            markerOptions.title("Here");
//                            markerOptions.position(latLng);
//                            mHere = mGoogleMap.addMarker(markerOptions);
//                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 5);
//                            mGoogleMap.animateCamera(cameraUpdate);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        //actions for after reminder + button is clicked
        btn_add_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_rem.getText().toString().length() == 0) {
                    Toast.makeText(selectLocation.this, "Enter valid reminder!", Toast.LENGTH_SHORT).show();
                } else if (spotLatitude == null || spotLongitude == null) {
                    Toast.makeText(selectLocation.this, "Please select a valid location", Toast.LENGTH_SHORT).show();
                }

                //code for adding details to database table - user's reminder
                else {
                    dbHandler.addReminderRecord(et_rem.getText().toString(), Address, spotLatitude, spotLongitude,"1234567890");
                    Toast.makeText(selectLocation.this, "Reminder has been saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(selectLocation.this, WelcomePage.class));

                }
            }
        });
    }
//    private boolean checkGooglePlayServices() {
//        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
//        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
//        if(result == ConnectionResult.SUCCESS){
//            return true;
//        }else if(googleApiAvailability.isUserResolvableError(result)){
//            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, 201, new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    Toast.makeText(selectLocation.this, "User cancelled", Toast.LENGTH_SHORT).show();
//                }
//            });
//            dialog.show();
//        }
//        return false;
//    }
//    private void checkMyPermissions() {
//        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
//            @Override
//            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                isPermissionGranted = true;
//                //Toast.makeText(selectLocation.this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
//                System.out.println("Location permissions were granted by user.");
//            }
//
//            @Override
//            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                Intent intent_settings = new Intent();
//                intent_settings.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                Uri uri = Uri.fromParts("package", getPackageName(), "");
//                intent_settings.setData(uri);
//                startActivity(intent_settings);
//            }
//
//            @Override
//            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                permissionToken.continuePermissionRequest();
//            }
//        }).check();
//    }


//    @SuppressLint("MissingPermission")
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        mGoogleMap = googleMap;
//        mGoogleMap.setMyLocationEnabled(true);
//        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
//    }
}


