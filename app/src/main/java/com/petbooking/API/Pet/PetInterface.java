package com.petbooking.API.Pet;

import com.petbooking.API.Pet.Models.AttributesResponse;
import com.petbooking.API.Pet.Models.BreedResp;
import com.petbooking.API.Pet.Models.CreatePetRqt;
import com.petbooking.API.Pet.Models.ListPetsResp;
import com.petbooking.API.Pet.Models.PetResp;
import com.petbooking.Constants.APIConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Luciano José on 30/04/2017.
 */

public interface PetInterface {


    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @GET(APIPetConstants.LIST_PETS_ENDPOINT)
    Call<ListPetsResp> listPets(@Path(APIConstants.PATH_PARAM) String userID);

    @Headers(APIConstants.HEADER_AUTHORIZATION_REQUIRED)
    @GET(APIPetConstants.LIST_BREEDS_CAT_ENDPOINT)
    Call<BreedResp> listCatBreeds();

    @Headers(APIConstants.HEADER_AUTHORIZATION_REQUIRED)
    @GET(APIPetConstants.LIST_BREEDS_DOG_ENDPOINT)
    Call<BreedResp> listDogBreeds();

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @POST(APIPetConstants.CREATE_USER_ENDPOINT)
    Call<PetResp> createPet(@Path(APIConstants.PATH_PARAM) String userID,
                            @Body CreatePetRqt createPetRqt);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @PUT(APIPetConstants.UPDATE_PET_ENDPOINT)
    Call<PetResp> updatePet(@Path(APIConstants.PATH_PARAM) String userID,
                            @Path(APIPetConstants.PATH_PET_ID) String petID,
                            @Body CreatePetRqt updatePetRqt);


    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @DELETE(APIPetConstants.REMOVE_PET_ENDPOINT)
    Call<Void> removePet(@Path(APIConstants.PATH_PARAM) String userID,
                         @Path(APIPetConstants.PATH_PET_ID) String petID);

    @Headers({APIConstants.HEADER_AUTHORIZATION_REQUIRED, APIConstants.HEADER_SESSION_TOKEN_REQUIRED})
    @GET(APIPetConstants.GET_PET_ATRIBUTTES)
    Call<AttributesResponse> getAtributtes(@Path(APIConstants.PATH_PARAM) String userID);
}

