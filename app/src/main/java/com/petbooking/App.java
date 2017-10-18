package com.petbooking;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.appevents.AppEventsLogger;
import com.petbooking.API.Auth.AuthService;
import com.petbooking.API.Auth.Models.AuthConsumerResp;
import com.petbooking.API.Auth.Models.SessionResp;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.AlarmReceiver;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.PreferenceManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Utils.CommonUtils;

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

        checkTokens();
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


    private void checkTokens() {
        AuthService mAuthService = new AuthService();
        SessionManager mSessionManager = SessionManager.getInstance();
        String loginType = mSessionManager.getLoginType();

        if (!mSessionManager.hasAuthTokenValid()) {
            authConsumer(mAuthService, mSessionManager, this);
        }

        if (!mSessionManager.hasSessionTokenValid()) {
            if (loginType.equals(AppConstants.FACEBOOK_TYPE)) {
                authFB(mAuthService, mSessionManager, this, mSessionManager.getLastFBToken());
            } else if (loginType.equals(AppConstants.LOGIN_TYPE)) {
                authEmail(mAuthService, mSessionManager, this);
            }
        }
    }

    /**
     * Auth Consumer
     */
    public void authConsumer(AuthService authService, final SessionManager sessionManager, final Context context) {
        authService.authConsumer(new APICallback() {
            @Override
            public void onSuccess(Object response) {
                AuthConsumerResp authConsumerResp = (AuthConsumerResp) response;
                sessionManager.setConsumerToken(authConsumerResp.data.attributes.token);
                sessionManager.setConsumerExpirationDate(authConsumerResp.data.attributes.tokenExpiresAt);
                scheduleRefresh(context, authConsumerResp.data.attributes.tokenExpiresAt, AppConstants.CONSUMER_TOKEN);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    /**
     * Login User with
     * email and password
     */
    public void authEmail(AuthService authService, final SessionManager sessionManager, final Context context) {
        final String email = sessionManager.getLastEmail();
        final String password = sessionManager.getLastPassword();

        authService.authUser(email, password, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                SessionResp sessionResp = (SessionResp) response;
                sessionManager.setSessionToken(sessionResp.data.attributes.token);
                sessionManager.setSessionExpirationDate(sessionResp.data.attributes.expiresAt);
                scheduleRefresh(context, sessionResp.data.attributes.expiresAt, AppConstants.SESSION_TOKEN);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    /**
     * Login With Facebook
     */
    public void authFB(AuthService authService, final SessionManager sessionManager, final Context context, final String providerToken) {
        authService.authUserSocial("facebook", providerToken, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                SessionResp sessionResp = (SessionResp) response;
                sessionManager.setSessionToken(sessionResp.data.attributes.token);
                sessionManager.setSessionExpirationDate(sessionResp.data.attributes.expiresAt);
                sessionManager.setLastFBToken(providerToken);
                scheduleRefresh(context, sessionResp.data.attributes.expiresAt, AppConstants.SESSION_TOKEN);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    /**
     * Refresh Token
     */
    public void scheduleRefresh(Context context, long timestamp, String type) {
        Intent mIntent;
        PendingIntent mAlarmIntent;
        long dateMillis = CommonUtils.getRefreshDate(timestamp);

        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mIntent = new Intent(context, AlarmReceiver.class);
        mIntent.putExtra(type, true);
        mAlarmIntent = PendingIntent.getBroadcast(context, 0, mIntent, 0);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, dateMillis, mAlarmIntent);
    }
}
