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

        @SerializedName("category_template_icon")
        public CategoryIcon categoryIcon;

    }

    public static class CoverImage {

        public String url;
        @SerializedName("mobile_thumb")
        public Listing listing;

    }

    public static class CategoryIcon {

        @SerializedName("icon")
        public Icon icon;

    }

    public static class Item {

        public String id;
        public String type;
        public Attributes attributes;

    }

    public static class Listing {

        public String url;

    }

    public static class Icon {
        public String url;
    }

}
