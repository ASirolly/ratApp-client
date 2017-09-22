package com.example.paramount.ratappandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * The first screen that users see after they log in.
 * Created by Greg on 9/22/17.
 */

public class Dashboard extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
    }
}
