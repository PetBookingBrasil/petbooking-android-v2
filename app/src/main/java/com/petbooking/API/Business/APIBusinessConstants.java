package com.petbooking.API.Business;

import com.petbooking.Constants.APIConstants;

/**
 * Created by Luciano José on 07/05/2017.
 */

public class APIBusinessConstants {

    /**
     * Data
     */
    public static final String DATA_APPOINTMENT = "Consulta veterinária";
    public static final String DATA_TRAINING = "Adestramento";
    public static final String DATA_BATH = "Banho e Tosa";
    public static final String DATA_TRANSPORT = "Transporte (Leva e traz)";
    public static final String DATA_WALKER = "Passeadores";
    public static final String DATA_DAYCARE = "Creche";
    public static final String DATA_HOTEL = "Hotelzinho";
    public static final String DATA_EMERGENCY = "Emergências e Serviços 24 horas";
    public static final String DATA_EXAMS = "Exames";

    public static final String PATH_CATEGORY_ID = "categoryID";
    public static final String PATH_BUSINESS_ID = "businessID";
    public static final String PATH_FAVORITE_ID = "favoriteID";

    public static final String QUERY_TAG = "q";
    public static final String QUERY_USER_ID = "user_id";
    public static final String QUERY_INCLUDE_SEARCH = "&include=services.service_category,service_categories";

    public static final String INCLUDE_REVIEW_BY_USER = "&include=clientship.user";

    public static final String FILTER_FEATURED = "&filter[other]=featured";
    public static final String FILTER_SEARCH_LOCATION = "filter[location]";
    public static final String FILTER_SEARCH_NEIGHBORHOOD = "filter[neighborhood]";
    public static final String FILTER_SEARCH_DATE = "filter[date]";
    public static final String FILTER_SEARCH_MIN_PRICE = "filter[min_price]";
    public static final String FILTER_SEARCH_MAX_PRICE = "filter[max_price]";
    public static final String FILTER_SEARCH_FACILITIES = "filter[facilities]";

    public static final String FIELDS_BUSINESS_CONTENT = "fields[businesses]=id,name,slug,description,street,street_number," +
            "neighborhood,city,state,zipcode,rating_average,rating_count,favorite_count,cover_image,pictures," +
            "distance,location,user_favorite,phone,website,instagram,snapchat,facebook_fanpage," +
            "twitter_profile,transportation_fee,googleplus_profile,services,service_categories,bitmask_values,businesstype,imported";
    public static final String FIELDS_BUSINESS = "?" + FIELDS_BUSINESS_CONTENT;
    public static final String FIELDS_BUSINESS_REVIEWS = "?fields[reviews]=id,comment,rating,business_id,user_id";
    public static final String FIELDS_CATEGORIES = "?fields[category_templates]=id,name,slug,cover_image";
    public static final String FIELDS_FAVORITES = "?fields[favorites]=favorable_id,favorable_type,favorite_count";

    /**
     * APIs
     */
    public static final String BUSINESS_FEATURED_ENDPOINT = "businesses/search" + FIELDS_BUSINESS + FILTER_FEATURED +
            APIConstants.QUERY_PAGE_SIZE_DEAFULT;

    public static final String BUSINESS_ENDPOINT = "businesses" + FIELDS_BUSINESS + APIConstants.QUERY_PAGE_SIZE_DEAFULT;

    public static final String BUSINESS_BY_CATEGORY_ENDPOINT = "category-templates/{" + PATH_CATEGORY_ID + "}/businesses" +
            FIELDS_BUSINESS + APIConstants.QUERY_PAGE_SIZE_DEAFULT;

    public static final String BUSINESS_SEARCH_ENDPOINT = "businesses/search" + FIELDS_BUSINESS + QUERY_INCLUDE_SEARCH +
            APIConstants.QUERY_PAGE_SIZE_DEAFULT;

    public static final String SERVICES_CATEGORIES_ENDPOINT = "category-templates" + FIELDS_CATEGORIES +
            APIConstants.QUERY_PAGE_SIZE_DEAFULT;

    public static final String BUSINESS_REVIEWS_ENDPOINT = "businesses/{" + PATH_BUSINESS_ID + "}/reviews" +
            FIELDS_BUSINESS_REVIEWS + INCLUDE_REVIEW_BY_USER + APIConstants.QUERY_PAGE_SIZE_DEAFULT;

    public static final String REVIEW_CREATE_ENDPOINT = "businesses/{" + PATH_BUSINESS_ID + "}/reviews";

    public static final String FAVORITES_CREATE_ENDPOINT = "users/{" + APIConstants.PATH_PARAM + "}/favorites" + FIELDS_FAVORITES;

    public static final String FAVORITES_DELETE_ENDPOINT = "favorites/{" + PATH_FAVORITE_ID + "}" + FIELDS_FAVORITES;

}
