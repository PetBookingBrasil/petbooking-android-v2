package com.petbooking.API.User;

import com.petbooking.API.APIClient;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.User.Models.RecoverPasswordRqt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Utils.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Luciano José on 16/04/2017.
 */

public class UserService {

    private final UserInterface mUserInterface;

    public UserService() {
        mUserInterface = APIClient.getClient().create(UserInterface.class);
    }

    public void recoverPassword(String email, final APICallback callback) {
        RecoverPasswordRqt recoverPasswordRqt = new RecoverPasswordRqt(email);
        Call<Void> call = mUserInterface.requestPassword(recoverPasswordRqt);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
               callback.onSuccess(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }


    public void getUser(String userId, final APICallback callback) {
        Call<AuthUserResp> call = mUserInterface.getUser(userId);
        call.enqueue(new Callback<AuthUserResp>() {
            @Override
            public void onResponse(Call<AuthUserResp> call, Response<AuthUserResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<AuthUserResp> call, Throwable t) {

            }
        });
    }

}