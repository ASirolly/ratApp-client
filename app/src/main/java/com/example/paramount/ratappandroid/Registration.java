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

import com.android.volley.VolleyError;
import com.example.paramount.ratappandroid.model.Account;
import com.example.paramount.ratappandroid.model.AccountType;
import com.example.paramount.ratappandroid.model.Model;

/**
 * Registration screen with cancel and submit buttons.
 * Created by Greg on 9/21/2017
 */

public class Registration extends AppCompatActivity {

    private String username;
    private Model model;
    private Button submit;

    /**
     * Creates the registration page and sets actions for the username, password, account status,
     * submit, and cancel elements.
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        Button cancel = findViewById(R.id.cancelButton);
        cancel.setOnClickListener(view -> finish());

        submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(view -> {
                submit.setEnabled(false);
                EditText usernameEditText = findViewById(R.id.usernameInput);
                EditText passwordEditText = findViewById(R.id.passwordInput);
                EditText passwordConfirmationEditText = findViewById(
                        R.id.passwordConfirmationInput);
                RadioGroup accountTypeRadioGroup = findViewById(
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

                    model = Model.getInstance(getApplicationContext());
                    model.registerAccount(
                            account,
                            Registration.this::onSuccess,
                            Registration.this::onFailure);
                }
        });
    }

    /**
     * method called upon successful registration
     * @param result message returned from POST request
     */
    private void onSuccess(String result) {
        submit.setEnabled(true);
        finish();
        showMessage(String.format("successfully registered with username: %s", username));
    }

    /**
     * Method called upon unsuccessful registration
     * @param volleyError Returned error
     */
    private void onFailure(VolleyError volleyError) {
        submit.setEnabled(true);
        showMessage("The provided email is already in use.");
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
