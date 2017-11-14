package com.example.paramount.ratappandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private static final String TAG = "ADD_RAT_SIGHTING";
    private static final int WAIT_TIME = 500;

    /**
     * Creates the dashboard page.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rat_sighting);

        longitudeEditText = (EditText) findViewById(R.id.longitudeInput);
        latitudeEditText = (EditText) findViewById(R.id.latitudeInput);
        cityEditText = (EditText) findViewById(R.id.cityInput);
        locationTypeEditText = (EditText) findViewById(R.id.locationTypeInput);
        boroughEditText = (EditText) findViewById(R.id.boroughInput);
        addressEditText = (EditText) findViewById(R.id.addressInput);
        zipEditText = (EditText) findViewById(R.id.zipInput);

        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(this::submit);

        Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(view -> finish());
    }

    /**
     * Method that is called when submit button is clicked
     * @param view the view that submit button belongs to
     */
    private void submit(View view) {
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
                Log.w(TAG, "I can't believe you've done this");
            }
            Intent intent = new Intent(AddRatSightingActivity.this, Dashboard.class);
            AddRatSightingActivity.this.startActivity(intent);
        }
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

        RatSightingDAO ratSightingDAO = RatSightingDAO.getInstance();
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
