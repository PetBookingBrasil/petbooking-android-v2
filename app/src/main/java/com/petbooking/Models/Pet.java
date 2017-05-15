package com.petbooking.Models;

import com.petbooking.API.Generic.AvatarResp;

/**
 * Created by Luciano José on 29/04/2017.
 */

public class Pet {

    public String id;
    public String name;
    public String type;
    public String breedName;
    public String size;
    public String breedId;
    public String userId;
    public String gender;
    public String mood;
    public String description;
    public String birthday;
    public String coatType;
    public String photo;
    public String imageBase;
    public AvatarResp avatar;

    public Pet() {
    }

    public Pet(String id, String name, String type, String breedName, String size, String breedId,
               String userId, String gender, String mood, String description, String birthday, String coatType,
               AvatarResp avatar) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.breedName = breedName;
        this.size = size;
        this.breedId = breedId;
        this.userId = userId;
        this.gender = gender;
        this.mood = mood;
        this.description = description;
        this.birthday = birthday;
        this.coatType = coatType;
        this.avatar = avatar;
    }

    public Pet(String id, String name, String description, String breedName, AvatarResp avatar) {
        this.id = id;
        this.name = name;
        this.breedName = breedName;
        this.description = description;
        this.avatar = avatar;
    }

}
