package com.petbooking.Models;

import com.petbooking.API.Business.Models.BusinessesRspAttributes;

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
    public String latitude;
    public String longitude;
    public String website;
    public String phone;
    public String facebook;
    public String instagram;
    public String twitter;
    public String googlePlus;
    public BusinessesRspAttributes.CoverImage image;
    public boolean imported;


    public Business(String id, String name, String description, String city, String state, String street,
                    String neighborhood, String streetNumber, String zipcode, float ratingAverage, int ratingCount,
                    float distance, String businesstype, String latitude, String longitude,
                    String website, String phone, String facebook, String instagram, String twitter,
                    String googlePlus, BusinessesRspAttributes.CoverImage image, boolean imported) {
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
        this.image = image;
        this.imported = imported;
    }

    public Business(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
