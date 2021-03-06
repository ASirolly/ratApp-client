package com.example.paramount.ratappandroid;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by joshuareno on 10/21/17.
 *
 * Displays rat sightings on a map, with the ability to choose a date range for which to display sightings.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MAPS_ACTIVITY";
    private static final String baseUrl = "http://10.0.2.2:9292/api/rat_sightings_by_date?";
    private static final SimpleDateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat requestDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US); // TODO: put this into a separate class along with getData()

    private RequestQueue requestQueue;

    private GoogleMap googlemap;

    private static final String selectStartDateButtonTextTemplate = "SELECT START DATE (selected date: %s)";
    private static final String selectEndDateButtonTextTemplate = "SELECT END DATE (selected date: %s)";
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

        // initialize start date to one year ago, and end date to tomorrow
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.add(Calendar.YEAR, -1);
        startDate = calendar.getTime();

        startDatePickerDialog = new DatePickerDialog(MapsActivity.this);
        endDatePickerDialog = new DatePickerDialog(MapsActivity.this);
        selectStartDateButton = (Button) findViewById(R.id.selectStartDateButton);
        selectEndDateButton = (Button) findViewById(R.id.selectEndDateButton);
        findRatSightingsButton = (Button) findViewById(R.id.findRatSightings);

        // clicking start/end date button shows start/end date picker
        selectStartDateButton.setOnClickListener(click -> startDatePickerDialog.show());
        selectEndDateButton.setOnClickListener(click -> endDatePickerDialog.show());

        // set initial text for start/end date buttons
        selectStartDateButton.setText(String.format(selectStartDateButtonTextTemplate, displayDateFormat.format(startDate)));
        selectEndDateButton.setText(String.format(selectEndDateButtonTextTemplate, displayDateFormat.format(endDate)));

        // choosing a date in the start/end DatePicker stores that date in the startDate/endDate variable,
        // and changes the text of the button to display that date
        startDatePickerDialog.setOnDateSetListener((view, year, month, day) -> {
            calendar.set(year, month, day);
            startDate = calendar.getTime();
            selectStartDateButton.setText(String.format(selectStartDateButtonTextTemplate, displayDateFormat.format(startDate)));
        });

        endDatePickerDialog.setOnDateSetListener((view, year, month, day) -> {
            calendar.set(year, month, day);
            endDate = calendar.getTime();
            selectEndDateButton.setText(String.format(selectEndDateButtonTextTemplate, displayDateFormat.format(endDate)));
        });

        // clicking "find rat sightings" button causes only rat sightings that fall within
        // the selected dates to be shown
        findRatSightingsButton.setOnClickListener(view -> {
            getData(startDate, endDate);
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

        // When map is first loaded, load sightings that fall within the initial values for start/end date
        getData(startDate, endDate);

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
     * Get rat sightings with a certain creation date, with a default limit of 100.
     * @param startDate earliest date for the selected rat sightings
     * @param endDate latest date for the selected rat sightings
     */
    private void getData(Date startDate, Date endDate) {
        getData(startDate, endDate, 100);
    }

    /**
     * Get rat sightings with a certain creation date
     * @param startDate earliest date for the selected rat sightings
     * @param endDate latest date for the selected rat sightings
     * @param limit maximum number of records to be returned
     */
    private void getData(Date startDate, Date endDate, int limit) {
        String startDateParam = String.format("start_date=%s", requestDateFormat.format(startDate));
        String endDateParam = String.format("end_date=%s", requestDateFormat.format(endDate));
        String limitParam = String.format("limit=%d", limit);
        String allParams = StringUtils.join(new String[] {startDateParam, endDateParam, limitParam}, "&");
        String getDataUrl = baseUrl + allParams;

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, getDataUrl, null,

                    (response) -> {
                        Model.getInstance().resetMapRatSightings();

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
                    },

                    (error) -> System.out.println("Having error: " + error.getMessage())
                );
        requestQueue.add(jsObjRequest);
    }

    /**
     * Displays all rat sightings on the map.
     */
    public void showAllRatSightings() {
        googlemap.clear();
        Model.getInstance().getMapRatSightings().values()
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
}
