package com.petbooking.API.Auth;

import com.petbooking.Models.ConsumerResp;
import com.petbooking.Models.ConsumerRqt;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Luciano José on 11/04/2017.
 */

public interface AuthInterface {

    @POST(APIAuthConstants.CONSUMER_AUTH_ENDPOINT)
    Call<ConsumerResp> authConsumer(@Body ConsumerRqt consumerRqt);

}
