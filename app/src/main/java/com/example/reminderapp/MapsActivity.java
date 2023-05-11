package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;





public class MapsActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;


    Button btn;
    private final static int PLACE_PICKER_REQUEST = 999;
    private final static int LOCATION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private boolean locationPermissionGranted;
    private String TvFileName, TvInfo, TvLocation;
    private Uri uri;
    LatLng myLocation ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps); // my_location_demo

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        getLocationPermission();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        enableMyLocation();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13f));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17f)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else {
            Toast.makeText(this, "Please click my location button\n(Top Right corner)", Toast.LENGTH_SHORT).show();
            LatLng point =  new LatLng(26, 73);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,5.0f));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(5.0f));
        }

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                Toast.makeText(getParent(), "Hi", Toast.LENGTH_SHORT).show();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                markerOptions.title(getAddress(latLng));
                mMap.clear();
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                        latLng, 15f);
                mMap.animateCamera(location);
                mMap.addMarker(markerOptions);
            }
        });


    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }
    //
    public boolean onMyLocationButtonClick() {
//        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            Toast.makeText(this, "Fetching your location...", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Please turn on the location of the device and try again.", Toast.LENGTH_SHORT).show();
        }

        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
    //
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }
    //
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        if (requestCode != LOCATION_REQUEST_CODE) {
            Toast.makeText(this, "Go", Toast.LENGTH_SHORT).show();
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();

        } else {

            permissionDenied = true;
        }

    }




    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private String getAddress(LatLng latLng){

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {

                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Receive bundle
            Bundle receive = getIntent().getExtras();

//            Bitmap bitmap = getIntent().getParcelableExtra("image");

            DialogFragment dialogFragment = new ConfirmAddress();

            Bundle args = new Bundle();
            args.putDouble("lat", latLng.latitude);
            args.putDouble("long", latLng.longitude);
            args.putString("address", address);

            String req_latitude = latLng.latitude+"";
            String req_longitude = latLng.longitude+"";

            //Toast.makeText(MainMaps.this, lat+" "+lon, Toast.LENGTH_SHORT).show();


            dialogFragment.setArguments(args);
            dialogFragment.show(ft, "dialog");
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return "No Address Found";

        }

    }
}