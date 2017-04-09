package com.petbooking.Utils;

import com.google.gson.Gson;
import com.petbooking.Constants.APIConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Luciano José on 09/04/2017.
 */

public class APIUtils {

    static Gson mJsonManager = new Gson();
    static final HashMap<String, String> headers = new HashMap<String, String>();

    public static JSONObject getBody(Object object) {
        JSONObject body = null;
        try {
            body = new JSONObject(mJsonManager.toJson(object));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return body;
    }

    public static HashMap<String, String> getHeaders(String consumer, String token) {
        consumer = String.format(APIConstants.HEADER_AUTHORIZATION_FORMAT, consumer);
        token = String.format(APIConstants.HEADER_SESSION_TOKEN_FORMAT, token);

        headers.put(APIConstants.HEADER_CONTENT_TYPE, APIConstants.HEADER_CONTENT_TYPE_VALUE);
        headers.put(APIConstants.HEADER_AUTHORIZATION, consumer);
        headers.put(APIConstants.HEADER_SESSION_TOKEN, token);

        return headers;
    }

}
