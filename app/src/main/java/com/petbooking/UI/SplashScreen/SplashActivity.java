package com.petbooking.UI.SplashScreen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.petbooking.API.Auth.AuthService;
import com.petbooking.API.Auth.Models.AuthConsumerResp;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.AlarmReceiver;
import com.petbooking.Managers.SessionManager;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.DashboardActivity;
import com.petbooking.UI.Login.LoginActivity;
import com.petbooking.UI.Presentation.PresentationActivity;
import com.petbooking.Utils.CommonUtils;

/**
 * Created by Luciano Junior on 04,December,2016
 */
public class SplashActivity extends AppCompatActivity {

    private AuthService mAuthService;
    private SessionManager mSessionManager;

    private ImageView mIvAppLogo;
    private Animation mPulseAnimation;

    /**
     * Auth Controller
     */
    private AlarmManager mAlarmManager;
    private boolean hasValidAuthToken;
    private boolean hasValidSessionToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mPulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
        mIvAppLogo = (ImageView) findViewById(R.id.appLogo);
        mIvAppLogo.startAnimation(mPulseAnimation);

        mSessionManager = SessionManager.getInstance();
        mAuthService = new AuthService();

        redirectUser();
    }


    /**
     * Go to Login Page
     */
    public void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Redirect User based
     * on Token validation
     */
    public void redirectUser() {
        hasValidAuthToken = mSessionManager.hasAuthTokenValid();
        hasValidSessionToken = mSessionManager.hasSessionTokenValid();
        authConsumer();
    }

    /**
     * Go to Presentation Screen
     */
    public void goToPresentation() {
        Intent intent = new Intent(this, PresentationActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Go to Dashboard Screen
     */
    private void goToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Auth Consumer
     */
    public void authConsumer() {
        mAuthService.authConsumer(new APICallback() {
            @Override
            public void onSuccess(Object response) {
                AuthConsumerResp authConsumerResp = (AuthConsumerResp) response;
                mSessionManager.setConsumerToken(authConsumerResp.data.attributes.token);
                mSessionManager.setConsumerExpirationDate(authConsumerResp.data.attributes.tokenExpiresAt);
                boolean alreadyLogged = mSessionManager.alreadyLogged();
                scheduleRefreshAuth();

                if (hasValidSessionToken) {
                    scheduleRefreshAuth();
                    goToDashboard();
                } else if (alreadyLogged) {
                    goToLogin();
                } else {
                    mSessionManager.setAlreadyLogged(true);
                    goToPresentation();
                }
            }

            @Override
            public void onError(Object error) {
                authConsumer();
            }
        });
    }

    /**
     * Refresh Auth Token
     */
    public void scheduleRefreshAuth() {
        Intent mIntent;
        PendingIntent mAlarmIntent;
        long dateMillis = CommonUtils.getRefreshDate(mSessionManager.getConsumerExpirationDate());

        mAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mIntent = new Intent(this, AlarmReceiver.class);
        mIntent.putExtra(AppConstants.CONSUMER_TOKEN, true);
        mAlarmIntent = PendingIntent.getBroadcast(this, AppConstants.REFRESH_CONSUMER, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, dateMillis, mAlarmIntent);
    }
}
