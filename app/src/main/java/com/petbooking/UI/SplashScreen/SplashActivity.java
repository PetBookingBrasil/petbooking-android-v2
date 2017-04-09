package com.petbooking.UI.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.petbooking.MainActivity;
import com.petbooking.UI.Login.LoginActivity;

/**
 * Created by Luciano Junior on 04,December,2016
 */
public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Register the consumer in the API
         * and store the token in the APP.
         */
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
