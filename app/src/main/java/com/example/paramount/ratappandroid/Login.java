package com.example.paramount.ratappandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.paramount.ratappandroid.model.Model;

/**
 * Login screen.
 * Created by Joshua Reno on 9/21/17.
 */

public class Login extends AppCompatActivity {

    private static final String TAG = "Login"; // used in log messages

    private Model model;
    private Button submit;
    Editable usernameEditable;

    /**
     * Creates login page and sets actions for the username, password, submit and cancel elements.
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        model = Model.getInstance(getApplicationContext());

        Button cancel = findViewById(R.id.cancelButtonLogin);
        cancel.setOnClickListener(view -> finish());

        submit = findViewById(R.id.submitButtonLogin);
        if (model.lockedOut) {
            submit.setEnabled(false); // so that user can't go back then reopen login screen
            // in order to reenable submit button
        }
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            model.lockedOut = true;
            EditText usernameEditText = findViewById(R.id.usernameEditTextLogin);
            EditText passwordEditText = findViewById(R.id.passwordEditTextLogin);

            usernameEditable = usernameEditText.getText();
            String username = usernameEditable.toString();

            Editable passwordEditable = passwordEditText.getText();
            String password = passwordEditable.toString();

            Log.i(TAG, String.format("attempting login with username: %s", username));
                model.lookUpAccount(username, password, Login.this::onSuccess, Login.this::onFailure);
        });
    }

    /**
     * method called upon successful login
     * @param result message returned from POST request
     */
    private void onSuccess(String result) {
        submit.setEnabled(true);
        model.lockedOut = false;
        Log.i(TAG, "successful login attempt" + result);
        Intent intent = new Intent(getBaseContext(), Dashboard.class);
        startActivity(intent);
    }

    /**
     * Method called upon unsuccessful login.
     * @param volleyError Returned error
     */
    private void onFailure(VolleyError volleyError) {
        String errorMessage = new String(volleyError.networkResponse.data);
        Boolean bruteForceDetected = errorMessage.toLowerCase().contains("brute");
        if (bruteForceDetected) {
            showMessage("Please try again in 30 seconds");

            Log.w(TAG, String.format(
                    "brute forcing detected for username %s",
                    usernameEditable.toString()));

            new Handler().postDelayed(
                    () -> {
                        submit.setEnabled(true);
                        model.lockedOut = false;
                    },
                    // Wait 5 minutes (since the lockout on the backend lasts for this long),
                    // then reenable "submit" button.
                    1000*60*5);
        } else {
            showMessage("Incorrect username or password");
            submit.setEnabled(true);
            model.lockedOut = false;
        }

    }

    /**
     * Shows the message
     * @param message The message to be shown
     */
    private void showMessage(CharSequence message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
