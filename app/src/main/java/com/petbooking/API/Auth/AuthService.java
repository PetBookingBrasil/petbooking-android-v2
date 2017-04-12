package com.petbooking.API.Auth;

import android.content.Context;

import com.petbooking.API.APIClient;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.ConsumerResp;
import com.petbooking.Models.ConsumerRqt;

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
        ConsumerRqt consumerRqt = new ConsumerRqt();
        Call<ConsumerResp> call = mAuthInterface.authConsumer(consumerRqt);
        call.enqueue(new Callback<ConsumerResp>() {
            @Override
            public void onResponse(Call<ConsumerResp> call, Response<ConsumerResp> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ConsumerResp> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
