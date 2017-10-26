package com.example.paramount.ratappandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

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

import org.apache.commons.lang3.time.DateFormatUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by joshuareno on 10/21/17.
 *
 * Displays rat sightings on a map, with the ability to choose a date range for which to display sightings.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MAPS_ACTIVITY";
    private static final String baseUrl = "http://10.0.2.2:9292/api/rat_sightings";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    RequestQueue requestQueue;

    private GoogleMap googlemap;

    private Button selectStartDateButton;
    private Button selectEndDateButton;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private Date startDate;
    private Date endDate;

    private Button findRatSightingsButton;

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

        // initialize start and end dates to today
        startDate = new Date();
        endDate = new Date();

        startDatePickerDialog = new DatePickerDialog(MapsActivity.this);
        endDatePickerDialog = new DatePickerDialog(MapsActivity.this);
        selectStartDateButton = (Button) findViewById(R.id.selectStartDateButton);
        selectEndDateButton = (Button) findViewById(R.id.selectEndDateButton);
        findRatSightingsButton = (Button) findViewById(R.id.findRatSightings);

        // clicking start/end date button shows start/end date picker
        selectStartDateButton.setOnClickListener(click -> startDatePickerDialog.show());
        selectEndDateButton.setOnClickListener(click -> endDatePickerDialog.show());

        startDatePickerDialog.setOnDateSetListener((view, year, month, day) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            startDate = calendar.getTime();
            selectStartDateButton.setText(String.format("SELECT START DATE (current date: %s)", simpleDateFormat.format(startDate)));
        });

        endDatePickerDialog.setOnDateSetListener((view, year, month, day) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            endDate = calendar.getTime();
            selectEndDateButton.setText(String.format("SELECT END DATE (current date: %s)", simpleDateFormat.format(endDate)));
        });

        // clicking "find rat sightings" button causes only rat sightings that fall within
        // the selected dates to be shown
        findRatSightingsButton.setOnClickListener(view -> {
            filter(startDate, endDate);
        });
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

    /**
     * Get rat sightings with a certain creation date
     * @param startDate earliest date for the selected rat sightings
     * @param endDate latest date for the selected rat sightings
     */
    private void getData(String startDate, String endDate) {
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
                        showAllRatSightings();
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

    /**
     * Displays only certain rat sightings on the map
     * @param startDate earliest date for the displayed rat sightings
     * @param endDate latest date for the displayed rat sightings
     */
    private void filter(Date startDate, Date endDate) {
        googlemap.clear();
        Model.getInstance().getMapRatSightings().values().stream()
            .filter(ratSighting -> ratSighting.getCreateDate().after(startDate) && ratSighting.getCreateDate().before(endDate))
            .forEach(ratSighting -> {
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

    /**
     * Shows all rat sightings by calling the filter method with a date long in the past.
     */
    private void showAllRatSightings() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.YEAR, -100);
        Date aLongTimeAgo = calendar.getTime();
        filter(aLongTimeAgo, now);
    }
}
