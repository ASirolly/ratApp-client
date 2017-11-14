package com.example.paramount.ratappandroid.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by greg on 11/14/17.
 *
 * Tests for Model class.
 */

public class ModelTest {

    /**
     * Registers an account, then attempts to register an account with the same username
     * but different password and AccountType.
     *
     * The first registration should succeed (and return true), but the second registration
     * should fail (and return false) since an account with the provided username will
     * already exist.
     */
    @Test
    public void testRegisterAccountWithDuplicateAccount() {
        Model model = Model.getInstance();
        Account account = new Account("username", "password", AccountType.USER);
        // succeeds the first time since the account has not been registered yet
        assertTrue(model.registerAccount(account));

        account.setPassword("newPassword");
        account.setAccountType(AccountType.ADMIN);
        // fails the second time because an account with this username has already been registered
        assertFalse(model.registerAccount(account));
    }
}
