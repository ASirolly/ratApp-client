package com.example.paramount.ratappandroid.model;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by andrew on 11/12/17.
 */

public class registerAccountTest {


    // set of tests that should only fail when two accounts are registered with the sane username, password and account type do not matter.
    @Test
    public void registerAccountNoAccounts() {
        Model model = Model.getInstance();
        Account account = new Account("NameUserOneA", "PassUserOneA", AccountType.USER);
        assertTrue(model.registerAccount(account));
    }

    @Test
    public void registerAccount_SameName_SamePass_SameType() {
        Model model = Model.getInstance();
        Account accountOne = new Account("NameUserOneB", "PassUserOneB", AccountType.USER);
        Account accountTwo = new Account("NameUserOneB", "PassUserOneB", AccountType.USER);
        model.registerAccount(accountOne);
        assertFalse(model.registerAccount(accountTwo));
    }

    @Test
    public void registerAccount_SameName_SamePass_DiffType() {
        Model model = Model.getInstance();
        Account accountOne = new Account("NameUserOneC", "PassUserOneC", AccountType.USER);
        Account accountTwo = new Account("NameUserOneC", "PassUserOneC", AccountType.ADMIN);
        model.registerAccount(accountOne);
        assertFalse(model.registerAccount(accountTwo));
    }

    @Test
    public void registerAccount_SameName_DiffPass_SameType() {
        Model model = Model.getInstance();
        Account accountOne = new Account("NameUserOneD", "PassUserOneD", AccountType.USER);
        Account accountTwo = new Account("NameUserOneD", "PassUserTwoD", AccountType.USER);
        model.registerAccount(accountOne);
        assertFalse(model.registerAccount(accountTwo));
    }

    @Test
    public void registerAccount_SameName_DiffPass_DiffType() {
        Model model = Model.getInstance();
        Account accountOne = new Account("NameUserOneE", "PassUserOneE", AccountType.USER);
        Account accountTwo = new Account("NameUserOneE", "PassUserTwoE", AccountType.ADMIN);
        model.registerAccount(accountOne);
        assertFalse(model.registerAccount(accountTwo));
    }

    @Test
    public void registerAccount_DiffName_SamePass_SameType() {
        Model model = Model.getInstance();
        Account accountOne = new Account("NameUserOneF", "PassUserOneF", AccountType.USER);
        Account accountTwo = new Account("NameUserTwoF", "PassUserOneF", AccountType.USER);
        model.registerAccount(accountOne);
        assertTrue(model.registerAccount(accountTwo));
    }

    @Test
    public void registerAccount_DiffName_SamePass_DiffType() {
        Model model = Model.getInstance();
        Account accountOne = new Account("NameUserOneG", "PassUserOneG", AccountType.USER);
        Account accountTwo = new Account("NameUserTwoG", "PassUserOneG", AccountType.ADMIN);
        model.registerAccount(accountOne);
        assertTrue(model.registerAccount(accountTwo));
    }

    @Test
    public void registerAccount_DiffName_DiffPass_SameType() {
        Model model = Model.getInstance();
        Account accountOne = new Account("NameUserOneH", "PassUserOneH", AccountType.USER);
        Account accountTwo = new Account("NameUserTwoH", "PassUserTwoH", AccountType.USER);
        model.registerAccount(accountOne);
        assertTrue(model.registerAccount(accountTwo));
    }

    @Test
    public void registerAccount_DiffName_DiffPass_DiffType() {
        Model model = Model.getInstance();
        Account accountOne = new Account("NameUserOneI", "PassUserOneI", AccountType.USER);
        Account accountTwo = new Account("NameUserTwoI", "PassUserTwoI", AccountType.ADMIN);
        model.registerAccount(accountOne);
        assertTrue(model.registerAccount(accountTwo));
    }

    @Test
    public void registerAccountEmpty() {
        Model model = Model.getInstance();
        Account account = new Account("", "PassUserOneA", AccountType.USER);
        assertTrue(model.registerAccount(account));
    }

    @Test(expected = NullPointerException.Class)
    public void registerAccountNull() {
        Model model = Model.getInstance();
        Account account = ObjectUtils.Null
        assertTrue(model.registerAccount(account));
    }


}