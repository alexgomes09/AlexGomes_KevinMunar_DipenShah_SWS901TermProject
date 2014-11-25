package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.graphics.Color;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PatientLocation extends Activity {

    GoogleMap googleMap;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_location);
        try{
            initializeMap();
        }catch(Exception e){
            e.printStackTrace();
        }


    }


    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
        Intent intent = getIntent();
        String patientName = intent.getStringExtra("patientName");
        double latitude = Double.parseDouble(intent.getStringExtra("latitude"));
        double longitude = Double.parseDouble(intent.getStringExtra("longitude"));

        final LatLng geoPoint = new LatLng(latitude,longitude);
        Marker pointLocation = googleMap.addMarker(new MarkerOptions().position(geoPoint).title(patientName));
        CameraUpdate view = CameraUpdateFactory.newLatLngZoom(geoPoint, 18);

        // Highlight the point on map. Code from Alex
        CircleOptions circleOptions = new CircleOptions()
                .center(geoPoint)
                .radius(2.0)
                .strokeWidth(3.0f)
                .strokeColor(Color.BLACK);

        pointLocation.showInfoWindow();
        googleMap.addCircle(circleOptions);

        // Move Camera to point on map
        googleMap.moveCamera(view);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        googleMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    @Override
    protected void onResume(){
        super.onResume();
        initializeMap();
    }

}
