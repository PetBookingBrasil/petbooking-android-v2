package com.petbooking.API.Pet;

import com.petbooking.API.APIClient;
import com.petbooking.API.Pet.Models.AttributesResponse;
import com.petbooking.API.Pet.Models.BreedResp;
import com.petbooking.API.Pet.Models.CreatePetRqt;
import com.petbooking.API.Pet.Models.ListPetsResp;
import com.petbooking.API.Pet.Models.PetResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Pet;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

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

    public void createPet(String userId, Pet pet, final APICallback callback) {
        CreatePetRqt petRqt = new CreatePetRqt(pet);
        Call<PetResp> call = mPetInterface.createPet(userId, petRqt);
        call.enqueue(new Callback<PetResp>() {
            @Override
            public void onResponse(Call<PetResp> call, Response<PetResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<PetResp> call, Throwable t) {

            }
        });
    }

    public void updatePet(String userId, Pet pet, final APICallback callback) {
        CreatePetRqt petRqt = new CreatePetRqt(pet);
        Call<PetResp> call = mPetInterface.updatePet(userId, pet.id, petRqt);
        call.enqueue(new Callback<PetResp>() {
            @Override
            public void onResponse(Call<PetResp> call, Response<PetResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<PetResp> call, Throwable t) {

            }
        });
    }

    public void removePet(String userId, String petId, final APICallback callback) {
        Call<Void> call = mPetInterface.removePet(userId, petId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void listPets(String userID, final APICallback callback) {
        Call<ListPetsResp> call = mPetInterface.listPets(userID);
        call.enqueue(new Callback<ListPetsResp>() {
            @Override
            public void onResponse(Call<ListPetsResp> call, Response<ListPetsResp> response) {
                if (response.isSuccessful()) {
                    ArrayList<Pet> petList = new ArrayList<Pet>();
                    ListPetsResp pets = response.body();
                    for (ListPetsResp.Item pet : pets.data) {
                        petList.add(new Pet(pet.id, pet.attributes.name, pet.attributes.kind, pet.attributes.breedName,
                                pet.attributes.size, pet.attributes.breedID, pet.attributes.userID, pet.attributes.gender,
                                pet.attributes.mood, pet.attributes.description, pet.attributes.birth, pet.attributes.coatType,
                                pet.attributes.photo));
                    }

                    callback.onSuccess(petList);
                } else {
                    callback.onError(APIUtils.handleError(response));
                }
            }

            @Override
            public void onFailure(Call<ListPetsResp> call, Throwable t) {

            }
        });
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

    public void getAtributtes(final APICallback callback){
        Call<AttributesResponse> call = mPetInterface.getAtributtes();

        call.enqueue(new Callback<AttributesResponse>() {
            @Override
            public void onResponse(Call<AttributesResponse> call, Response<AttributesResponse> response) {
                APIUtils.handleResponse(response,callback);
            }

            @Override
            public void onFailure(Call<AttributesResponse> call, Throwable t) {

            }
        });
    }
}
