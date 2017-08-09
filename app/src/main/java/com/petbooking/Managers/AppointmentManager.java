package com.petbooking.Managers;

import android.content.Context;

import com.google.gson.Gson;

/**
 * Created by Luciano José on 08/08/2017.
 */

public class AppointmentManager {

    private static Gson mJsonManager;
    private static AppointmentManager mInstance;
    private static PreferenceManager mPreferenceManager;

    public static AppointmentManager getInstance() {
        if (mInstance == null) {
            mInstance = new AppointmentManager();
        }

        return mInstance;
    }

    public static void initialize(Context context) {
        mPreferenceManager = PreferenceManager.getInstance();
        mJsonManager = new Gson();
    }

    public void setCurrentBusinessId(String businessId) {
        mPreferenceManager.putString("businessId", businessId);
    }

    public void setCurrentBusinessName(String businessName) {
        mPreferenceManager.putString("businessName", businessName);
    }

    public void setCurrentBusinessDistance(float businessDistance) {
        mPreferenceManager.putFloat("businessDistance", businessDistance);
    }

    public String getCurrentBusinessId() {
        return mPreferenceManager.getString("businessId");
    }

    public String getCurrentBusinessName() {
        return mPreferenceManager.getString("businessName");
    }

    public float getCurrentBusinessDistance() {
        return mPreferenceManager.getFloat("businessDistance");
    }

    public void removeKey(String key) {
        mPreferenceManager.removeKey(key);
    }
}
