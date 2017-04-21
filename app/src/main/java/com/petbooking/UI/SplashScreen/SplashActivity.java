package com.petbooking.UI.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.petbooking.API.Auth.AuthService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.API.Auth.Models.AuthConsumerResp;
import com.petbooking.UI.Login.LoginActivity;
import com.petbooking.UI.Presentation.PresentationActivity;

/**
 * Created by Luciano Junior on 04,December,2016
 */
public class SplashActivity extends AppCompatActivity {

    private AuthService mAuthService;
    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSessionManager = SessionManager.getInstance();
        mAuthService = new AuthService();
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

            }
        });


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
     * Go to Presentation Screen
     */
    public void goToPresentation() {
        Intent intent = new Intent(this, PresentationActivity.class);
        startActivity(intent);
        finish();
    }
}
