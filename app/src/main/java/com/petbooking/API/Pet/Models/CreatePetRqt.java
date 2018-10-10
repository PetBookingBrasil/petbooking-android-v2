package com.petbooking.API.Pet.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.API.Pet.APIPetConstants;
import com.petbooking.Models.Pet;

/**
 * Created by Luciano José on 30/04/2017.
 */
public class CreatePetRqt {

    public Data data;
    public transient Attributes attributes;

    public CreatePetRqt(Pet pet) {
        int[] petColor = new int[1];
        petColor[0] = pet.colorPet;

        this.attributes = new Attributes(pet.userId, pet.photo, pet.name, pet.gender,
                pet.mood, pet.description, pet.size, pet.breedId, pet.birthday, pet.coatType,petColor,pet.chipNumber);
        this.data = new Data(pet.id, this.attributes);
    }

    public static class Data {

        public String id;
        public String type;
        public Attributes attributes;

        public Data(String petId, Attributes attributes) {
            this.id = petId;
            this.type = APIPetConstants.DATA_PETS;
            this.attributes = attributes;
        }

    }

    public static class Attributes {

        public String photo;
        public String name;
        public String gender;
        public String mood;
        public String description;
        public String size;
        public int[] coat_colors;
        public String chip_id;

        @SerializedName("breed_id")
        public String breedID;

        @SerializedName("user_id")
        public String userID;

        @SerializedName("birth_date")
        public String birth;

        @SerializedName("coat_type")
        public String coatType;

        public Attributes(String userID, String photo, String name, String gender, String mood,
                          String description, String size, String breedID, String birth, String coatType , int[] coat_colors, String chip_id) {
            this.photo = photo;
            this.name = name;
            this.gender = gender;
            this.mood = mood;
            this.description = description;
            this.size = size;
            this.breedID = breedID;
            this.userID = userID;
            this.birth = birth;
            this.coatType = coatType;
            this.coat_colors = coat_colors;
            this.chip_id = chip_id;
        }
    }

}
