package com.petbooking.UI.Menu.Pets.RegisterPet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.petbooking.API.Pet.Models.BreedResp;
import com.petbooking.API.Pet.PetService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Breed;
import com.petbooking.R;

import java.util.ArrayList;
import java.util.List;

public class RegisterPetActivity extends AppCompatActivity {

    private PetService mPetService;

    private List<Breed> dogBreeds;
    private List<Breed> catBreeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_form);

        mPetService = new PetService();

        dogBreeds = new ArrayList<>();
        catBreeds = new ArrayList<>();

        listDogBreeds();
        listCatBreeds();
    }

    /**
     * List All Dog Breeds
     */
    public void listDogBreeds() {
        mPetService.listDogBreeds(new APICallback() {
            @Override
            public void onSuccess(Object response) {
                BreedResp breeds = (BreedResp) response;
                for (BreedResp.Item breed : breeds.data) {
                    dogBreeds.add(new Breed(breed.id, breed.attributes.name, breed.attributes.kind, breed.attributes.size));
                }
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    /**
     * List All Cat Breeds
     */
    public void listCatBreeds() {
        mPetService.listCatBreeds(new APICallback() {
            @Override
            public void onSuccess(Object response) {
                BreedResp breeds = (BreedResp) response;
                for (BreedResp.Item breed : breeds.data) {
                    catBreeds.add(new Breed(breed.id, breed.attributes.name, breed.attributes.kind, breed.attributes.size));
                }
            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}
