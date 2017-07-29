package com.petbooking.API.AppointmentService;


import com.petbooking.API.Business.APIBusinessConstants;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.Constants.APIConstants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Luciano José on 11/04/2017.
 */

public interface AppointmentInterface {

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @GET(APIAppointmentConstants.CATEGORY_SERVICES_ENDPOINT)
    Call<CategoryResp> listCategoryServices(@Path(APIConstants.PATH_PARAM) String categoryId,
                                            @Query(APIAppointmentConstants.PET_QUERY) String petId);
}
