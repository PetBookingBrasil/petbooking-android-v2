package com.petbooking.API.User;

import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.Business.Models.BusinessResp;
import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.API.User.Models.CreateSocialUserRqt;
import com.petbooking.API.User.Models.CreateUserRqt;
import com.petbooking.API.User.Models.RecoverPasswordRqt;
import com.petbooking.API.User.Models.UpdateUserRqt;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Models.UserAddress;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Luciano José on 16/04/2017.
 */

public interface UserInterface {

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @POST(APIUserConstants.USER_ENDPOINT)
    Call<AuthUserResp> createUser(@Body CreateUserRqt createUserRqt);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED})
    @POST(APIUserConstants.USER_ENDPOINT)
    Call<AuthUserResp> createSocialUser(@Body CreateSocialUserRqt createSocialUserRqt);


    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @PUT(APIUserConstants.ENDPOINT_UPDATE_USER)
    Call<AuthUserResp> updateUser(@Path(APIConstants.PATH_PARAM) String userID, @Body UpdateUserRqt updateUserRqt);

    @Headers(APIConstants.HEADER_AUTHORIZATION_REQUIRED)
    @POST(APIUserConstants.ENDPOINT_RECOVER_PASSWORD)
    Call<Void> requestPassword(@Body RecoverPasswordRqt recoverPassRqt);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @GET(APIUserConstants.ENDPOINT_REQUEST_USER)
    Call<AuthUserResp> getUser(@Path(APIConstants.PATH_PARAM) String userID);

    @GET(APIUserConstants.ENDPOINT_USER_ADDRESS)
    Call<UserAddress> getAddress(@Path(APIConstants.PATH_PARAM) String zipcode);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @GET(APIUserConstants.FAVORITES_ENDPOINT)
    Call<BusinessesResp> listFavorites(@Path(APIConstants.PATH_PARAM) String userId,
                                       @Query(APIConstants.QUERY_COORDS) String coords,
                                       @Query(APIConstants.QUERY_PAGE_INDEX) int pageIndex);

}
