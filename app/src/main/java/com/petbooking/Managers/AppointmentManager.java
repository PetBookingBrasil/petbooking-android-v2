package com.petbooking.Managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.petbooking.Constants.AppConstants;

/**
 * Created by Luciano José on 08/08/2017.
 */

public class AppointmentManager {

    private static Gson mJsonManager;
    private static AppointmentManager mInstance;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public static AppointmentManager getInstance() {
        if (mInstance == null) {
            mInstance = new AppointmentManager();
        }

        return mInstance;
    }

    public void initialize(Context context) {
        pref = context.getSharedPreferences(AppConstants.PREF_APPOINTMENT_NAME, AppConstants.PREF_PRIVATE_MODE);
        editor = pref.edit();
        mJsonManager = new Gson();
    }

    public void setCurrentBusinessId(String businessId) {
        editor.putString("businessId", businessId);
        editor.apply();
    }

    public void setCurrentBusinessName(String businessName) {
        editor.putString("businessName", businessName);
        editor.apply();
    }

    public void setCurrentBusinessDistance(float businessDistance) {
        editor.putFloat("businessDistance", businessDistance);
        editor.apply();
    }

    public String getCurrentBusinessId() {
        return pref.getString("businessId", "");
    }

    public String getCurrentBusinessName() {
        return pref.getString("businessName", "");
    }

    public float getCurrentBusinessDistance() {
        return pref.getFloat("businessDistance", 0);
    }

    public void incrementPetAppointment(String petId) {
        int totalAppointments = pref.getInt(petId + "_SERVICE", 0);
        totalAppointments++;
        editor.putInt(petId + "_SERVICE", totalAppointments);
        editor.apply();
    }

    public void incrementCategoryAppointment(String categoryId, String petId) {
        int totalAppointments = pref.getInt(categoryId + "_" + petId + "_TOTAL", 0);
        totalAppointments++;
        editor.putInt(categoryId + "_" + petId + "_TOTAL", totalAppointments);
        editor.apply();
    }

    public int getTotalAppointments(String petId) {
        return pref.getInt(petId + "_SERVICE", 0);
    }

    public int getTotalCategoryAppointments(String categoryId, String petId) {
        return pref.getInt(categoryId + "_" + petId + "_TOTAL", 0);
    }

    public AppointmentManager removeKey(String key) {
        pref.edit().remove(key).apply();
        return this;
    }

    public void reset() {
        editor.clear();
        editor.commit();
    }
}
