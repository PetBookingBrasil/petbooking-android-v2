package com.petbooking.API.Auth;

import com.petbooking.Constants.APIConstants;

/**
 * Created by Luciano José on 09/04/2017.
 */

public class APIAuthConstants {

    public static String DATA_PROVIDER = "b2beauty";
    public static final String DATA_CONSUMERS = "consumers";
    public static final String DATA_USERS = "users";
    public static final String DATA_SESSIONS = "sessions";

    public static final String CONSUMER_AUTH_ENDPOINT = "consumers/auth";
    public static final String USER_ENDPOINT = "users";
    public static final String ENDPOINT_REQUEST_USER = "users/{" + APIConstants.PATH_PARAM + "}";
    public static final String SESSION_ENDPOINT = "sessions";

}
