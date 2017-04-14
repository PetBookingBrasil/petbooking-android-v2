package com.petbooking.UI.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.petbooking.API.APIClient;
import com.petbooking.API.Auth.AuthInterface;
import com.petbooking.API.Auth.AuthService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.MainActivity;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.ConsumerResp;
import com.petbooking.Models.ConsumerRqt;
import com.petbooking.UI.Login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                ConsumerResp consumerResp = (ConsumerResp) response;
                mSessionManager.setConsumerToken(consumerResp.data.attributes.token);
                mSessionManager.setConsumerExpirationDate(consumerResp.data.attributes.tokenExpiresAt);
                goToLogin();
            }

            @Override
            public void onError(String message) {

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
}
