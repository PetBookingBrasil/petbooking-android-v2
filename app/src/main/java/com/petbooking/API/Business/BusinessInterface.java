package com.petbooking.API.Business;

import com.petbooking.API.Business.Models.BusinessResp;
import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.API.Business.Models.FavoriteResp;
import com.petbooking.API.Business.Models.FavoriteRqt;
import com.petbooking.API.Business.Models.ReviewResp;
import com.petbooking.Constants.APIConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Luciano José on 07/05/2017.
 */

public interface BusinessInterface {

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.BUSINESS_ENDPOINT)
    Call<BusinessesResp> listBusiness(@Query(APIConstants.QUERY_COORDS) String coords,
                                      @Query(APIConstants.QUERY_USER_ID) String userId,
                                      @Query(APIConstants.QUERY_PAGE_INDEX) int pageIndex,
                                      @Query(APIConstants.QUERY_PAGE_LIMIT) int limit);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.ENDPOINT_BUSINESS_REVIEWS)
    Call<ReviewResp> listBusinessReviews(@Path(APIBusinessConstants.PATH_BUSINESS_ID) String businessID,
                                         @Query(APIConstants.QUERY_PAGE_INDEX) int pageIndex);


    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.BUSINESS_INFO_ENDPOINT)
    Call<BusinessResp> getBusiness(@Path(APIBusinessConstants.PATH_BUSINESS_ID) String businessId,
                                   @Query(APIConstants.QUERY_USER_ID) String userId);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.BUSINESS_HIGHLIGHT_ENDPOINT)
    Call<BusinessesResp> listHighlightBusiness(@Query(APIConstants.QUERY_USER_ID) String userId);


    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.BUSINESS_BY_CATEGORY_ENDPOINT)
    Call<BusinessesResp> listByCategory(@Path(APIBusinessConstants.PATH_CATEGORY_ID) String categoryID,
                                        @Query(APIBusinessConstants.QUERY_USER_ID) String userID,
                                        @Query(APIConstants.QUERY_COORDS) String coords,
                                        @Query(APIConstants.QUERY_PAGE_INDEX) int pageIndex);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @POST(APIBusinessConstants.FAVORITES_CREATE_ENDPOINT)
    Call<FavoriteResp> createFavorite(@Path(APIConstants.PATH_PARAM) String userId, @Body FavoriteRqt favoriteRqt);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @DELETE(APIBusinessConstants.FAVORITES_DELETE_ENDPOINT)
    Call<FavoriteResp> deleteFavorite(@Path(APIConstants.PATH_PARAM) String favoriteId);
}

