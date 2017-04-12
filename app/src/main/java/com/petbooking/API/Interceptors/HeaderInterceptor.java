package com.petbooking.API.Interceptors;

import android.util.Log;

import com.petbooking.Constants.APIConstants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Luciano José on 11/04/2017.
 */

public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder newRequestBuilder = originalRequest.newBuilder()
                .header(APIConstants.HEADER_CONTENT_TYPE, APIConstants.HEADER_CONTENT_TYPE_VALUE)
                .method(originalRequest.method(), originalRequest.body());

        Response response = chain.proceed(newRequestBuilder.build());
        return response;
    }
}
