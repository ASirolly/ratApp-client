package com.example.paramount.ratappandroid.dao;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.paramount.ratappandroid.App;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Paramount on 2017/11/4.
 *
 * Data access object for graph dates.
 */

public final class GraphDateDAO {
    private static final String TAG = "graph_date_dao";
    private static final String baseUrl = "http://10.0.2.2:9292/api/rat_sightings/frequency?";
    private final RequestQueue requestQueue;
    private static final GraphDateDAO _instance = new GraphDateDAO();
    private static final SimpleDateFormat requestDateFormat = new SimpleDateFormat("dd/MM/yyyy",
            Locale.US);
    private static final int TIMEOUT_TIME = 30000;

    private GraphDateDAO() {
         requestQueue = Volley.newRequestQueue(App.getContext());
    }

    /**
     * Returns the singleton instance of this DAO.
     * @return singleton instance
     */
    public static GraphDateDAO getInstance() {
        return _instance;
    }

    /**
     * Get graph dates between startDate and endDate
     * @param startDate starting date
     * @param endDate ending date
     * @param callback function to call upon successful response
     */
    public void getDates(Date startDate, Date endDate, Callback<JSONObject> callback) {
        String startDateParam = String.format("start_date=%s", requestDateFormat.format(startDate));
        String endDateParam = String.format("end_date=%s", requestDateFormat.format(endDate));
        String allParams = StringUtils.join(new String[] {startDateParam, endDateParam}, "&");

        String url = baseUrl + allParams;
        makeRequest(url, callback);
    }

    private void makeRequest(String url, Callback<JSONObject> callback) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        callback::onSuccess,
                        error -> {
                            Log.w(TAG, "We're SINKING");
                            Log.w(TAG, "Having error: " + error.getMessage()
                            );
                        }
                );
        RetryPolicy policy = new DefaultRetryPolicy(TIMEOUT_TIME,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

}
