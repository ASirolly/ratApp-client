package com.example.paramount.ratappandroid.model;


import com.example.paramount.ratappandroid.dao.Callback;
import com.example.paramount.ratappandroid.dao.RatSightingDAO;
import com.example.paramount.ratappandroid.dao.UserDAO;

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
    private final UserDAO userDAO;

    // maps unique key to rat sighting

    private Model() {
        userDAO = UserDAO.getInstance();
    }

    /**
     * Returns a model instance.
     * @return _instance An instance of the model.
     */
    public static Model getInstance() { return _instance; }

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
        userDAO.authenticate(username, password, callback);
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
        userDAO.createUser(account.getUsername(), account.getPassword(),
                account.getPassword(), callback);
    }

}
