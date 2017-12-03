package com.example.paramount.ratappandroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paramount.ratappandroid.dao.RatSightingDAO;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 10/22/17.
 *
 * Activity for user to add a new rat sighting.
 */

public class AddRatSightingActivity extends LoggedInBaseActivity {

    private static final String TAG = "ADD_RAT_SIGHTING";
    private static final int WAIT_TIME = 500;
    private static final int INITIAL_REQUEST = 1337;

    /**
     * Declare EditTexts here so that they can be used in createRatSighting() method
     */
    private EditText longitudeEditText;
    private EditText latitudeEditText;
    private EditText cityEditText;
    private EditText locationTypeEditText;
    private EditText boroughEditText;
    private EditText addressEditText;
    private EditText zipEditText;

    private LocationManager locationManager;

    /**
     * Creates the dashboard page.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rat_sighting);

        longitudeEditText = findViewById(R.id.longitudeInput);
        latitudeEditText = findViewById(R.id.latitudeInput);
        cityEditText = findViewById(R.id.cityInput);
        locationTypeEditText = findViewById(R.id.locationTypeInput);
        boroughEditText = findViewById(R.id.boroughInput);
        addressEditText = findViewById(R.id.addressInput);
        zipEditText = findViewById(R.id.zipInput);

        locationManager = (LocationManager) AddRatSightingActivity.this.getSystemService(Context.LOCATION_SERVICE);

        Button submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(this::submit);

        Button cancel = findViewById(R.id.cancelButton);
        cancel.setOnClickListener(view -> finish());
    }

    /**
     * Method that is called when submit button is clicked
     * @param view the view that submit button belongs to
     */
    private void submit(View view) {
        Log.d("this", view.toString());
        if (StringUtils.isEmpty(longitudeEditText.getText())) {
            showMessage("longitude field is empty");
        } else if (StringUtils.isEmpty(latitudeEditText.getText())) {
            showMessage("latitude field is empty");
        } else if (StringUtils.isEmpty(cityEditText.getText())) {
            showMessage("city field is empty");
        } else if (StringUtils.isEmpty(locationTypeEditText.getText())) {
            showMessage("location type field is empty");
        } else if (StringUtils.isEmpty(boroughEditText.getText())) {
            showMessage("borough field is empty");
        } else if (StringUtils.isEmpty(addressEditText.getText())) {
            showMessage("address field is empty");
        } else if (StringUtils.isEmpty(zipEditText.getText())) {
            showMessage("zip field is empty");
        } else {
            createRatSighting();
            // Black magic to start a new Dashboard activity (so the new sighting is displayed)
            // https://stackoverflow.com/a/4186097/5377941
            try {
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                Log.w(TAG, "Caught InterruptedException");
            }
            Intent intent = new Intent(AddRatSightingActivity.this, Dashboard.class);
            AddRatSightingActivity.this.startActivity(intent);
        }
    }

    private void populateLocationFields() {
        if (canAccessLocation()) {
            Log.i(TAG, "Have access to location");
                try {
                    Location lastKnown = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                    if (lastKnown == null) {
                        showMessage("Could not find your last known location");
                        Log.i(TAG, "Last known location was null");
                    } else {
                        Log.i(TAG, String.format(
                                "Successfully got last location with longitude %s and latitude %s",
                                lastKnown.getLongitude(),
                                lastKnown.getLatitude()));
                        longitudeEditText.setText(String.valueOf(lastKnown.getLongitude()), TextView.BufferType.EDITABLE);
                        latitudeEditText.setText(String.valueOf(lastKnown.getLatitude()), TextView.BufferType.EDITABLE);
                    }
                } catch (SecurityException e) {
                    Log.i(TAG, "Permission to use loc was rejected");
                }
        } else {
            Log.i(TAG, "Do not have permission to access location");
        }

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        populateLocationFields();
    }

    public void detectLocation(View view) {
        if (!canAccessLocation()) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, INITIAL_REQUEST);
        } else {
            populateLocationFields();
        }
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }

    /**
     * Creates a rat sighting using the values stored in activity's EditTexts
     */
    private void createRatSighting() {
        Map<String,String> params = new HashMap<>();

        Editable longitude = longitudeEditText.getText();
        params.put("longitude", longitude.toString());

        Editable latitude = latitudeEditText.getText();
        params.put("latitude", latitude.toString());

        Editable city = cityEditText.getText();
        params.put("city", city.toString());

        Editable locationType = locationTypeEditText.getText();
        params.put("location_type", locationType.toString());

        Editable borough = boroughEditText.getText();
        params.put("borough", borough.toString());

        Editable address = addressEditText.getText();
        params.put("address", address.toString());

        Editable zip = zipEditText.getText();
        params.put("zip", zip.toString());


        RatSightingDAO ratSightingDAO = RatSightingDAO.getInstance(getApplicationContext());
        ratSightingDAO.createRatSighting(params, response ->
            Log.i(TAG, String.format("create rat sighting response: %s", response))
        );
    }

    /**
     * Shows the message
     * @param message The message to be shown
     */
    private void showMessage(CharSequence message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
