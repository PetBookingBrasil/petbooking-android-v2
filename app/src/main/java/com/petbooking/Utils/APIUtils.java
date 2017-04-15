package com.petbooking.Utils;

import com.google.gson.Gson;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.Generic.ErrorResp;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Luciano José on 09/04/2017.
 */

public class APIUtils {

    public static User parseUser(AuthUserResp resp) {
        AuthUserResp.Attributes attr = resp.data.attributes;
        User user = new User(resp.data.id, attr.authToken, attr.name, attr.birthday, attr.phone, attr.phoneActivated,
                attr.phoneCodeCreatedAt, attr.email, attr.futureEventsCount, attr.acceptsSms, attr.zipcode,
                attr.street, attr.neighborhood, attr.streetNumber, attr.city, attr.state, attr.nickname,
                attr.gender, attr.cpf, attr.searchRange, attr.acceptsEmail, attr.acceptsNotifications, attr.acceptsTerms);

        return user;
    }

    public static void handleResponse(Response response, APICallback callback) {
        if (response.isSuccessful()) {
           callback.onSuccess(response.body());
        } else {
           callback.onError(handleError(response));
        }
    }

    public static Object handleError(Response response) {
        ResponseBody errorBody = response.errorBody();
        String error = null;
        try {
            error = errorBody != null ? new String(errorBody.bytes(), Charset.defaultCharset()) : response.message();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Gson().fromJson(error, ErrorResp.class);
    }
}
