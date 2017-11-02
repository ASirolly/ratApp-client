package com.example.paramount.ratappandroid.model;

/**
 * Represents information associated with an account.
 * Created by Greg on 9/22/17.
 */

public class Account {
    private String username;
    private String password;
    private AccountType accountType;

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
    public String getUsername() { return username; }

    /**
     * Returns an account's password
     * @return password
     */
    public String getPassword() { return password; }

    /**
     * Returns an account's type.
     * @return accountType
     */
    public AccountType getAccountType() { return accountType; }

    /**
     * For easy printing of account information in log messages. Not the most secure way to do things...
     * @return string
     */
    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountType=" + accountType +
                '}';
    }
}
