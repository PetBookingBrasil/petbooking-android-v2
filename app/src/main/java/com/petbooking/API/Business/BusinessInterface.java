package com.petbooking.API.Business;

import com.petbooking.API.Appointment.Models.ServiceResp;
import com.petbooking.API.Business.Models.BannerResponse;
import com.petbooking.API.Business.Models.BusinessResp;
import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.API.Business.Models.FavoriteResp;
import com.petbooking.API.Business.Models.FavoriteRqt;
import com.petbooking.API.Business.Models.ReviewResp;
import com.petbooking.API.Business.Models.ReviewableResp;
import com.petbooking.API.Review.ReviewRequest;
import com.petbooking.Constants.APIConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
    @GET(APIBusinessConstants.BUSINESS_SEARCH_ENDPOINT)
    Call<BusinessesResp> searchBusiness(@Query(APIConstants.QUERY_COORDS) String coords,
                                        @Query(APIConstants.QUERY_USER_ID) String userId,
                                        @Query(APIConstants.QUERY_PAGE_INDEX) int pageIndex,
                                        @Query(APIConstants.QUERY_PAGE_LIMIT) int limit,
                                        @Query(APIConstants.QUERY_CATEGORY_TEMPLATE) String categoryID,
                                        @Query(APIConstants.QUERY_TEXT) String text);


    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @GET(APIBusinessConstants.BUSINESS_PROMO_ENDPOINT)
    Call<BusinessesResp> listPromos(@Path(APIBusinessConstants.PATH_PROMO_ID) String promoID,
                                    @Query(APIConstants.QUERY_LATITUDE) String latitude,
                                    @Query(APIConstants.QUERY_LONGITUDE) String longitude);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @GET(APIBusinessConstants.BUSINESS_PROMO_ENDPOINT)
    Call<BusinessesResp> listPromos(@Path(APIBusinessConstants.PATH_PROMO_ID) String promoID);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.BUSINESS_SEARCH_ENDPOINT)
    Call<BusinessesResp> listBusinessByCategory(@Query(APIConstants.QUERY_COORDS) String coords,
                                                @Query(APIConstants.QUERY_USER_ID) String userId,
                                                @Query(APIConstants.QUERY_PAGE_INDEX) int pageIndex,
                                                @Query(APIConstants.QUERY_PAGE_LIMIT) int limit,
                                                @Query(APIConstants.QUERY_CATEGORY_TEMPLATE) String categoryID);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.ENDPOINT_BUSINESS_REVIEWS)
    Call<ReviewResp> listBusinessReviews(@Path(APIBusinessConstants.PATH_BUSINESS_ID) String businessID,
                                         @Query(APIConstants.QUERY_PAGE_INDEX) int pageIndex);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED,APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @GET(APIBusinessConstants.ENDPOINT_GET_REVIEWS)
    Call<ReviewableResp> getReviews (@Path(APIConstants.PATH_PARAM) String userId);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED,APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @POST(APIBusinessConstants.ENDPOINT_SEND_REVIEW)
    Call<Void> sendReviews(@Path(APIConstants.PATH_PARAM) String userID,
                                                   @Body ReviewRequest reviewRequest);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.BUSINESS_INFO_ENDPOINT)
    Call<BusinessResp> getBusiness(@Path(APIBusinessConstants.PATH_BUSINESS_ID) String businessId,
                                   @Query(APIConstants.QUERY_USER_ID) String userId);


    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.SERVICES_CATEGORIES_ENDPOINT)
    Call<CategoryResp> listCategories();

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.BUSSINESS_BANNER_ENDPOINT)
    Call<BannerResponse> listBanners();

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.BUSSINESS_BANNER_ENDPOINT)
    Call<BannerResponse> listBanners(@Query(APIConstants.QUERY_LATITUDE) String latitude,
                                     @Query(APIConstants.QUERY_LONGITUDE) String longitude);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @GET(APIBusinessConstants.BUSINESS_CATEGORIES_ENDPOINT)
    Call<CategoryResp> listBusinessCategories(@Path(APIBusinessConstants.PATH_BUSINESS_ID) String businessId);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @POST(APIBusinessConstants.FAVORITES_CREATE_ENDPOINT)
    Call<FavoriteResp> createFavorite(@Path(APIConstants.PATH_PARAM) String userId, @Body FavoriteRqt favoriteRqt);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @DELETE(APIBusinessConstants.FAVORITES_DELETE_ENDPOINT)
    Call<FavoriteResp> deleteFavorite(@Path(APIConstants.PATH_PARAM) String favoriteId);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @POST(APIBusinessConstants.ENDPOINT_BUSSINESS_SLUG)
    Call<Void> listBussinesSlug(@Path((APIConstants.PATH_PARAM)) String bussinesSlug);
}

