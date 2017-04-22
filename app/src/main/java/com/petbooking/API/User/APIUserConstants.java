package com.petbooking.API.User;

import com.petbooking.Constants.APIConstants;

/**
 * Created by Luciano José on 16/04/2017.
 */

public class APIUserConstants {

    public static final String USER_ENDPOINT = "users";
    public static final String ENDPOINT_REQUEST_USER = "users/{" + APIConstants.PATH_PARAM + "}";
    public static final String ENDPOINT_USER_ADDRESS = "https://viacep.com.br/ws/{" + APIConstants.PATH_PARAM + "}/json/";
    public static final String ENDPOINT_RECOVER_PASSWORD = "users/recover-password";

}
