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
     * Sets an account's username
     * @param username
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Returns an account's password
     * @return password
     */
    public String getPassword() { return password; }

    /**
     * Sets an account's password.
     * @param password
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Returns an account's type.
     * @return accountType
     */
    public AccountType getAccountType() { return accountType; }

    /**
     * Sets an account's type.
     * @param accountType
     */
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

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
