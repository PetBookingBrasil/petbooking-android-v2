package com.petbooking;

import android.app.Application;

import com.petbooking.Managers.SessionManager;

/**
 * Created by Luciano José on 13/04/2017.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        initManagers();
    }

    public void initManagers() {
        SessionManager.initialize(this);
    }
}
