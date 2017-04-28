package com.petbooking.UI.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.petbooking.API.Auth.AuthService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.API.Auth.Models.AuthConsumerResp;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.DashboardActivity;
import com.petbooking.UI.Login.LoginActivity;
import com.petbooking.UI.Presentation.PresentationActivity;

/**
 * Created by Luciano Junior on 04,December,2016
 */
public class SplashActivity extends AppCompatActivity {

    private AuthService mAuthService;
    private SessionManager mSessionManager;

    private ImageView mIvAppLogo;
    private Animation mPulseAnimation;

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

        if (!hasValidAuthToken) {
            authConsumer();
        } else if (hasValidAuthToken && !hasValidSessionToken) {
            goToLogin();
        } else if (hasValidAuthToken && hasValidSessionToken) {
            goToDashboard();
        }
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

                if (alreadyLogged) {
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
}
