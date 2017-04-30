package com.petbooking.API.Pet;

import com.petbooking.API.Pet.Models.BreedResp;
import com.petbooking.API.Pet.Models.CreatePetRqt;
import com.petbooking.API.Pet.Models.ListPetsResp;
import com.petbooking.API.Pet.Models.PetResp;
import com.petbooking.API.User.APIUserConstants;
import com.petbooking.Constants.APIConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Luciano José on 30/04/2017.
 */

public interface PetInterface {


    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @GET(APIPetConstants.ENDPOINT_LIST_PETS)
    Call<ListPetsResp> listPets(@Path(APIConstants.PATH_PARAM) String userID);

    @Headers(APIConstants.HEADER_AUTHORIZATION_REQUIRED)
    @GET(APIPetConstants.ENDPOINT_LIST_BREEDS_CAT)
    Call<BreedResp> listBreedsCat(@Query(APIConstants.QUERY_PAGE_INDEX) int pageIndex);

    @Headers(APIConstants.HEADER_AUTHORIZATION_REQUIRED)
    @GET(APIPetConstants.ENDPOINT_LIST_BREEDS_DOG)
    Call<BreedResp> listBreedsDog(@Query(APIConstants.QUERY_PAGE_INDEX) int pageIndex);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @POST(APIPetConstants.ENDPOINT_CREATE_USER)
    Call<PetResp> createPet(@Path(APIConstants.PATH_PARAM) String userID,
                            @Body CreatePetRqt createPetRqt);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @PUT(APIPetConstants.ENDPOINT_UPDATE_PET)
    Call<PetResp> updatePet(@Path(APIConstants.PATH_PARAM) String userID,
                           @Path(APIPetConstants.PATH_PET_ID) String petID,
                           @Body CreatePetRqt updatePetRqt);


    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @DELETE(APIPetConstants.ENDPOINT_REMOVE_PET)
    Call<Void> removePet(@Path(APIConstants.PATH_PARAM) String userID,
                         @Path(APIPetConstants.PATH_PET_ID) String petID);
}

