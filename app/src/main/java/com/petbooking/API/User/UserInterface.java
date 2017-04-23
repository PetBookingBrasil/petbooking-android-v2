package com.petbooking.API.User;

import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.User.Models.CreateUserRqt;
import com.petbooking.API.User.Models.RecoverPasswordRqt;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Models.UserAddress;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Luciano José on 16/04/2017.
 */

public interface UserInterface {

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @POST(APIUserConstants.USER_ENDPOINT)
    Call<AuthUserResp> createUser(@Body CreateUserRqt createUserRqt);


    @Headers(APIConstants.HEADER_AUTHORIZATION_REQUIRED)
    @POST(APIUserConstants.ENDPOINT_RECOVER_PASSWORD)
    Call<Void> requestPassword(@Body RecoverPasswordRqt recoverPassRqt);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @GET(APIUserConstants.ENDPOINT_REQUEST_USER)
    Call<AuthUserResp> getUser(@Path(APIConstants.PATH_PARAM) String userID);

    @GET(APIUserConstants.ENDPOINT_USER_ADDRESS)
    Call<UserAddress> getAddress(@Path(APIConstants.PATH_PARAM) String zipcode);

}
