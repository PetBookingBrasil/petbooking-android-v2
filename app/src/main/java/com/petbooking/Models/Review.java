package com.petbooking.Models;

import com.petbooking.API.Generic.AvatarResp;

/**
 * Created by Luciano José on 23/05/2017.
 */

public class Review {

    public String id;
    public String userName;
    public String comment;
    public float rating;
    public AvatarResp userAvatar;

    public Review(String id, String userName, String comment, float rating, AvatarResp avatar) {
        this.id = id;
        this.userName = userName;
        this.comment = comment;
        this.rating = rating;
        this.userAvatar = avatar;
    }

}
