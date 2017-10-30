package com.example.paramount.ratappandroid.dao;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Greg on 10/29/17.
 *
 * Data Access Object for RatSightings. Uses singleton design pattern.
 */

public class RatSightingDAO {
    private static final String TAG = "rat_sighting_dao";
    private static final String baseUrl = "http://10.0.2.2:9292/api";
    private static final SimpleDateFormat requestDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    private final RequestQueue requestQueue;
    private static RatSightingDAO _instance;

    private RatSightingDAO(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static RatSightingDAO getInstance(Context context) {
        if (_instance == null) {
            _instance = new RatSightingDAO(context);
        }
        return _instance;
    }

    /**
     * Get rat sightings with a certain creation date, with a default limit of 100.
     * @param startDate earliest date for the selected rat sightings
     * @param endDate latest date for the selected rat sightings
     * @param callback callback providing an onSuccess method that will be called after a response to the request is received.
     */
    public void getRatSightingsByDate(Date startDate, Date endDate, Callback<JSONArray> callback) {
        getRatSightingsByDate(startDate, endDate, 100, callback);
    }

    /**
     * Get rat sightings with a certain creation date
     * @param startDate earliest date for the selected rat sightings
     * @param endDate latest date for the selected rat sightings
     * @param limit maximum number of records to be returned
     * @param callback callback providing an onSuccess method that will be called after a response to the request is received.
     */
    public void getRatSightingsByDate(Date startDate, Date endDate, int limit, Callback<JSONArray> callback) {
        String resourceUrl = "/rat_sightings_by_date?";

        String startDateParam = String.format("start_date=%s", requestDateFormat.format(startDate));
        String endDateParam = String.format("end_date=%s", requestDateFormat.format(endDate));
        String limitParam = "limit=" + limit;
        String allParams = StringUtils.join(new String[] {startDateParam, endDateParam, limitParam}, "&");

        String url = baseUrl + resourceUrl + allParams;
        makeJsonArrayRequest(url, callback);
    }

    /**
      * Loads a single page of rat sightings
      * @param page which page of rat sightings to get
      */
    public void getRatSightings(int page, Callback<JSONArray> callback) {
        String resourceUrl = "/rat_sightings?";

        String allParams = "page=" + page;

        String url = baseUrl + resourceUrl + allParams;
        makeJsonArrayRequest(url, callback);
    }

    /**
     * Creates a rat sighting using the provided values
     * @param params map from attribute name to value
     * @param callback provides function to be called after receiving a response
     */
    public void createRatSighting(Map<String, String> params, Callback<String> callback) {
        makePostRequest(baseUrl + "/rat_sightings?", params, callback);
    }
    

    /**
     * Makes a JSONArrayRequest
     * @param url url for the request
     * @param callback provides function to be called after receiving a response
     */
    private void makeJsonArrayRequest(String url, Callback<JSONArray> callback) {
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
            (Request.Method.GET, url, null,
                callback::onSuccess,
                error -> Log.w(TAG, "Having error: " + error.getMessage())
            );

        int socketTimeout = 30000; // 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

    /**
     * Makes a POST request using the provided values
     * @param url url for the request
     * @param params parameters for the request
     * @param callback provides function to be called after receiving a response
     */
    private void makePostRequest(String url, Map<String, String> params, Callback<String> callback) {
        StringRequest stringRequest = new StringRequest(
            Request.Method.POST,
            url,
            callback::onSuccess,
            error -> {
                String body;
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    body = e.getMessage();
                }
                Log.w(TAG, String.format("create rat sighting error. body is: %s", body));
                error.printStackTrace();
            }) {

            @Override
            protected Map<String,String> getParams() {
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
