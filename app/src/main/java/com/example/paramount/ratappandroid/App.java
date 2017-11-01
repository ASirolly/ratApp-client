package com.example.paramount.ratappandroid;

import android.app.Application;
import android.content.Context;

/**
 * Created by greg on 11/1/17.
 *
 * App class that provides global access to application access
 */

public class App extends Application {

    private static Application application;

    public static Context getContext() {
        return application.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
