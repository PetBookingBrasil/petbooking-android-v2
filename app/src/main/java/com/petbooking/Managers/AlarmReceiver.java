package com.petbooking.Managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.petbooking.API.Auth.AuthService;
import com.petbooking.API.Auth.Models.AuthConsumerResp;
import com.petbooking.API.Auth.Models.SessionResp;
import com.petbooking.API.Generic.APIError;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Events.ShowSnackbarEvt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.Utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

/**
 * Created by Luciano José on 28/04/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private SessionManager mSessionManager;
    private AuthService mAuthService;
    private AlarmManager mAlarmManager;

    private boolean isRefreshAuth;
    private boolean isRefreshSession;
    private String loginType;

    @Override
    public void onReceive(Context context, Intent intent) {
        mAuthService = new AuthService();
        mSessionManager = SessionManager.getInstance();
        isRefreshAuth = intent.getBooleanExtra(AppConstants.CONSUMER_TOKEN, false);
        isRefreshSession = intent.getBooleanExtra(AppConstants.SESSION_TOKEN, false);
        loginType = mSessionManager.getLoginType();

        if (isRefreshAuth) {
            authConsumer(context);
        }

        if (isRefreshSession) {
            if (loginType.equals(AppConstants.FACEBOOK_TYPE)) {
                authFB(context, mSessionManager.getLastFBToken());
            } else if (loginType.equals(AppConstants.LOGIN_TYPE)) {
                authEmail(context);
            }
        }
    }

    /**
     * Refresh Token
     */
    public void scheduleRefresh(Context context, long timestamp, String type) {
        Intent mIntent;
        PendingIntent mAlarmIntent;
        long dateMillis = CommonUtils.getRefreshDate(timestamp);

        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mIntent = new Intent(context, AlarmReceiver.class);
        mIntent.putExtra(type, true);
        mAlarmIntent = PendingIntent.getBroadcast(context, 0, mIntent, 0);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, dateMillis, mAlarmIntent);
    }

    /**
     * Auth Consumer
     */
    public void authConsumer(final Context context) {
        mAuthService.authConsumer(new APICallback() {
            @Override
            public void onSuccess(Object response) {
                AuthConsumerResp authConsumerResp = (AuthConsumerResp) response;
                mSessionManager.setConsumerToken(authConsumerResp.data.attributes.token);
                mSessionManager.setConsumerExpirationDate(authConsumerResp.data.attributes.tokenExpiresAt);
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
    public void authEmail(final Context context) {
        final String email = mSessionManager.getLastEmail();
        final String password = mSessionManager.getLastPassword();

        mAuthService.authUser(email, password, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                SessionResp sessionResp = (SessionResp) response;
                mSessionManager.setSessionToken(sessionResp.data.attributes.token);
                mSessionManager.setSessionExpirationDate(sessionResp.data.attributes.expiresAt);
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
    public void authFB(final Context context, final String providerToken) {
        mAuthService.authUserSocial("facebook", providerToken, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                SessionResp sessionResp = (SessionResp) response;
                mSessionManager.setSessionToken(sessionResp.data.attributes.token);
                mSessionManager.setSessionExpirationDate(sessionResp.data.attributes.expiresAt);
                mSessionManager.setLastFBToken(providerToken);
                scheduleRefresh(context, sessionResp.data.attributes.expiresAt, AppConstants.SESSION_TOKEN);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

}
