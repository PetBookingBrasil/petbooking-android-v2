package com.petbooking.API.Business;

import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.Constants.APIConstants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Luciano José on 07/05/2017.
 */

public interface BusinessInterface {

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.BUSINESS_ENDPOINT)
    Call<BusinessesResp> listBusiness(@Query(APIConstants.QUERY_COORDS) String coords,
                                      @Query(APIConstants.QUERY_PAGE_INDEX) int pageIndex);


    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.BUSINESS_HIGHLIGHT_ENDPOINT)
    Call<BusinessesResp> listHighlightBusiness();


    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.BUSINESS_BY_CATEGORY_ENDPOINT)
    Call<BusinessesResp> listByCategory(@Path(APIBusinessConstants.PATH_CATEGORY_ID) String categoryID,
                                        @Query(APIBusinessConstants.QUERY_USER_ID) String userID,
                                        @Query(APIConstants.QUERY_COORDS) String coords,
                                        @Query(APIConstants.QUERY_PAGE_INDEX) int pageIndex);

}

