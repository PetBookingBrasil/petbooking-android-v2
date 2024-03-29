package com.petbooking.Models;

import android.util.Log;

import com.petbooking.API.Business.Models.BusinessesRspAttributes;
import com.petbooking.API.Generic.AvatarResp;

/**
 * Created by Luciano José on 07/05/2017.
 */

public class Business {

    public String id;
    public String name;
    public String description;
    public String city;
    public String state;
    public String street;
    public String neighborhood;
    public String streetNumber;
    public String zipcode;
    public float ratingAverage;
    public int ratingCount;
    public float distance;
    public String businesstype;
    public Double latitude;
    public Double longitude;
    public String website;
    public String phone;
    public String facebook;
    public String instagram;
    public String twitter;
    public String googlePlus;
    public String snapchat;
    public BusinessesRspAttributes.CoverImage image;
    public AvatarResp avatar;
    public BusinessesRspAttributes.UserFavorite userFavorite;
    public boolean favorited;
    public boolean imported;
    public String favoritedId;
    public String slug;


    public Business(String id, String name, String description, String city, String state, String street,
                    String neighborhood, String streetNumber, String zipcode, float ratingAverage, int ratingCount,
                    float distance, String businesstype, Double latitude, Double longitude,
                    String website, String phone, String facebook, String instagram, String twitter,
                    String googlePlus, String snapchat, BusinessesRspAttributes.CoverImage image, AvatarResp avatar,
                    BusinessesRspAttributes.UserFavorite userFavorite, boolean favorited, boolean imported, String favoritedId,String slug) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.state = state;
        this.street = street;
        this.neighborhood = neighborhood;
        this.streetNumber = streetNumber;
        this.zipcode = zipcode;
        this.ratingAverage = ratingAverage;
        this.ratingCount = ratingCount;
        this.distance = distance;
        this.businesstype = businesstype;
        this.latitude = latitude;
        this.longitude = longitude;
        this.website = website;
        this.phone = phone;
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.googlePlus = googlePlus;
        this.snapchat = snapchat;
        this.image = image;
        this.avatar = avatar;
        this.userFavorite = userFavorite;
        this.favorited = favorited;
        this.imported = imported;
        this.favoritedId = favoritedId;
        this.slug = slug;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setFavoritedId(String favoritedId) {
        this.favoritedId = favoritedId;
    }

}

