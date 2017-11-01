package com.example.paramount.ratappandroid.dao;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.paramount.ratappandroid.App;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by greg on 11/1/17.
 *
 * Data Access Object for user class.
 */

public class UserDAO {
    private static final String TAG = "user_dao";
    private static final String baseUrl = "http://10.0.2.2:9292/api/";

    private final RequestQueue requestQueue;
    private static final UserDAO _instance = new UserDAO();

    private UserDAO() {
        requestQueue = Volley.newRequestQueue(App.getContext());
    }

    public static UserDAO getInstance() {
        return _instance;
    }

    public void authenticate(String email, String password, Callback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        makePostRequest(baseUrl + "login", params, callback);
    }

    public void createUser(String email, String password, String passwordConfirmation, Callback<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("password_confirmation", passwordConfirmation);

        makePostRequest(baseUrl + "users", params, callback);
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
                    Log.w(TAG, String.format("create user error. body is: %s", body));
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
