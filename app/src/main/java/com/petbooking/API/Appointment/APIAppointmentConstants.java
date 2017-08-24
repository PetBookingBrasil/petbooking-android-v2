package com.petbooking.API.Appointment;

import com.petbooking.Constants.APIConstants;

/**
 * Created by Luciano José on 29/07/2017.
 */

public class APIAppointmentConstants {

    public static final String DATA_CART_TYPE = "carts";
    public static final String PET_QUERY = "pet_id";

    public static final String CATEGORY_SERVICES_ENDPOINT = "service-categories/{" + APIConstants.PATH_PARAM + "}/services";
    public static final String PROFESSIONAL_LIST_ENDPOINT = "services/{" + APIConstants.PATH_PARAM + "}/employments";
    public static final String CREATE_CART_ENDPOINT = "users/{" + APIConstants.PATH_PARAM + "}/carts";

    public static final String PAYMENT_WEBVIEW_URL = "%swebviews/payments/%s/%s/new";
}
