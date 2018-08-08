package com.petbooking;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.GoogleApiAvailability;
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
import com.salesforce.marketingcloud.InitializationStatus;
import com.salesforce.marketingcloud.MarketingCloudConfig;
import com.salesforce.marketingcloud.MarketingCloudSdk;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Luciano José on 13/04/2017.
 */

public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initManagers();
        initDependencies();
        initSalesForceConfiguration();

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

    public void initSalesForceConfiguration() {
        MarketingCloudSdk.init(this, MarketingCloudConfig.builder()
                .setApplicationId("6cbf4f6b-cf41-42d4-b105-8880e52d81d7")
                .setAccessToken("c2bpggs2bsxx8bathf9uw8f4")
                .setGcmSenderId("599738167963")
                .setNotificationSmallIconResId(R.drawable.ic_logo_pet_booking)
                .setNotificationChannelName("petbooking-android")
                .build(), new MarketingCloudSdk.InitializationListener() {
            @Override
            public void complete(InitializationStatus status) {
                if (status.isUsable()) {
                    if (status.status() == InitializationStatus.Status.COMPLETED_WITH_DEGRADED_FUNCTIONALITY) {
                        // While the SDK is usable, something happened during init that you should address.
                        // This could include:
                        if (GoogleApiAvailability.getInstance().isUserResolvableError(status.locationPlayServicesStatus())) {
                            //Google play services encountered a recoverable error
                            GoogleApiAvailability.getInstance().showErrorNotification(App.this, status.locationPlayServicesStatus());
                        } else if (status.messagingPermissionError()) {
              /* The user had previously provided the location permission, but it has now been revoked.
                 Geofence and Beacon messages have been disabled.  You will need to request the location
                 permission again and re-enable Geofence and/or Beacon messaging again. */
                        }
                    }
                } else {
                    //Something went wrong with init that makes the SDK unusable.
                }
            }
        });// Required if Android target API is >= 26
        //Enable any other feature desired.);
    }
}
