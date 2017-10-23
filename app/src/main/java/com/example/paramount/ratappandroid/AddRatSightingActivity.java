package com.example.paramount.ratappandroid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
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
    EditText longitudeEditText;
    EditText latitudeEditText;
    EditText cityEditText;
    EditText locationTypeEditText;
    EditText boroughEditText;
    EditText addressEditText;
    EditText zipEditText;

    private final static String TAG = "ADD_RAT_SIGHTING";
    private final static String baseUrl = "http://10.0.2.2:9292/api/rat_sightings";
    private RequestQueue requestQueue;

    /**
     * Creates the dashboard page.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rat_sighting);
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        longitudeEditText = (EditText) findViewById(R.id.longitudeInput);
        latitudeEditText = (EditText) findViewById(R.id.latitudeInput);
        cityEditText = (EditText) findViewById(R.id.cityInput);
        locationTypeEditText = (EditText) findViewById(R.id.locationTypeInput);
        boroughEditText = (EditText) findViewById(R.id.boroughInput);
        addressEditText = (EditText) findViewById(R.id.addressInput);
        zipEditText = (EditText) findViewById(R.id.zipInput);

        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(view -> {
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
            }
        });

        Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(view -> finish());
    }

    public void createRatSighting() {
        StringRequest stringRequest = new StringRequest(
            Request.Method.POST,
            baseUrl,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i(TAG, String.format("create rat sighting response: %s", response));
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String body;
                    try {
                        body = new String(error.networkResponse.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        body = e.getMessage();
                    }
                    Log.w(TAG, String.format("create rat sighting error. body is: %s", body));
                    error.printStackTrace();
                }
            }) {

            @Override
            protected Map<String,String> getParams() {
            Map<String,String> params = new HashMap<>();
            params.put("longitude", longitudeEditText.getText().toString());
            params.put("latitude", latitudeEditText.getText().toString());
            params.put("city", cityEditText.getText().toString());
            params.put("location_type", locationTypeEditText.getText().toString());
            params.put("borough", boroughEditText.getText().toString());
            params.put("address", addressEditText.getText().toString());
            params.put("zip", zipEditText.getText().toString());

            return params;
            }
        };

        Log.i(TAG, "right before adding stringRequest");
        requestQueue.add(stringRequest);
    }

    /**
     * Shows the message
     * @param message The message to be shown
     */
    private void showMessage(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
