package com.petbooking.Managers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.petbooking.Interfaces.RequestCallback;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Luciano José on 18/12/2016.
 */

public class FacebookAuthManager implements FacebookCallback<LoginResult> {

    private AccessToken accessToken;
    private LoginManager mLoginManager;
    private CallbackManager mFBCallback;
    private RequestCallback mReqCallback;

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            getUserInfo(loginResult);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    /**
     * Configura o autenticador do facebook
     *
     * @param callback
     */
    public void init(RequestCallback callback) {
        mFBCallback = CallbackManager.Factory.create();
        mLoginManager = LoginManager.getInstance();
        mLoginManager.registerCallback(mFBCallback, mCallback);
        this.mReqCallback = callback;
    }

    /**
     * Autentica o usuário
     * @param activity
     */
    public void auth(Activity activity) {
        mLoginManager.logInWithReadPermissions(activity, Arrays.asList(LoginConsts.FACEBOOK_EMAIL_PERMISSION));
    }

    /**
     * Busca informações do usuário
     * no GraphAPI
     *
     * @param loginResult
     */
    public void getUserInfo(LoginResult loginResult) {

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        try {
                            User user = new User();

                            user.id = object.getString("id").toString();
                            user.email = object.getString("email").toString();
                            user.name = object.getString("name").toString();
                            user.profilePicture = String.format(Locale.getDefault(), LoginConsts.USER_PICTURE_URL, object.getString("id").toString());
                            mReqCallback.onLoginSuccess(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        accessToken = loginResult.getAccessToken();
        getUserInfo(loginResult);

        if (!accessToken.isExpired()) {
            Log.d("FACEBOOK_LOGIN", "ACCESS_TOKEN_EXPIRED");
            return;
        }

        AccessToken.refreshCurrentAccessTokenAsync(new AccessToken.AccessTokenRefreshCallback() {
            @SuppressWarnings("PMD.MethodNamingConventions")
            @Override
            public void OnTokenRefreshed(AccessToken accessToken) {
                Log.d("FACEBOOK_LOGIN", "TOKEN_REFRESHED");
            }

            @SuppressWarnings("PMD.MethodNamingConventions")
            @Override
            public void OnTokenRefreshFailed(FacebookException exception) {
                Log.d("FACEBOOK_LOGIN", "TOKEN_REFRESHED_FAIL");
            }
        });
    }

    @Override
    public void onCancel() {
        Log.d("FACEBOOK_LOGIN", "LOGIN_CANCELLED");
    }

    @Override
    public void onError(FacebookException error) {
        Log.d("FACEBOOK_LOGIN", "ERROR_ON_LOGIN");
    }

    public CallbackManager getCallback() {
        return mFBCallback;
    }
}
