package com.example.reminderapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ConfirmAddress extends DialogFragment implements
        android.view.View.OnClickListener, OnMapReadyCallback {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    private GoogleMap mMap;
    MapView mapView;
    public Double Lat;
    public Double Long;
    String Address;
    TextView myAddress;
    TextView myLatitude;
    TextView myLongitude;
    Button SelectBtn;
    Button ChangeBtn;
    private String TvFileName, TvInfo, TvLocation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Lat = getArguments().getDouble("lat");
        Long = getArguments().getDouble("long");
        Address = getArguments().getString("address");

    }
    MapFragment mapFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_confirm_address, container, false);
        myAddress=(TextView)v.findViewById(R.id.myAddress);
        SelectBtn=(Button) v.findViewById(R.id.Select);
        ChangeBtn=(Button) v.findViewById(R.id.Change);
        myLatitude=(TextView)v.findViewById(R.id.myTVLatitude);
        myLongitude=(TextView)v.findViewById(R.id.myTVLongitude);


        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapp);
        mapFragment.getMapAsync(this);

        SelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),myLatitude.getText().toString(),Toast.LENGTH_LONG).show();
                Bundle bundleLocation = new Bundle();
                bundleLocation.putString("Latitude_req",myLatitude.getText().toString());
                bundleLocation.putString("Longitude_req",myLongitude.getText().toString());
                bundleLocation.putString("address",myAddress.getText().toString());

                Intent i = new Intent(getActivity(), selectLocation.class);
                i.putExtras(bundleLocation);
                startActivity(i);
                getFragmentManager().beginTransaction().remove(mapFragment).commit();
                dismiss();
            }
        });
        ChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(mapFragment).commit();
                dismiss();
            }
        });

        getDialog().setCanceledOnTouchOutside(true);
        return v;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getFragmentManager().beginTransaction().remove(mapFragment).commit();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dismiss();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myAddress.setText(Address);
        myLatitude.setText(Lat+"");
        myLongitude.setText(Long+"");
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(Lat,Long));

        markerOptions.title(Address);
        mMap.clear();
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                new LatLng(Lat,Long), 14f);
        mMap.animateCamera(location);
        mMap.addMarker(markerOptions);
//        Log.d("status","success");
    }


}
