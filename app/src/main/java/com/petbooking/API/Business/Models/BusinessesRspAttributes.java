package com.petbooking.API.Business.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.API.Generic.AvatarResp;

import java.util.ArrayList;
import java.util.List;

public class BusinessesRspAttributes {

    public String name;
    public String slug;
    public String description;
    public String city;
    public String state;
    public String street;
    public String neighborhood;
    public String zipcode;
    public float distance;
    public List<String> location;
    public String phone;
    public String website;
    public String instagram;
    public String snapchat;
    public AvatarResp avatar;
    public ArrayList<BusinessesResp.Picture> pictures;

    @SerializedName("transportation_fee")
    public String transportationFee;
    @SerializedName("street_number")
    public String streetNumber;
    @SerializedName("rating_average")
    public Float ratingAverage;
    @SerializedName("rating_count")
    public Integer ratingCount;
    @SerializedName("favorite_count")
    public Integer favoriteCount;
    @SerializedName("cover_image")
    public CoverImage coverImage;
    @SerializedName("user_favorite")
    public UserFavorite userFavorite;
    @SerializedName("facebook_fanpage")
    public String facebook;
    @SerializedName("twitter_profile")
    public String twitter;
    @SerializedName("googleplus_profile")
    public String googlePlus;

    public String businesstype;
    public boolean favorited;

    @SerializedName("imported")
    public boolean imported;


    public static class CoverImage {

        public String url;
        public Listing listing;


        @SerializedName("listing_480")
        public CoverImage.Listing listing480;


        @SerializedName("listing_768")
        public CoverImage.Listing listing768;


        @SerializedName("listing_1280")
        public CoverImage.Listing listing1280;

        public static class Listing {
            public String url;
        }

    }

    public static class UserFavorite {

        public String id;

        @SerializedName("user_id")
        public Integer userId;

        @SerializedName("favorable_id")
        public Integer favorableId;

        @SerializedName("favorable_type")
        public String favorableType;

    }

}
