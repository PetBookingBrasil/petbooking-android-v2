package com.petbooking.Managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.petbooking.Constants.AppConstants;

/**
 * Created by Luciano José on 18/12/2016.
 */

public class PreferenceManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, AppConstants.PREF_PRIVATE_MODE);
        editor = pref.edit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public void putLong(String key, Long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public void putFloat(String key, Float value) {
        editor.putFloat(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }


    public boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }


    public int getInt(String key) {
        return pref.getInt(key, 0);
    }

    public long getLong(String key) {
        return pref.getLong(key, 0);
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }

    public String getString(String key) {
        return pref.getString(key, "");
    }


}
