package com.petbooking.Utils;

import com.google.gson.Gson;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.Generic.ErrorResp;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.User;
import com.petbooking.R;

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

    private static final String ERROR_STATUS_UNAUTHORIZED = "401";

    private static final int ERROR_CODE_INVALID_LOGIN = 124;
    private static final int ERROR_CODE_OBJECT_REQUEST = 125;
    private static final int ERROR_CODE_INVALID_AUTH_TOKEN = 126;
    private static final int ERROR_CODE_INVALID_USER_TOKEN = 127;

    public static User parseUser(AuthUserResp resp) {
        AuthUserResp.Attributes attr = resp.data.attributes;
        User user = new User(resp.data.id, attr.authToken, attr.name, attr.birthday, attr.phone, attr.phoneActivated,
                attr.phoneCodeCreatedAt, attr.email, attr.futureEventsCount, attr.acceptsSms, attr.zipcode,
                attr.street, attr.neighborhood, attr.streetNumber, attr.city, attr.state, attr.nickname,
                attr.gender, attr.cpf, attr.searchRange, attr.acceptsEmail, attr.acceptsNotifications,
                attr.acceptsTerms, attr.validForScheduling, attr.avatar);

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

    public static int getErrorMessage(ErrorResp errorRsp) {
        if (errorRsp.errors != null && errorRsp.errors.size() > 0) {
            ErrorResp.Error firstError = errorRsp.errors.get(0);
            switch (firstError.status) {
                case ERROR_STATUS_UNAUTHORIZED:
                    return handlerUnauthorizedError(firstError.code);
            }
        }
        return -1;
    }

    private static int handlerUnauthorizedError(int code) {
        switch (code) {
            case ERROR_CODE_INVALID_AUTH_TOKEN:
                return R.string.error_invalid_login;
            case ERROR_CODE_INVALID_USER_TOKEN:
                return R.string.error_invalid_login;
            case ERROR_CODE_INVALID_LOGIN:
                return R.string.error_invalid_login;
        }
        return code;
    }
}
