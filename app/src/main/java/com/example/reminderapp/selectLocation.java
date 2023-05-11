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


    private Button mBtnLocation;
    EditText et_rem,et_desc;
    Button btn_add_rem;
    private DBHandler dbHandler;

    public float distance[];

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
        et_desc = findViewById(R.id.et_desc);
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

        mBtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent2 = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent2);
            }
        });

        btn_add_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_rem.getText().toString().length() == 0) {
                    Toast.makeText(selectLocation.this, "Enter a valid title!", Toast.LENGTH_SHORT).show();
                } else if (spotLatitude == null || spotLongitude == null) {
                    Toast.makeText(selectLocation.this, "Please select a valid location", Toast.LENGTH_SHORT).show();
                }


                //code for adding details to database table - user's reminder
                else {
                    dbHandler.addReminderRecord(et_desc.getText().toString(),et_rem.getText().toString(), Address, spotLatitude, spotLongitude,"12345678900");
                    Toast.makeText(selectLocation.this, "Reminder has been saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(selectLocation.this, WelcomePage.class));

                }
            }
        });
    }



}


