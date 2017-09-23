package com.example.paramount.ratappandroid.model;


/**
 * Holds application state information. Uses the singleton design pattern to allow
 * access to the model from each controller.
 *
 * Created by Greg on 9/22/17.
 */

public class Model {
    // singleton instance
    private static final Model _instance = new Model();
    public static Model getInstance() { return _instance; }

    // account of the currently logged in user
    private Account account;

    public boolean isLoggedIn() { return account == null; }
    public void logOut() { account = null; }

    public void setAccount(Account account) { this.account = account; }
    public Account getAccount() { return account; }

    /**
     * Makes a call to the backend to determine whether the provided username/password combination is valid.
     * @return the corresponding account if the combination is valid, and none otherwise
     */
    public Account lookUpAccount(String username, String password) {
        // TODO: call to backend here. And move to separate class?
        // hard-coded for M4
        if (username.equals("username") && password.equals("password")) {
            return new Account(username, password, AccountType.USER);
        } else {
            return null; // invalid username/password combination
        }
    }

    /**
     * Attempts to register an account.
     * @return true if successful and false if unsuccessful (e.g. because the provided username is already taken).
     */
    public boolean registerAccount(Account account) {
        // TODO: call to backend here. And move to separate class?
        // hard-coded (change for M5)
        return true;
    }

}
