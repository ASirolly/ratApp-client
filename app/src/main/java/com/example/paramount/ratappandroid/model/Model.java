package com.example.paramount.ratappandroid.model;


import com.example.paramount.ratappandroid.dao.Callback;
import com.example.paramount.ratappandroid.dao.UserDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds application state information. Uses the singleton design pattern to allow
 * access to the model from each controller.
 *
 * Created by Greg on 9/22/17.
 */

public final class Model {
    // singleton instance
    private static final Model _instance = new Model();
    private final ArrayList<RatSighting> ratSightings;
    private final ArrayList<GraphDate> graphDates;

    // maps unique key to rat sighting
    private final Map<String, RatSighting> mapRatSightings;

    private Model() {
        // leaving in test account for now
        ratSightings = new ArrayList<>(25);
        mapRatSightings = new HashMap<>();
        graphDates = new ArrayList<>();
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
     * @return list of rat sightings
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
     * Clears out the list of dates being used for the graph
     */
    public void resetGraphDates() { graphDates.clear(); }


    /**
     * @return list of rat sightings for display on the map.
     */
    public Map<String, RatSighting> getMapRatSightings() { return mapRatSightings; }

    /**
     *
     * @return list of the dates that will be used for the graph
     */
    public List<GraphDate> getGraphDates() {
        return graphDates;
    }

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
     * Determines whether the provided username/password combination is valid.
     * @param username provided username
     * @param password provided password
     * @param callback provides onSuccess method to execute upon successful registration
     */
    public void lookUpAccount(String username, String password, Callback<String> callback) {
//        Account account = allAccounts.get(username);
//        if (account == null) { // there is no account with the given username
//            return null;
//        }
//         // account exists and correct password was provided
//        if (password.equals(account.getPassword())) {
//            return account;
//        }
//        return null; // account exists, but wrong password provided
        UserDAO.getInstance().authenticate(username, password, callback);
    }

    /**
     * Attempts to register an account.
     * @param account account to register
     * @param callback provides onSuccess method to execute upon successful registration
     */
    public void registerAccount(Account account, Callback<String> callback) {
//        if (allAccounts.containsKey(account.getUsername())) { // username already taken
//            return false;
//        }
//        allAccounts.put(account.getUsername(), account);
//        return true;
        UserDAO.getInstance().createUser(account.getUsername(), account.getPassword(),
                account.getPassword(), callback);
    }
}
