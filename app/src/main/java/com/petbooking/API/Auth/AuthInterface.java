package com.petbooking.API.Auth;


import com.petbooking.API.Auth.Models.AuthConsumerResp;
import com.petbooking.API.Auth.Models.AuthConsumerRqt;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.Auth.Models.AuthUserRqt;
import com.petbooking.API.Auth.Models.SessionResp;
import com.petbooking.Constants.APIConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Luciano José on 11/04/2017.
 */

public interface AuthInterface {

    @POST(APIAuthConstants.CONSUMER_AUTH_ENDPOINT)
    Call<AuthConsumerResp> authConsumer(@Body AuthConsumerRqt authConsumerRqt);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @POST(APIAuthConstants.SESSION_ENDPOINT)
    Call<SessionResp> authUser(@Body AuthUserRqt authUserRqt);

}
