package com.petbooking.Constants;

/**
 * Created by Luciano José on 09/04/2017.
 */

public class APIConstants {

    public static final String BASE_URL_BETA = "https://beta.petbooking.com.br/api/v2/";
    public static final String BASE_URL_PRODUCTION = "https://petbooking.com.br/api/v2/";
    public static final String ASSET_ENDPOINT_BETA = "https://beta.petbooking.com.br/%s";
    public static final String ASSET_ENDPOINT_PRODUCTION = "https://petbooking.com.br/%s";

    /**
     * DATA TYPES
     */

    public static final String DATA_PROVIDER = "b2beauty";
    public static final String DATA_PROVIDER_FACEBOOK = "facebook";
    public static final String DATA_CONSUMERS = "consumers";
    public static final String DATA_USERS = "users";
    public static final String DATA_SESSIONS = "sessions";
    public static final String DATA_FAVORITES = "favorites";
    public static final String DATA_FAVORITES_TYPE_BUSINESS = "Business";
    public static final String DATA_REVIEW_TYPE_REVIEWS = "reviews";


    /**
     * CONFIG AND QUERYS
     */
    public static final String UUID_PRODUCTION = "fe2e0261-2cac-440d-8027-c8cfc4720940";
    public static final String UUID_BETA = "489ceffe-d387-4416-a9b0-0318c8a83dd2";
    public static final String PATH_PARAM = "ID_PARAM";
    public static final int PAGE_SIZE_DEAFULT = 10;
    public static final String QUERY_COORDS = "coords";
    public static final String QUERY_PAGE_SIZE_DEAFULT = "?page[size]=" + PAGE_SIZE_DEAFULT;
    public static final String QUERY_USER_ID = "user_id";
    public static final String QUERY_TEXT = "q";
    public static final String QUERY_CATEGORY_TEMPLATE = "category_template_id";
    public static final String PAYMENT_OK_TAG = "ok";
    public static final String FALLBACK_TAG = "fallbacks";
    public static final String CURRENT_DATE = "current_date";
    public static final String PET_TYPE = "pet_type";

    /**
     * Headers
     */
    public static final String HEADER_LANGUAGE = "X-PB-language: required";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_TYPE_VALUE = "application/vnd.api+json";
    public static final String HEADER_AUTHORIZATION_REQUIRED = "Authorization: required";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_AUTHORIZATION_FORMAT = "Bearer %s";
    public static final String HEADER_SESSION_TOKEN_REQUIRED = "X-PetBooking-Session-Token: required";
    public static final String HEADER_SESSION_TOKEN = "X-PetBooking-Session-Token";
    public static final String HEADER_SESSION_TOKEN_FORMAT = "Token token=\"%s\"";


    /**
     * Error Code
     */

    public static final int ERROR_CODE_INVALID_LOGIN = 124;

    /**
     * Query
     */
    public static final String QUERY_PAGE_INDEX = "page[number]";
    public static final String QUERY_PAGE_LIMIT = "page[size]";

}
