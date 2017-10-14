package com.example.paramount.ratappandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.paramount.ratappandroid.model.Model;
import com.example.paramount.ratappandroid.model.RatSighting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The first screen that users see after they log in.
 * Created by Greg on 9/22/17.
 * Modified by William on 10/10/17
 */

public class Dashboard extends LoggedInBaseActivity {
    private int pageToLoad = 1;
    final String baseUrl = "http://10.0.2.2:9292/api/rat_sightings?page=";
    RequestQueue requestQueue;
    ArrayList<Object> ratSightings = new ArrayList<>(25);


    /**
     * Creates the dashboard page.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
//        requestQueue = Model.getInstance().getRequestQueue();
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        //Keeping the array as Object for now
        //The below sets up the listview
        //RatSighting[] ratSightings = new RatSighting[25];
        //\Object[] ratSightings = new Object[]{"test1", "test2"};
//        ArrayList<Object> ratSightings = new ArrayList<>();

        ratSightings.add("Test1");
        ratSightings.add("Test2");

        System.out.println("This is arraylist: " + ratSightings);

        //final ListView
        final ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ratSightings);
        final ListView sightingListView = (ListView) findViewById(R.id.sightingListView);
        sightingListView.setAdapter(arrayAdapter);
        //Sets the actions that happen when you click on an item in the listview
        sightingListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //For funsies just gets the toString of the clicked item and makes it a toast
                        String funsy = String.valueOf(parent.getItemAtPosition(position));

                        //Makes the toast. Can be taken out
                        Toast toast = Toast.makeText(getApplicationContext(), String.format("clicked item: %s", funsy), Toast.LENGTH_LONG);
                        toast.show();
                        //loadNextDataFromApi(1);
                    }
                }
        );

        sightingListView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                pageToLoad++;
                loadNextDataFromApi(pageToLoad);
                System.out.println("IN SET ON SCROLL LISTENER");
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    public void loadNextDataFromApi(int offset) {
        System.out.println("Inside loadNextData");
        String getDataUrl = baseUrl + offset;
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, getDataUrl, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject temp;

                        int len = response.length();
                        for (int i = 0; i < len; i++) {
                            try {
                                temp = (JSONObject) response.get(i);
                                //System.out.println("This is Object " + i + " : " + temp);
                                System.out.println(temp.get("location_type"));
                                RatSighting sighting = new RatSighting();
                                //sighting.setBorough((String) temp.get("Borough"));
                                Object test = temp.get("location_type");
                                ratSightings.add(temp.get("location_type"));

                            } catch (JSONException e) {
                                System.out.println(e);
                            }
                        }
                        System.out.println("Inside the onResponse Method");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Having error: " + error);
                        // TODO Auto-generated method stub

                    }
                });

        System.out.println("right before adding JsOBJreq");

        requestQueue.add(jsObjRequest);

        }
    }


