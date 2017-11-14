package com.example.paramount.ratappandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Sets on-click for logout button.
 * Created by Greg on 9/22/17.
 */

public class LoggedInBaseActivity extends AppCompatActivity {

    /**
     * Sets the action for logging out of the dashboard screen.
     * @param view building block for user interface components
     */
    public void logOutAndReturnToMainScreen(View view) {
        Log.d("msg", view.toString());
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        navigateUpTo(intent);
    }
}
