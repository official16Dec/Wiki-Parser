package com.sundroid.wikiparser.utils;

import android.app.Application;
import android.content.res.Configuration;

public class UserApp extends Application {
    public void onCreate() {
        super.onCreate();
        // MobileAds.initialize(this, getResources().getString(R.string.publisher_id));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
