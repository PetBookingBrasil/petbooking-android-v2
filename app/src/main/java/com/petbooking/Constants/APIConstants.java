package com.petbooking.Constants;

/**
 * Created by Luciano José on 09/04/2017.
 */

public class APIConstants {

    public static final String BASE_URL_BETA = "https://staging.petbooking.com.br/api/v2/";
    public static final String BASE_URL_PRODUCTION = "https://petbooking.com.br/api/v2/";
    public static final String ASSET_ENDPOINT_BETA = "https://staging.petbooking.com.br/%s";
    public static final String ASSET_ENDPOINT_PRODUCTION = "https://petbooking.com.br/%s";

    /**
     * CONFIG
     */
    public static final String UUID_PRODUCTION = "fe2e0261-2cac-440d-8027-c8cfc4720940";
    public static final String UUID_BETA = "489ceffe-d387-4416-a9b0-0318c8a83dd2";
    public static final String PATH_PARAM = "ID_PARAM";
    public static final int PAGE_SIZE_DEFAULT = 10;

    /**
     * Headers
     */
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_TYPE_VALUE = "application/vnd.api+json";
    public static final String HEADER_AUTHORIZATION_REQUIRED = "Authorization: required";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_AUTHORIZATION_FORMAT = "Bearer %s";
    public static final String HEADER_SESSION_TOKEN_REQUIRED = "X-PetBooking-Session-Token: required";
    public static final String HEADER_SESSION_TOKEN = "X-PetBooking-Session-Token";
    public static final String HEADER_SESSION_TOKEN_FORMAT = "Token token=\"%s\"";


}
