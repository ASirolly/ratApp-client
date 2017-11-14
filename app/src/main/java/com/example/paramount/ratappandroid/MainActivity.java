package com.example.paramount.ratappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Joshua Reno on 9/21/17.
 *
 * First activity that is shown when the app is started.
 */

public class MainActivity extends AppCompatActivity {

    /**
     * Creates the welcome page and sets actions for the login and registration buttons.
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login = (Button) findViewById(R.id.button_login);
        login.setOnClickListener(view -> {
                Intent intent = new Intent(getBaseContext(), Login.class);
                startActivity(intent);
        });

        TextView registration = (TextView) findViewById(R.id.text_registration);
        registration.setOnClickListener(view -> {
                Intent intent = new Intent(getBaseContext(), Registration.class);
                startActivity(intent);
        });
    }
}
