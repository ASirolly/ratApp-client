package com.example.paramount.ratappandroid;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by joshuareno on 10/21/17.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{
    private final static String TAG = "MAPSACTIVITY";
    private final static String baseUrl = "http://10.0.2.2:9292/api/rat_sightings?page=";
    RequestQueue requestQueue;

    private GoogleMap googlemap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googlemap = googleMap;
        googlemap.setOnMapClickListener((latLng) -> {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

        });
    }

    public void getData(String startDate, String endDate) {

    }
}
