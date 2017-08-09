package com.petbooking;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.appevents.AppEventsLogger;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.PreferenceManager;
import com.petbooking.Managers.SessionManager;

import io.fabric.sdk.android.Fabric;

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
        PreferenceManager.getInstance().initialize(this);
        SessionManager.initialize(this);
        AppointmentManager.getInstance().initialize(this);
        LocationManager.getInstance().initialize(this);
    }

    private void initDependencies() {
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this, crashlyticsKit);
        AppEventsLogger.activateApp(this);
    }
}
