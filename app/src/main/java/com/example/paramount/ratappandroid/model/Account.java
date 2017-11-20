package com.example.paramount.ratappandroid.model;

/**
 * Represents information associated with an account.
 * Created by Greg on 9/22/17.
 */

public class Account {
    private final String username;
    private final String password;
    private final AccountType accountType;

    /**
     * Sets the accounts information.
     * @param username An actor's username.
     * @param password An actor's password.
     * @param accountType An actor's account type.
     */
    public Account(String username, String password, AccountType accountType) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    /**
     * Returns an account's username.
     * @return username
     */
    String getUsername() { return username; }

    /**
     * Returns an account's password
     * @return password
     */
    String getPassword() { return password; }

    /**
     * For easy printing of account information in log messages.
     * @return string
     */
    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", accountType=" + accountType +
                '}';
    }
}
