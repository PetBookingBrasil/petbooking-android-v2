package com.petbooking.API.Interceptors;

import com.petbooking.Constants.APIConstants;
import com.petbooking.Events.HideLoadingEvt;
import com.petbooking.Events.ShowLoadingEvt;
import com.petbooking.Managers.SessionManager;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Set;

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
        Set<String> headers = originalRequest.headers().names();

        if (headers.contains(APIConstants.HEADER_AUTHORIZATION)) {
            String authorization = String.format(APIConstants.HEADER_AUTHORIZATION_FORMAT,
                    SessionManager.getInstance().getConsumerToken());
            newRequestBuilder.header(APIConstants.HEADER_AUTHORIZATION, authorization);
        }

        if (headers.contains(APIConstants.HEADER_SESSION_TOKEN_REQUIRED)) {
            String userToken = String.format(APIConstants.HEADER_SESSION_TOKEN_FORMAT,
                    SessionManager.getInstance().getSessionToken());
            newRequestBuilder.header(APIConstants.HEADER_SESSION_TOKEN, userToken);
        }

        EventBus.getDefault().post(new ShowLoadingEvt());
        Response response = chain.proceed(newRequestBuilder.build());
        EventBus.getDefault().post(new HideLoadingEvt());
        return response;
    }
}
