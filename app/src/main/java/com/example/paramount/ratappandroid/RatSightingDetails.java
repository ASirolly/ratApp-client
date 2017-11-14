package com.example.paramount.ratappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.paramount.ratappandroid.model.RatSighting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Greg on 10/14/17.
 *
 * Displays details for a single RatSighting.
 */

public class RatSightingDetails extends LoggedInBaseActivity {
    private static final DateFormat displayDateFormat =
            new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.US);

    /**
     * Creates the dashboard page.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rat_sighting_details);
        Intent intent = getIntent();
        RatSighting ratSighting = (RatSighting) intent.getSerializableExtra("ratSighting");

        TextView keyValueTextView = (TextView) findViewById(R.id.keyValue);
        TextView createDateTextView = (TextView) findViewById(R.id.createDateValue);
        TextView locationTypeTextView = (TextView) findViewById(R.id.locationTypeValue);
        TextView zipTextView = (TextView) findViewById(R.id.zipValue);
        TextView addressTextView = (TextView) findViewById(R.id.addressValue);
        TextView cityTextView = (TextView) findViewById(R.id.cityValue);
        TextView boroughTextView = (TextView) findViewById(R.id.boroughValue);
        TextView latitudeTextView = (TextView) findViewById(R.id.latitudeValue);
        TextView longitudeTextView = (TextView) findViewById(R.id.longitudeValue);

        keyValueTextView.setText(ratSighting.getUniqueKey());
        createDateTextView.setText(displayDateFormat.format(ratSighting.getCreateDate()));
        locationTypeTextView.setText(ratSighting.getLocType());
        zipTextView.setText(ratSighting.getIncidentZip());
        addressTextView.setText(ratSighting.getIncidentAddress());
        cityTextView.setText(ratSighting.getCity());
        boroughTextView.setText(ratSighting.getBorough());
        latitudeTextView.setText(String.format(Locale.US, "%f", ratSighting.getLatitude()));
        longitudeTextView.setText(String.format(Locale.US, "%f", ratSighting.getLongitude()));
    }
}
