package com.petbooking.Managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.petbooking.API.Auth.AuthService;
import com.petbooking.API.Auth.Models.AuthConsumerResp;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Utils.CommonUtils;

/**
 * Created by Luciano José on 28/04/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private SessionManager mSessionManager;
    private AuthService mAuthService;
    private AlarmManager mAlarmManager;

    private boolean isRefreshAuth;
    private boolean isRefreshSession;

    @Override
    public void onReceive(Context context, Intent intent) {
        mAuthService = new AuthService();
        isRefreshAuth = intent.getBooleanExtra(AppConstants.CONSUMER_TOKEN, false);
        isRefreshSession = intent.getBooleanExtra(AppConstants.SESSION_TOKEN, false);

        if (isRefreshAuth) {
            authConsumer(context);
        }

        if (isRefreshSession) {
            //NEW SESSION TOKEN
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
}
