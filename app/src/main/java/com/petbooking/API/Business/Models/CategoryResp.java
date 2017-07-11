package com.petbooking.API.Business.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResp {

    public List<Item> data;

    public static class Attributes {

        public String name;
        public String slug;

        @SerializedName("cover_image")
        public CoverImage coverImage;

    }

    public static class CoverImage {

        public String url;
        public Listing listing;

    }

    public static class Item {

        public String id;
        public String type;
        public Attributes attributes;

    }

    public static class Listing {

        public String url;

    }

}
