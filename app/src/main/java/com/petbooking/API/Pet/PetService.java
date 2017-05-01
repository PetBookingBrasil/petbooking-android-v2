package com.petbooking.API.Pet;

import com.petbooking.API.APIClient;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.Pet.Models.BreedResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Pet;
import com.petbooking.Utils.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Luciano José on 30/04/2017.
 */

public class PetService {

    private PetInterface mPetInterface;

    public PetService() {
        mPetInterface = APIClient.getClient().create(PetInterface.class);
    }

    public void createPet(String userID, Pet pet, APICallback callback) {

    }

    public void updatePet(String userID, Pet pet, APICallback callback) {

    }

    public void removePet(String userID, Pet pet, APICallback callback) {

    }

    public void listPets(String userID, APICallback callback) {

    }

    public void listCatBreeds(final APICallback callback) {
        Call<BreedResp> call = mPetInterface.listCatBreeds();
        call.enqueue(new Callback<BreedResp>() {
            @Override
            public void onResponse(Call<BreedResp> call, Response<BreedResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<BreedResp> call, Throwable t) {

            }
        });
    }

    public void listDogBreeds(final APICallback callback) {
        Call<BreedResp> call = mPetInterface.listDogBreeds();
        call.enqueue(new Callback<BreedResp>() {
            @Override
            public void onResponse(Call<BreedResp> call, Response<BreedResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<BreedResp> call, Throwable t) {

            }
        });
    }
}
