package com.example.paramount.ratappandroid.model;


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

    private Model() {
        allAccounts = new HashMap<>();
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
     * Maps username to corresponding account, for all existing accounts.
     * Temporary (for M5).
     */
    private Map<String, Account> allAccounts;

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
        Account account = allAccounts.get(username);
        if (account == null) { // there is no account with the given username
            return null;
        }
        if (password.equals(account.getPassword())) { // account exists and correct password was provided
            return account;
        }
        return null; // account exists, but wrong password provided
    }

    /**
     * Attempts to register an account.
     * @return true if successful and false if unsuccessful (e.g. because the provided username is already taken).
     */
    public boolean registerAccount(Account account) {
        // TODO: call to backend here. And move to separate class?
        if (allAccounts.containsKey(account.getUsername())) { // username already taken
            return false;
        }
        allAccounts.put(account.getUsername(), account);
        return true;
    }

}
