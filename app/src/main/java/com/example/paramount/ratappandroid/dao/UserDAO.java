package com.example.paramount.ratappandroid.dao;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by greg on 11/1/17.
 *
 * Data Access Object for user class. Implements singleton design pattern
 */

public final class UserDAO {
    private static final String TAG = "user_dao";
    private static final String baseUrl = "http://10.0.2.2:9292/api/";

    private final RequestQueue requestQueue;
    private static UserDAO _instance;

    private UserDAO(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Initializes the singleton instance of this DAO if it has not been initialized, then returns
     * it.
     * @param context application context to initialize Volley request queue
     * @return singleton instance
     */
    public static synchronized UserDAO getInstance(Context context) {
        if (_instance == null) {
            _instance = new UserDAO(context);
        }
        return _instance;
    }

    /**
     * attempts to log in
     * @param email user email
     * @param password user password
     * @param successCallback provides an onSuccess method to be called upon a successful response from
     *                 the server
     */
    public void authenticate(String email, String password, SuccessCallback<String> successCallback,
                             FailureCallback<VolleyError> failureCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        makePostRequest(baseUrl + "login", params, successCallback, failureCallback);
    }

    /**
     * Creates a user
     * @param email user email
     * @param password user password
     * @param passwordConfirmation confirmation for password
     * @param successCallback provides an onSuccess method to be called upon a successful response from
     *                 the server
     */
    public void createUser(
            String email,
            String password,
            String passwordConfirmation,
            SuccessCallback<String> successCallback,
            FailureCallback<VolleyError> failureCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("password_confirmation", passwordConfirmation);

        makePostRequest(baseUrl + "users", params, successCallback, failureCallback);
    }

    /**
     * Makes a POST request using the provided values
     * @param url url for the request
     * @param params parameters for the request
     * @param successCallback provides function to be called after receiving a response
     */
    private void makePostRequest(
            String url, Map<String, String> params, SuccessCallback<String> successCallback,
            FailureCallback<VolleyError> failureCallback) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                successCallback::onSuccess,
                failureCallback::onFailure) {

            @Override
            protected Map<String,String> getParams() {
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }




}
