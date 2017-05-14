package com.petbooking.API.Business.Models;

import com.google.gson.annotations.SerializedName;

public class FavoriteResp {

    public Data data;

    public static class Data {

        public String id;
        public String type;
        public Attributes attributes;

    }

    public static class Attributes {

        @SerializedName("favorable_type")
        public String favorableType;

        @SerializedName("favorable_id")
        public int favorableId;

        @SerializedName("favorite_count")
        public int favoriteCount;
    }

}
