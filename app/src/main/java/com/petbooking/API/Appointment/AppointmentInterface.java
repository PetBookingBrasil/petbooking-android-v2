package com.petbooking.API.Appointment;


import com.petbooking.API.Appointment.Models.CartResp;
import com.petbooking.API.Appointment.Models.CartRqt;
import com.petbooking.API.Appointment.Models.ProfessionalResp;
import com.petbooking.API.Appointment.Models.ServiceResp;
import com.petbooking.Constants.APIConstants;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Luciano José on 11/04/2017.
 */

public interface AppointmentInterface {

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @GET(APIAppointmentConstants.CATEGORY_SERVICES_ENDPOINT)
    Call<ServiceResp> listCategoryServices(@Path(APIConstants.PATH_PARAM) String categoryId,
                                           @QueryMap Map<String,String> petId);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @GET(APIAppointmentConstants.PROFESSIONAL_LIST_ENDPOINT)
    Call<ProfessionalResp> listProfessional(@Path(APIConstants.PATH_PARAM) String serviceId);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @POST(APIAppointmentConstants.CREATE_CART_ENDPOINT)
    Call<CartResp> createCart(@Path(APIConstants.PATH_PARAM) String userId, @Body CartRqt cart);
}
