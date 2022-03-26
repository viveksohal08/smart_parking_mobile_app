package com.example.vivek.parking;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.firebase.client.Firebase;

public class FireApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        MultiDex.install(this);
    }
}
