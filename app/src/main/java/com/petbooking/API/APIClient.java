package com.petbooking.API;

import com.petbooking.API.Interceptors.HeaderInterceptor;
import com.petbooking.Constants.APIConstants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Luciano José on 11/04/2017.
 */

public class APIClient {

    private static Retrofit retrofit = null;
    private static OkHttpClient.Builder mBuilder;
    private static OkHttpClient mClient;
    private static HeaderInterceptor headerInterceptor;

    public static Retrofit getClient() {
        if (retrofit == null) {
            buildClient();
        }

        return retrofit;
    }

    private static void buildClient() {
        headerInterceptor = new HeaderInterceptor();
        mBuilder = new OkHttpClient.Builder();
        mBuilder.addNetworkInterceptor(new HeaderInterceptor());
        mClient = mBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(APIConstants.BASE_URL_PRODUCTION)
                .addConverterFactory(GsonConverterFactory.create())
                .client(mClient)
                .build();
    }

}
