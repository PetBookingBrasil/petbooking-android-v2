package com.petbooking.API.Pet;

import com.petbooking.API.APIClient;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Pet;

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

    public void listBreedsCat(APICallback callback) {

    }

    public void listBreedsDog(APICallback callback) {

    }
}
