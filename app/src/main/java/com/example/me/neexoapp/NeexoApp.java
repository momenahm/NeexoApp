package com.example.me.neexoapp;

import com.firebase.client.Firebase;

import android.app.Application;

/**
 * Created by m.elshaeir on 1/29/2018.
 */

public class NeexoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
