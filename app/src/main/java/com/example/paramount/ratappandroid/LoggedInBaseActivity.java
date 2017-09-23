package com.example.paramount.ratappandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.paramount.ratappandroid.model.Model;

/**
 * Sets on-click for logout button.
 * Created by Greg on 9/22/17.
 */

public abstract class LoggedInBaseActivity extends AppCompatActivity {

    public void logOutAndReturnToMainScreen(View view) {
        Model.getInstance().logOut();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        navigateUpTo(intent);
    }
}
