package com.example.paramount.ratappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.paramount.ratappandroid.model.Model;
import com.example.paramount.ratappandroid.model.RatSighting;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by joshuareno on 10/21/17.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private final static String TAG = "MAPS_ACTIVITY";
    private final static String baseUrl = "http://10.0.2.2:9292/api/rat_sightings";
    RequestQueue requestQueue;

    private GoogleMap googlemap;

    /**
     * Creates the dashboard page.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());
    }

    /**
     * Called after map is completely loaded.
     *
     * @param googleMap The map thas has been loaded
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "onMapReady was called");
        this.googlemap = googleMap;
        googlemap.setOnMapClickListener((latLng) -> {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

        });

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true); // add buttons so user can zoom in and out

        getData("", "");

        googlemap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            /**
             * Gets the unique key associated with the rat sighting that was clicked, and opens a details page
             * displaying information about that sighting.
             * @param marker The marker that was clicked
             * @return true, to indicate that this action was consumed
             */
            @Override
            public boolean onMarkerClick(Marker marker) {
                String uniqueKey = (String) marker.getTag();
                Log.i(TAG, String.format("sighting with key %s was clicked", uniqueKey));
                Intent intent = new Intent(getBaseContext(), RatSightingDetails.class);
                intent.putExtra("ratSighting", Model.getInstance().getMapRatSightings().get(uniqueKey));
                startActivity(intent);
                return true;
            }
        });
    }

    public void getData(String startDate, String endDate) {
        String getDataUrl = baseUrl; // TODO: add start date and end date parameters
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, getDataUrl, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject json;

                        int len = response.length();
                        for (int i = 0; i < len; i++) {
                            try {
                                json = (JSONObject) response.get(i);
                                RatSighting ratSighting = new RatSighting(json);
                                Model.getInstance().getMapRatSightings().put(ratSighting.getUniqueKey(), ratSighting);
                            } catch (JSONException | ParseException e) {
                                Log.w(TAG, e);
                            }
                        }

                        // after rat sightings are loaded, display them on the map
                        Log.i(TAG, String.format("displaying %d rat sightings", len));
                        displayRatSightings();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Having error: " + error.getMessage());
                        // TODO Auto-generated method stub

                    }
                });
        requestQueue.add(jsObjRequest);
    }

    public void filter(String startDate, String endDate) {
        googlemap.clear();

    }

    /**
     * Creates a marker for each rat sighting, with each rat sighting's unique key as a tag for its marker
     */
    public void displayRatSightings() {
        googlemap.clear();
        Model.getInstance().getMapRatSightings().values().forEach(ratSighting -> {
            Log.i(TAG, String.format("Placing marker at lat %f and long %f", ratSighting.getLatitude(), ratSighting.getLongitude()));
            final Marker marker = googlemap.addMarker(
                    new MarkerOptions()
                            .position(
                                    new LatLng(
                                            ratSighting.getLatitude(),
                                            ratSighting.getLongitude())));
            marker.setTag(ratSighting.getUniqueKey());

        });
    }
}
