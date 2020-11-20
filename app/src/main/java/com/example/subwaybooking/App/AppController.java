package com.example.subwaybooking.App;

import android.app.Application;
import android.util.Log;

public class AppController extends Application {

    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }
}
