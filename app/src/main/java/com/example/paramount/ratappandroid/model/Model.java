package com.example.paramount.ratappandroid.model;


import com.example.paramount.ratappandroid.App;
import com.example.paramount.ratappandroid.dao.Callback;
import com.example.paramount.ratappandroid.dao.UserDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds application state information. Uses the singleton design pattern to allow
 * access to the model from each controller.
 *
 * Created by Greg on 9/22/17.
 */

public class Model {
    // singleton instance
    private static final Model _instance = new Model();
    private ArrayList<RatSighting> ratSightings;
    private UserDAO userDAO;

    // maps unique key to rat sighting
    private Map<String, RatSighting> mapRatSightings;

    private Model() {
        // leaving in test account for now
        ratSightings = new ArrayList<>(25);
        mapRatSightings = new HashMap<>();
        userDAO = UserDAO.getInstance(App.getContext());
    }

    /**
     * Returns a model instance.
     * @return _instance An instance of the model.
     */
    public static Model getInstance() { return _instance; }

    /*
     * Account of the currently logged in user.
      */
    private Account account;

    /**
     * Get list of rat sightings.
     */
    public ArrayList<RatSighting> getRatSightings() {
        return ratSightings;
    }

    /**
     * Clears out list of rat sightings.
     */
    public void resetRatSightings() {
        ratSightings.clear();
    }

    /**
     * @return list of rat sightings for display on the map.
     */
    public Map<String, RatSighting> getMapRatSightings() { return mapRatSightings; }

    /**
     * Clears out list of rat sightings for display on the map.
     */
    public void resetMapRatSightings() { mapRatSightings.clear(); }

    /**
     * Sets the account to null when an actor logs out.
     */
    public void logOut() { account = null; }

    /**
     * Sets the account variable.
     * @param account an instance of account
     */
    public void setAccount(Account account) { this.account = account; }

    /**
     * Returns the viewed account instance.
     * @return account An instance of account
     */
    public Account getAccount() { return account; }

    /**
     * Makes a call to the backend to determine whether the provided username/password combination is valid.
     * @return the corresponding account if the combination is valid, and none otherwise
     */
    public void lookUpAccount(String username, String password, Callback<String> callback) {
//        Account account = allAccounts.get(username);
//        if (account == null) { // there is no account with the given username
//            return null;
//        }
//        if (password.equals(account.getPassword())) { // account exists and correct password was provided
//            return account;
//        }
//        return null; // account exists, but wrong password provided
        userDAO.authenticate(username, password, callback);
    }

    /**
     * Attempts to register an account.
     * @return true if successful and false if unsuccessful (e.g. because the provided username is already taken).
     */
    public void registerAccount(Account account, Callback<String> callback) {
//        if (allAccounts.containsKey(account.getUsername())) { // username already taken
//            return false;
//        }
//        allAccounts.put(account.getUsername(), account);
//        return true;
        userDAO.createUser(account.getUsername(), account.getPassword(), account.getPassword(), callback);
    }
}
