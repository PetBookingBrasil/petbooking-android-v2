package com.petbooking.API.Business.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.API.Generic.AvatarResp;

import java.util.List;

/**
 * Created by ediposilva on 9/29/15.
 */
public class ReviewResp {

    public List<Item> data;

    public static class Item {

        public String id;
        public String type;
        public Attributes attributes;

    }

    public static class Attributes {

        public String comment;
        @SerializedName("user_name")
        public String userName;
        public AvatarResp avatar;
        public float rating;

        @SerializedName("business_id")
        public int businessId;

        @SerializedName("user_id")
        public int userId;
    }
}
