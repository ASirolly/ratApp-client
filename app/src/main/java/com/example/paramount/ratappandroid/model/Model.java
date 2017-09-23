package com.example.paramount.ratappandroid.model;


/**
 * Holds application state information. Uses the singleton design pattern to allow
 * access to the model from each controller.
 *
 * Created by greg on 9/22/17.
 */

public class Model {
    // singleton instance
    private static final Model _instance = new Model();
    public static Model getInstance() { return _instance; }

    // username of currently logged in user
    private String username = null;

    public boolean isLoggedIn() {
        return username == null;
    }

    public void logOut() { username = null; }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
