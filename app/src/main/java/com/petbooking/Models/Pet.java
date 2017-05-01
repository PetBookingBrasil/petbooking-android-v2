package com.petbooking.Models;

import com.petbooking.API.Generic.AvatarResp;

/**
 * Created by Luciano José on 29/04/2017.
 */

public class Pet {

    public String id;
    public String name;
    public String description;
    public AvatarResp avatar;

    public Pet(String id, String name, String description, AvatarResp avatar) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.avatar = avatar;
    }
}
