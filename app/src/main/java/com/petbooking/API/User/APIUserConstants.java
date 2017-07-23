package com.petbooking.API.User;

import com.petbooking.Constants.APIConstants;

/**
 * Created by Luciano José on 16/04/2017.
 */

public class APIUserConstants {

    public static final String USER_ENDPOINT = "users";
    public static final String ENDPOINT_REQUEST_USER = "users/{" + APIConstants.PATH_PARAM + "}";
    public static final String ENDPOINT_UPDATE_USER = "users/{" + APIConstants.PATH_PARAM + "}";
    public static final String ENDPOINT_USER_ADDRESS = "https://viacep.com.br/ws/{" + APIConstants.PATH_PARAM + "}/json/";
    public static final String ENDPOINT_RECOVER_PASSWORD = "users/recover-password";

    public static final String FILTER_TYPE_BUSINESS = "&type=businesses";

    public static final String FAVORITES_ENDPOINT = "users/{" + APIConstants.PATH_PARAM + "}/favorites" + APIConstants.QUERY_PAGE_SIZE_DEAFULT
            + FILTER_TYPE_BUSINESS;

    public static final String SCHEDULES_ENDPOINT = "users/{" + APIConstants.PATH_PARAM + "}/schedules";
}
