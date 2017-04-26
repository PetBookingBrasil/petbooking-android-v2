package com.petbooking.API;

import com.petbooking.API.Interceptors.HeaderInterceptor;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Constants.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Luciano José on 11/04/2017.
 */

public class APIClient {

    private static Retrofit retrofit = null;
    private static OkHttpClient.Builder mBuilder;
    private static OkHttpClient mClient;
    private static HttpLoggingInterceptor mLogger;
    private static HeaderInterceptor headerInterceptor;

    public static Retrofit getClient() {
        if (retrofit == null) {
            buildClient();
        }

        return retrofit;
    }

    private static void buildClient() {
        mLogger = new HttpLoggingInterceptor();
        mLogger.setLevel(HttpLoggingInterceptor.Level.BODY);

        headerInterceptor = new HeaderInterceptor();

        mBuilder = new OkHttpClient.Builder();
        mBuilder.addNetworkInterceptor(new HeaderInterceptor());
        mBuilder.addNetworkInterceptor(mLogger);
        mBuilder.connectTimeout(AppConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        mBuilder.writeTimeout(AppConstants.WRITE_TIMEOUT, TimeUnit.SECONDS);
        mBuilder.readTimeout(AppConstants.READ_TIMEOUT, TimeUnit.SECONDS);

        mClient = mBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(APIConstants.BASE_URL_BETA)
                .addConverterFactory(GsonConverterFactory.create())
                .client(mClient)
                .build();
    }

}
