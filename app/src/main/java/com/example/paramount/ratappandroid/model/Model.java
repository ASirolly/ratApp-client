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

    /**
     * Returns a model instance
     * @return _instance An instance of the model.
     */
    public static Model getInstance() { return _instance; }

    // account of the currently logged in user
    private Account account;

    /**
     * Returns the boolean status of an account
     * @return account == null Whether an account is null
     */
    public boolean isLoggedIn() { return account == null; }

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
