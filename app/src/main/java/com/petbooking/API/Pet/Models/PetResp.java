package com.petbooking.API.Pet.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.API.Generic.AvatarResp;

/**
 * Created by Luciano José on 30/04/2017.
 */
public class PetResp {

    public Item data;

    public static class Item {

        public String id;
        public String type;
        public Attributes attributes;

    }

    public static class Attributes {

        public String name;
        public String kind;
        public String gender;
        public String mood;
        public String description;
        public String size;
        public AvatarResp photo;

        @SerializedName("breed_id")
        public String breedID;

        @SerializedName("breed_name")
        public String breedName;

        @SerializedName("user_id")
        public String userID;

        @SerializedName("birth_date")
        public String birth;

        @SerializedName("coat_type")
        public String coatType;

    }

}
