package com.example.paramount.ratappandroid;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.paramount.ratappandroid.model.Account;
import com.example.paramount.ratappandroid.model.AccountType;
import com.example.paramount.ratappandroid.model.Model;

/**
 * Registration screen with cancel and submit buttons.
 * Created by Greg on 9/21/2017
 */

public class Registration extends AppCompatActivity {

    private String username;

    /**
     * Creates the registration page and sets actions for the username, password, account status,
     * submit, and cancel elements.
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(view -> finish());

        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(view -> {
                EditText usernameEditText = (EditText) findViewById(R.id.usernameInput);
                EditText passwordEditText = (EditText) findViewById(R.id.passwordInput);
                EditText passwordConfirmationEditText = (EditText) findViewById(
                        R.id.passwordConfirmationInput);
                RadioGroup accountTypeRadioGroup = (RadioGroup) findViewById(
                        R.id.accountTypeRadioGroup);
                Checkable userRadioButton = (RadioButton) findViewById(R.id.userRadioButton);

                Editable usernameEditable = usernameEditText.getText();
                String username = usernameEditable.toString();

                Editable passwordEditable = passwordEditText.getText();
                String password = passwordEditable.toString();

                Editable passwordConfirmationEditable = passwordConfirmationEditText.getText();
                String confirmedPassword = passwordConfirmationEditable.toString();

                if (username.isEmpty()) {
                    showMessage("please enter a username");
                } else if (password.isEmpty()) {
                    showMessage("please enter a password");
                } else if (!(password.equals(confirmedPassword))) {
                    showMessage("password does not match password confirmation");
                } else if (accountTypeRadioGroup.getCheckedRadioButtonId() == -1) {
                    showMessage("no account type selected");
                } else {
                    AccountType accountType =
                            userRadioButton.isChecked() ? AccountType.USER : AccountType.ADMIN;
                    Account account = new Account(username, password, accountType);

                    Registration.this.username = username;

                    // successful registration
//                    if (Model.getInstance().registerAccount(userDAO, account)) {
//                        Log.i(TAG, String.format("registered account: %s", account));
//                        finish();
//                        showMessage(String.format(
//                          "successfully registered with username: %s", username));
//                    } else {
//                        Log.i(TAG, String.format("failed to register account: %s", account));
//                        showMessage("registration could not be completed");
//                    }
                    Model model = Model.getInstance();
                    model.registerAccount(account, Registration.this::onSuccess);
                }
        });
    }

    /**
     * method called upon successful registration
     * @param result message returned from POST request
     */
    private void onSuccess(String result) {
        finish();
        showMessage(String.format(result + "successfully registered with username: %s", username));
    }

    /**
     * Shows the message
     * @param message message to be shown
     */
    private void showMessage(CharSequence message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
