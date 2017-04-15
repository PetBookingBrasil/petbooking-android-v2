package com.petbooking.API.Auth;

import android.util.Log;

import com.google.gson.Gson;
import com.petbooking.API.APIClient;
import com.petbooking.API.Auth.Models.AuthConsumerResp;
import com.petbooking.API.Auth.Models.AuthConsumerRqt;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.Auth.Models.AuthUserRqt;
import com.petbooking.API.Auth.Models.SessionResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Utils.APIUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Luciano José on 09/04/2017.
 */

public class AuthService {

    private final AuthInterface mAuthInterface;

    public AuthService() {
        mAuthInterface = APIClient.getClient().create(AuthInterface.class);
    }

    public void authConsumer(final APICallback callback) {
        AuthConsumerRqt authConsumerRqt = new AuthConsumerRqt();
        Call<AuthConsumerResp> call = mAuthInterface.authConsumer(authConsumerRqt);
        call.enqueue(new Callback<AuthConsumerResp>() {
            @Override
            public void onResponse(Call<AuthConsumerResp> call, Response<AuthConsumerResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<AuthConsumerResp> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void authUser(String email, String password, final APICallback callback) {
        AuthUserRqt authUserRqt = new AuthUserRqt(email, password);
        Call<SessionResp> call = mAuthInterface.authUser(authUserRqt);
        call.enqueue(new Callback<SessionResp>() {
            @Override
            public void onResponse(Call<SessionResp> call, Response<SessionResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<SessionResp> call, Throwable t) {
                callback.onError(call);
            }
        });
    }

    public void getUser(String userId, final APICallback callback) {
        Call<AuthUserResp> call = mAuthInterface.getUser(userId);
        call.enqueue(new Callback<AuthUserResp>() {
            @Override
            public void onResponse(Call<AuthUserResp> call, Response<AuthUserResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<AuthUserResp> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
