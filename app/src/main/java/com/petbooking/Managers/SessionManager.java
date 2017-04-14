package com.petbooking.Managers;

import android.content.Context;

import com.petbooking.Constants.AppConstants;

/**
 * Created by Luciano José on 18/12/2016.
 */
public class SessionManager {

    private static SessionManager mInstance;
    private static PreferenceManager mPreferenceManager;

    public static SessionManager getInstance() {
        if (mInstance == null) {
            mInstance = new SessionManager();
        }

        return mInstance;
    }

    public static void initialize(Context context) {
        mPreferenceManager = new PreferenceManager(context);
    }

    public void setConsumerToken(String consumer) {
        mPreferenceManager.putString(AppConstants.CONSUMER_TOKEN, consumer);
    }

    public String getConsumerToken() {
        return mPreferenceManager.getString(AppConstants.CONSUMER_TOKEN);
    }

    public void setConsumerExpirationDate(long date) {
        mPreferenceManager.putLong(AppConstants.CONSUMER_EXPIRATION_DATE, date);
    }

    public long getConsumerExpirationDate() {
        return mPreferenceManager.getLong(AppConstants.CONSUMER_EXPIRATION_DATE);
    }

}
