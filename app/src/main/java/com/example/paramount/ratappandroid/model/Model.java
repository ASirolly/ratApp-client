package com.example.paramount.ratappandroid.model;


import android.content.Context;

import com.example.paramount.ratappandroid.dao.Callback;
import com.example.paramount.ratappandroid.dao.UserDAO;


/**
 * Holds application state information. Uses the singleton design pattern to allow
 * access to the model from each controller.
 *
 * Created by Greg on 9/22/17.
 */

public final class Model {
    // singleton instance
    private static Model _instance;
    private final UserDAO userDAO;

    // maps unique key to rat sighting

    private Model(Context context) {
        userDAO = UserDAO.getInstance(context);
    }

    /**
     * Returns a model instance.
     * @param context application context to initialize UserDAO
     * @return _instance An instance of the model.
     */
    public static synchronized Model getInstance(Context context) {
        if (_instance == null) {
            _instance = new Model(context);
        }
        return _instance; }

    /**
     * Determines whether the provided username/password combination is valid.
     * @param username provided username
     * @param password provided password
     * @param callback provides onSuccess method to execute upon successful registration
     */
    public void lookUpAccount(String username, String password, Callback<String> callback) {
        userDAO.authenticate(username, password, callback);
    }

    /**
     * Attempts to register an account.
     * @param account account to register
     * @param callback provides onSuccess method to execute upon successful registration
     */
    public void registerAccount(Account account, Callback<String> callback) {
        userDAO.createUser(account.getUsername(), account.getPassword(),
                account.getPassword(), callback);
    }

}
