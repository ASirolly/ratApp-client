package com.example.paramount.ratappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.paramount.ratappandroid.model.Account;
import com.example.paramount.ratappandroid.model.AccountType;
import com.example.paramount.ratappandroid.model.Model;

/**
 * Login screen.
 * Created by Joshua Reno on 9/21/17.
 */

public class Login extends AppCompatActivity {

    private static final String TAG = "Login"; // used in log messages

    private Account account;

    /**
     * Creates login page and sets actions for the username, password, submit and cancel elements.
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Log.d("msg", account.toString());

        Button cancel = (Button) findViewById(R.id.cancelButtonLogin);
        cancel.setOnClickListener(view -> finish());

        Button submit = (Button) findViewById(R.id.submitButtonLogin);
        submit.setOnClickListener(view -> {
                EditText usernameEditText = (EditText) findViewById(R.id.usernameEditTextLogin);
                EditText passwordEditText = (EditText) findViewById(R.id.passwordEditTextLogin);

                Editable usernameEditable = usernameEditText.getText();
                String username = usernameEditable.toString();

                Editable passwordEditable = passwordEditText.getText();
                String password = passwordEditable.toString();

                Login.this.account = new Account(username, password, AccountType.ADMIN);

                Log.i(TAG, String.format("attempting login with username: %s, password: %s",
                        username, password));
                Model model = Model.getInstance();
                model.lookUpAccount(username, password, Login.this::onSuccess);
//                if (account != null) {
////                    Log.i(TAG, "successful login attempt");
////                    // set username in model
////                    Model.getInstance().setAccount(account);
////                    // open dashboard
////                    Intent intent = new Intent(getBaseContext(), Dashboard.class);
////                    startActivity(intent);
//                } else {
//                    Log.i(TAG, "unsuccessful login attempt");
//                    // show popup message indicating that username and password don't match
//                    // (this) https://developer.android.com/guide/topics/ui/notifiers/toasts.html
//                    Context context = getApplicationContext();
//                    CharSequence text = "incorrect username or password";
//                    int duration = Toast.LENGTH_LONG;
//
//                    Toast toast = Toast.makeText(context, text, duration);
//                    toast.show();
//
//                    // clear the entered username/password
//                    usernameEditText.setText("");
//                    passwordEditText.setText("");
//                    usernameEditText.requestFocus(); // focus on username EditText
//                }

        });
    }

    /**
     * method called upon successful login
     * @param result message returned from POST request
     */
    private void onSuccess(String result) {
        Log.i(TAG, "successful login attempt" + result);
        // set username in model
        // open dashboard
        Intent intent = new Intent(getBaseContext(), Dashboard.class);
        startActivity(intent);
    }
}
