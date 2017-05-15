package com.petbooking.Utils;

import com.google.gson.Gson;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.Generic.APIError;
import com.petbooking.API.Generic.ErrorResp;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.User;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Luciano José on 09/04/2017.
 */

public class APIUtils {

    /**
     * Create User from
     * AuthUserResponse
     *
     * @param resp
     * @return
     */
    public static User parseUser(AuthUserResp resp) {
        AuthUserResp.Attributes attr = resp.data.attributes;
        User user = new User(resp.data.id, attr.authToken, attr.name, attr.birthday, attr.phone, attr.phoneActivated,
                attr.phoneCodeCreatedAt, attr.email, attr.futureEventsCount, attr.acceptsSms, attr.zipcode,
                attr.street, attr.neighborhood, attr.streetNumber, attr.city, attr.state, attr.nickname,
                attr.gender, attr.cpf, attr.searchRange, attr.acceptsEmail, attr.acceptsNotifications,
                attr.acceptsTerms, attr.validForScheduling, attr.avatar);

        return user;
    }

    /**
     * Handle API Response
     *
     * @param response
     * @param callback
     */
    public static void handleResponse(Response response, APICallback callback) {
        if (response.isSuccessful()) {
            callback.onSuccess(response.body());
        } else {
            callback.onError(handleError(response));
        }
    }

    /**
     * Handle Error status
     *
     * @param response
     * @return
     */
    public static Object handleError(Response response) {
        ResponseBody errorBody = response.errorBody();
        String error = null;
        try {
            error = errorBody != null ? new String(errorBody.bytes(), Charset.defaultCharset()) : response.message();
        } catch (IOException e) {
            e.printStackTrace();
        }
        APIError apiError = getFirstError(new Gson().fromJson(error, ErrorResp.class));
        return apiError;
    }

    /**
     * Get Error message
     *
     * @param errorRsp
     */
    public static APIError getFirstError(ErrorResp errorRsp) {
        if (errorRsp.errors != null && errorRsp.errors.size() > 0) {
            ErrorResp.Error firstError = errorRsp.errors.get(0);
            APIError error = new APIError(firstError.title, firstError.detail.toString(), firstError.code, firstError.status);
            return error;
        }

        return null;
    }


    /**
     * Get Asset Endpoint
     *
     * @param assetPath
     * @return
     */
    public static String getAssetEndpoint(String assetPath) {
        String endpoint = String.format(APIConstants.ASSET_ENDPOINT_BETA, assetPath);
        return endpoint;
    }
}
