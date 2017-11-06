package com.example.paramount.ratappandroid.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.paramount.ratappandroid.App;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by Paramount on 2017/11/4.
 */

public class GraphDateDAO {
    private static final String TAG = "graph_date_dao";
    private static final String baseUrl = "http://10.0.2.2:9292/api/rat_sightings/frequency?";
    private static RequestQueue requestQueue;
    private static final GraphDateDAO _instance = new GraphDateDAO();
    private static final SimpleDateFormat requestDateFormat = new SimpleDateFormat("dd/MM/yyyy",
            Locale.US);
    private static final int TIMEOUT_TIME = 30000;

    private GraphDateDAO() {
         requestQueue = Volley.newRequestQueue(App.getContext());
    }

    public static GraphDateDAO getInstance() {
        return _instance;
    }
    public void getDates(Date startDate, Date endDate, Callback<JSONObject> callback) {

        //Do some stuff about
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
