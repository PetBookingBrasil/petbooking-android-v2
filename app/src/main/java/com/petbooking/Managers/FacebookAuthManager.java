package com.petbooking.Managers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Interfaces.SocialCallback;
import com.petbooking.Models.SocialUser;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Luciano José on 18/12/2016.
 */

public class FacebookAuthManager implements com.facebook.FacebookCallback {

    private AccessToken accessToken;
    private LoginManager mLoginManager;
    private CallbackManager mFBCallback;
    private SocialCallback mReqCallback;

    private com.facebook.FacebookCallback mCallback = new com.facebook.FacebookCallback() {
        @Override
        public void onSuccess(Object o) {
            LoginResult result = (LoginResult) o;
            getUserInfo(result);
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
    public void init(SocialCallback callback) {
        mFBCallback = CallbackManager.Factory.create();
        mLoginManager = LoginManager.getInstance();
        mLoginManager.registerCallback(mFBCallback, mCallback);
        this.mReqCallback = callback;
    }

    /**
     * Autentica o usuário
     *
     * @param activity
     */
    public void auth(Activity activity) {
        mLoginManager.logInWithReadPermissions(activity, Arrays.asList(AppConstants.FACEBOOK_EMAIL_PERMISSION));
    }

    /**
     * Busca informações do usuário
     * no GraphAPI
     *
     * @param loginResult
     */
    public void getUserInfo(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        try {
                            SocialUser user = new SocialUser();

                            user.id = object.getString("id").toString();
                            user.accessToken = loginResult.getAccessToken().getToken();
                            user.email = object.getString("email").toString();
                            user.name = object.getString("name").toString();
                            user.profilePicture = String.format(Locale.getDefault(), AppConstants.USER_PICTURE_URL, object.getString("id").toString());
                            mReqCallback.onFacebookLoginSuccess(user);
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
    public void onSuccess(Object o) {
        Log.d("FB", new Gson().toJson(o));
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
