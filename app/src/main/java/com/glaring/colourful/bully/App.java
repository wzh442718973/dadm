package com.glaring.colourful.bully;

import android.app.Application;
import android.content.Context;

import com.glaring.colourful.bully.games.lib.AAAHelper;

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AAAHelper._attachBaseContext(base, this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AAAHelper._onCreate(this);
    }
}
