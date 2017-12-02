package com.example.paramount.ratappandroid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

import com.example.paramount.ratappandroid.dao.RatSightingDAO;
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

import java.security.Security;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Joshua Reno on 10/21/17.
 *
 * Displays rat sightings on a map, with the ability to choose a date range for which to display
 * sightings.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MAPS_ACTIVITY";
    private static final SimpleDateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd",
            Locale.US);

    private GoogleMap googlemap;
    private final Map<String, RatSighting> ratSightingMap = new HashMap<>();
    private RatSightingDAO ratSightingDAO;

    private static final String selectStartDateButtonTextTemplate =
            "SELECT START DATE (selected date: %s)";
    private static final String selectEndDateButtonTextTemplate =
            "SELECT END DATE (selected date: %s)";
    private Button selectStartDateButton;
    private Button selectEndDateButton;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private Date startDate;
    private Date endDate;

    private LocationManager lookAtMe;

    /**
     * Creates the dashboard page.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        ratSightingDAO = RatSightingDAO.getInstance(getApplicationContext());

        //ratSightingMap = new HashMap<>();
        setDatePicker();
        lookAtMe = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Sets initial values for the date picker, and sets onClickListeners for date picker buttons
     * and findRatSightings button.
     */
    private void setDatePicker() {
        // initialize start date to one year ago, and end date to tomorrow
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.add(Calendar.YEAR, -1);
        startDate = calendar.getTime();

        startDatePickerDialog = new DatePickerDialog(MapsActivity.this);
        endDatePickerDialog = new DatePickerDialog(MapsActivity.this);
        selectStartDateButton = findViewById(R.id.selectStartDateButton);
        selectEndDateButton = findViewById(R.id.selectEndDateButton);
        Button findRatSightingsButton = findViewById(R.id.findRatSightings);

        // clicking start/end date button shows start/end date picker
        selectStartDateButton.setOnClickListener(click -> startDatePickerDialog.show());
        selectEndDateButton.setOnClickListener(click -> endDatePickerDialog.show());

        // set initial text for start/end date buttons
        selectStartDateButton.setText(String.format(selectStartDateButtonTextTemplate,
                displayDateFormat.format(startDate)));
        selectEndDateButton.setText(
                String.format(selectEndDateButtonTextTemplate, displayDateFormat.format(endDate)));

        // choosing date in start/end DatePicker stores that date in the startDate/endDate variable,
        // and changes the text of the button to display that date
        startDatePickerDialog.setOnDateSetListener((view, year, month, day) -> {
            calendar.set(year, month, day);
            startDate = calendar.getTime();
            selectStartDateButton.setText(String.format(selectStartDateButtonTextTemplate,
                    displayDateFormat.format(startDate)));
        });

        endDatePickerDialog.setOnDateSetListener((view, year, month, day) -> {
            calendar.set(year, month, day);
            endDate = calendar.getTime();
            selectEndDateButton.setText(String.format(selectEndDateButtonTextTemplate,
                    displayDateFormat.format(endDate)));
        });

        // clicking "find rat sightings" button causes only rat sightings that fall within
        // the selected dates to be shown
        findRatSightingsButton.setOnClickListener(view ->
                ratSightingDAO.getRatSightingsByDate(startDate, endDate, this::handleData)
        );
    }

    /**
     * Called after map is completely loaded.
     *
     * @param googleMap The map has has been loaded
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

        // When map is first loaded, load sightings that fall within the initial values for
        // start/end date
        ratSightingDAO.getRatSightingsByDate(startDate, endDate, this::handleData);

        googlemap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            /**
             * Gets the unique key associated with the rat sighting that was clicked, and opens a
             * details page displaying information about that sighting.
             * @param marker The marker that was clicked
             * @return true, to indicate that this action was consumed
             */
            @Override
            public boolean onMarkerClick(Marker marker) {
                String uniqueKey = (String) marker.getTag();
                Log.i(TAG, String.format("sighting with key %s was clicked", uniqueKey));
                Intent intent = new Intent(getBaseContext(), RatSightingDetails.class);
                intent.putExtra(
                        "ratSighting",
                        ratSightingMap.get(uniqueKey));
                startActivity(intent);
                return true;
            }
        });
    }

    /**
     * Displays all rat sightings on the map.
     */
    private void showAllRatSightings() {
        googlemap.clear();
        Collection<RatSighting> values = ratSightingMap.values();
        values.forEach(ratSighting -> {
                Log.i(TAG,
                        String.format(
                            "Placing marker at lat %f and long %f",
                            ratSighting.getLatitude(),
                            ratSighting.getLongitude()));
                MarkerOptions newMarkerOptions = new MarkerOptions();
                MarkerOptions markerOptions = newMarkerOptions.position(new LatLng(
                        ratSighting.getLatitude(),
                        ratSighting.getLongitude()));
                final Marker marker = googlemap.addMarker(markerOptions);
                marker.setTag(ratSighting.getUniqueKey());
        });
        MarkerOptions myOps = new MarkerOptions();
        MarkerOptions lolWut = new MarkerOptions();

        try {
            Location lastKnown = lookAtMe.getLastKnownLocation(lookAtMe.GPS_PROVIDER);
            lolWut = myOps.position(new LatLng(lastKnown.getLatitude(), lastKnown.getLongitude()));
            Marker marker = googlemap.addMarker(lolWut);
            marker.setTag("This is your current location: " + lastKnown.getLatitude()
                + " , " + lastKnown.getLongitude());
        } catch(SecurityException e) {
            Log.i(TAG, "Permission to use loc was rejected");
        }

    }

    /**
     * method called upon successful response from GET request
     * @param response list of RatSightings returned from GET request
     */
    private void handleData(JSONArray response) {
        ratSightingMap.clear();
        JSONObject json;
        int len = response.length();
        for (int i = 0; i < len; i++) {
            try {
                json = (JSONObject) response.get(i);
                RatSighting ratSighting = new RatSighting(json);
                ratSightingMap.put(ratSighting.getUniqueKey(), ratSighting);
            } catch (JSONException | ParseException e) {
                Log.w(TAG, e);
            }
        }
        showAllRatSightings();
    }
}
