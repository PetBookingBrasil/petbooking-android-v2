package com.petbooking.Managers;

import android.content.Context;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Models.User;
import com.petbooking.Utils.CommonUtils;

import java.util.Date;

/**
 * Created by Luciano José on 18/12/2016.
 */
public class SessionManager {

    private static Gson mJsonManager;
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
        mJsonManager = new Gson();
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

    public void setSessionToken(String token) {
        mPreferenceManager.putString(AppConstants.SESSION_TOKEN, token);
    }

    public void setSessionExpirationDate(long date) {
        mPreferenceManager.putLong(AppConstants.SESSION_EXPIRATION_DATE, date);
    }

    public long getSessionExpirationDate() {
        return mPreferenceManager.getLong(AppConstants.CONSUMER_EXPIRATION_DATE);
    }

    public String getSessionToken() {
        return mPreferenceManager.getString(AppConstants.SESSION_TOKEN);
    }

    public void setUserLogged(User user) {
        mPreferenceManager.putString(AppConstants.USER_LOGGED, mJsonManager.toJson(user));
    }

    public User getUserLogged() {
        User user = mJsonManager.fromJson(mPreferenceManager.getString(AppConstants.USER_LOGGED), User.class);
        return user;
    }

    public boolean alreadyLogged() {
        return mPreferenceManager.getBoolean(AppConstants.ALREADY_LOGGED);
    }

    public void setAlreadyLogged(boolean alreadyLogged) {
        mPreferenceManager.putBoolean(AppConstants.ALREADY_LOGGED, alreadyLogged);
    }

    public boolean hasAuthTokenValid() {
        String token = getConsumerToken();
        String epoch = String.valueOf(getConsumerExpirationDate());
        Date today = new Date();
        Date expireDate = CommonUtils.getUTCDate(epoch);

        if (CommonUtils.isEmpty(token)) {
            return false;
        }

        if (today.after(expireDate)) {
            return false;
        }

        return true;
    }

    public boolean hasSessionTokenValid() {
        String token = getSessionToken();
        String epoch = String.valueOf(getSessionExpirationDate());
        Date today = new Date();
        Date expireDate = CommonUtils.getUTCDate(epoch);

        if (CommonUtils.isEmpty(token)) {
            return false;
        }

        if (today.after(expireDate)) {
            return false;
        }

        return true;
    }


    public void logout() {
        mPreferenceManager
                .removeKey(AppConstants.USER_LOGGED)
                .removeKey(AppConstants.SESSION_TOKEN);
        LoginManager.getInstance().logOut();
    }
}
