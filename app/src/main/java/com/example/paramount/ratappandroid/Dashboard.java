package com.example.paramount.ratappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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
    private final static String TAG = "DASHBOARD";
    private final static String baseUrl = "http://10.0.2.2:9292/api/rat_sightings?page=";
    RequestQueue requestQueue;

    Button mapButton;

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
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Model.getInstance().getRatSightings());
        final ListView sightingListView = (ListView) findViewById(R.id.sightingListView);
        sightingListView.setAdapter(arrayAdapter);

        mapButton = (Button) findViewById(R.id.mapViewButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        //Sets the actions that happen when you click on an item in the listview
        sightingListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getBaseContext(), RatSightingDetails.class);
                        intent.putExtra("ratSighting", Model.getInstance().getRatSightings().get(position));
                        startActivity(intent);
                    }
                }
        );

        sightingListView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadNextDataFromApi(page);
                System.out.println("IN SET ON SCROLL LISTENER");
                return true;
            }
        });

        loadNextDataFromApi(0);

        // Causes add button to open a screen that allows the user to create a new rat sighting.
        findViewById(R.id.addSightingButton).setOnClickListener(
                new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), AddRatSightingActivity.class);
                        startActivity(intent);
                    }
                });

    }

    /**
     * Loads the next data from the api once user scrolls to the bottom
     * @param offset
     */
    public void loadNextDataFromApi(int offset) {
        System.out.println("Inside loadNextData");
        String getDataUrl = baseUrl + offset;
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, getDataUrl, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject json;

                        int len = response.length();
                        for (int i = 0; i < len; i++) {
                            try {
                                json = (JSONObject) response.get(i);
                                Model.getInstance().getRatSightings().add(new RatSighting(json));
                                arrayAdapter.notifyDataSetChanged();
                            } catch (JSONException | ParseException e) {
                                Log.w(TAG, e);
                            }
                        }
                        System.out.println("Inside the onResponse Method");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Having error: " + error.getMessage());
                        // TODO Auto-generated method stub

                    }
                });

        System.out.println("right before adding JsOBJreq");
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
        }
    }


