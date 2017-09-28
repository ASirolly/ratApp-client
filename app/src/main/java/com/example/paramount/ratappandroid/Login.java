package com.example.paramount.ratappandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paramount.ratappandroid.model.Account;
import com.example.paramount.ratappandroid.model.Model;

/**
 * Login screen.
 * Created by joshuareno on 9/21/17.
 */

public class Login extends AppCompatActivity {

    private final static String TAG = "Login"; // used in log messages
    private Button submit;
    private Button cancel;

    /**
     * Creates the login page and sets actions for the username, password, submit and cancel elements.
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        cancel = (Button) findViewById(R.id.cancelButtonLogin);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit = (Button) findViewById(R.id.submitButtonLogin);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameEditText = (EditText) findViewById(R.id.usernameEditTextLogin);
                EditText passwordEditText = (EditText) findViewById(R.id.passwordEditTextLogin);

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                Log.i(TAG, String.format("attempting login with username: %s, password: %s", username, password));
                Account account = Model.getInstance().lookUpAccount(username, password); // returns null if the provided username/password combination is invalid
                if (account != null) {
                    Log.i(TAG, "successful login attempt");
                    // set username in model
                    Model.getInstance().setAccount(account);
                    // open dashboard
                    Intent intent = new Intent(getBaseContext(), Dashboard.class);
                    startActivity(intent);
                } else {
                    Log.i(TAG, "unsuccessful login attempt");
                    // show popup message indicating that username and password don't match
                    // (this) https://developer.android.com/guide/topics/ui/notifiers/toasts.html
                    Context context = getApplicationContext();
                    CharSequence text = "incorrect username or password";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    // clear the entered username/password
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    usernameEditText.requestFocus(); // focus on username EditText
                }
            }
        });
    }
}
