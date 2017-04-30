package com.petbooking.API.Pet.Models;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.petbooking.API.Pet.APIPetConstants;
import com.petbooking.Models.Pet;

/**
 * Created by Luciano José on 30/04/2017.
 */
public class CreatePetRqt {

    public Data data;

    public CreatePetRqt() {
    }

    public CreatePetRqt(String userID, Pet pet) {
        this.data = null;
    }

    public static class Data {

        public String id;
        public String type;
        public Attributes attributes;

        public Data() {
        }

        public Data(String petID, Attributes attributes) {
            this.id = petID;
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

        @SerializedName("breed_id")
        public String breedID;

        @SerializedName("user_id")
        public String userID;

        @SerializedName("birth_date")
        public String birth;

        @SerializedName("coat_type")
        public String coatType;

        public Attributes() {
        }

        public Attributes(Context context, String userID, String photo, String name, String gender,
                          String mood, String description, String size, String breedID, String birth,
                          String coatType) {
            this.photo = TextUtils.isEmpty(photo) ? null : APIPetConstants.DATA_BASE64 + photo;
            this.name = name;
            this.gender = "";
            this.mood = mood;
            this.description = description;
            this.size = "";
            this.breedID = breedID;
            this.userID = userID;
            this.birth = TextUtils.isEmpty(birth) ? null : birth;
            this.coatType = "";
        }

    }

}
