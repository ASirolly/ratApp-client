package com.example.paramount.ratappandroid;

import android.os.Bundle;

/**
 * The first screen that users see after they log in.
 * Created by Greg on 9/22/17.
 */

public class Dashboard extends LoggedInBaseActivity {

    /**
     * Creates the dashboard page.
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
    }
}
