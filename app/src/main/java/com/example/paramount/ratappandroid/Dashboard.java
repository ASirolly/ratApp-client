package com.example.paramount.ratappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.paramount.ratappandroid.dao.RatSightingDAO;
import com.example.paramount.ratappandroid.model.Model;
import com.example.paramount.ratappandroid.model.RatSighting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * The first screen that users see after they log in.
 * Created by Greg on 9/22/17.
 * Modified by William on 10/10/17
 */

public class Dashboard extends LoggedInBaseActivity {
    private static final String TAG = "DASHBOARD";

    private ArrayAdapter arrayAdapter;

    /**
     * Creates the dashboard page.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                Model.getInstance().getRatSightings());
        ListView sightingListView = (ListView) findViewById(R.id.sightingListView);
        sightingListView.setAdapter(arrayAdapter);

        Button mapButton = (Button) findViewById(R.id.mapViewButton);
        mapButton.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), MapsActivity.class);
            startActivity(intent);
        });

        //Sets the actions that happen when you click on an item in the listview
        sightingListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getBaseContext(), RatSightingDetails.class);
            intent.putExtra("ratSighting", Model.getInstance().getRatSightings().get(position));
            startActivity(intent);
        });

        sightingListView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                RatSightingDAO.getInstance().getRatSightings(page, Dashboard.this::handleData);
                return true;
            }
        });

        RatSightingDAO.getInstance().getRatSightings(0, Dashboard.this::handleData);

        // Causes add button to open a screen that allows the user to create a new rat sighting.
        findViewById(R.id.addSightingButton).setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), AddRatSightingActivity.class);
            startActivity(intent);
        });

    }

    /**
     * method called upon successful GET request
     * @param response list of RatSightings returned from GET request
     */
    private void handleData(JSONArray response) {
        JSONObject json;

        int len = response.length();
        for (int i = 0; i < len; i++) {
            try {
                json = (JSONObject) response.get(i);
                Model.getInstance().getRatSightings().add(new RatSighting(json));
                arrayAdapter.notifyDataSetChanged();
            } catch (JSONException | ParseException e) {
                Log.d(TAG, "error parsing rat sighting field");
            }
        }
    }
}


