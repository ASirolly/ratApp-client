package com.example.paramount.ratappandroid.model;

/**
 * Represents information associated with an account.
 * Created by Greg on 9/22/17.
 */

public class Account {
    private String username;
    private String password;
    private AccountType accountType;

    public Account(String username, String password, AccountType accountType) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    /**
     * For easy printing of account information in log messages. Not the most secure way to do things...
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
