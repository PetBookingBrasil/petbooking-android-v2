package com.petbooking.API.User;

import android.util.Log;

import com.google.gson.Gson;
import com.petbooking.API.APIClient;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.Business.Models.BusinessResp;
import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.API.User.Models.CreateSocialUserRqt;
import com.petbooking.API.User.Models.CreateUserRqt;
import com.petbooking.API.User.Models.RecoverPasswordRqt;
import com.petbooking.API.User.Models.ScheduleResp;
import com.petbooking.API.User.Models.UpdateUserRqt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Business;
import com.petbooking.Models.User;
import com.petbooking.Models.UserAddress;
import com.petbooking.Utils.APIUtils;

import java.util.ArrayList;

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

    public void getAddress(String zipcode, final APICallback callback) {
        Call<UserAddress> call = mUserInterface.getAddress(zipcode);
        call.enqueue(new Callback<UserAddress>() {
            @Override
            public void onResponse(Call<UserAddress> call, Response<UserAddress> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<UserAddress> call, Throwable t) {

            }
        });
    }

    public void createUser(User user, final APICallback callback) {
        CreateUserRqt userRqt = new CreateUserRqt(user);
        Call<AuthUserResp> call = mUserInterface.createUser(userRqt);
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

    public void createSocialUser(User user, String provider, String providerToken, final APICallback callback) {
        CreateSocialUserRqt userRqt = new CreateSocialUserRqt(user, provider, providerToken);
        Call<AuthUserResp> call = mUserInterface.createSocialUser(userRqt);
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

    public void updateUser(String id, User user, final APICallback callback) {
        UpdateUserRqt userRqt = new UpdateUserRqt(id, user);
        Call<AuthUserResp> call = mUserInterface.updateUser(id, userRqt);
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

    public void listFavorites(String userId, String coords, int page, final APICallback callback) {
        Call<BusinessesResp> call = mUserInterface.listFavorites(userId, coords, page);
        call.enqueue(new Callback<BusinessesResp>() {
            @Override
            public void onResponse(Call<BusinessesResp> call, Response<BusinessesResp> response) {
                if (response.isSuccessful()) {
                    BusinessesResp responseList = response.body();
                    ArrayList<Business> businessList = new ArrayList<>();
                    for (BusinessesResp.Item item : responseList.data) {
                        businessList.add(APIUtils.parseBusiness(item.id, item.attributes));
                    }

                    callback.onSuccess(businessList);
                } else {
                    callback.onError(APIUtils.handleError(response));
                }
            }

            @Override
            public void onFailure(Call<BusinessesResp> call, Throwable t) {

            }
        });
    }

    public void listSchedules(String userId, final APICallback callback) {
        Call<ScheduleResp> call = mUserInterface.listSchedules(userId);
        call.enqueue(new Callback<ScheduleResp>() {
            @Override
            public void onResponse(Call<ScheduleResp> call, Response<ScheduleResp> response) {
                if (response.isSuccessful()) {
                    ScheduleResp responseData = response.body();

                    callback.onSuccess(responseData.data);
                } else {
                    callback.onError(APIUtils.handleError(response));
                }
            }

            @Override
            public void onFailure(Call<ScheduleResp> call, Throwable t) {

            }
        });
    }

}
