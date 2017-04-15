package com.petbooking;

import android.app.Application;
import android.hardware.camera2.params.Face;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.petbooking.Managers.SessionManager;

/**
 * Created by Luciano José on 13/04/2017.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        initManagers();
        initDependencies();
    }


    public void initManagers() {
        SessionManager.initialize(this);
    }

    private void initDependencies() {
        AppEventsLogger.activateApp(this);
    }
}
