package com.example.apitransactions.utils;

import android.app.Application;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefManager.init(this);
    }
}
