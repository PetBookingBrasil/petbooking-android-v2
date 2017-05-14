package com.petbooking.Models;

import com.petbooking.API.Business.Models.BusinessesRspAttributes;

/**
 * Created by Luciano José on 07/05/2017.
 */

public class Business {

    public String id;
    public String name;
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
    public BusinessesRspAttributes.CoverImage image;
    public boolean imported;


    public Business(String id, String name, String city, String state, String street, String neighborhood, String streetNumber,
                    String zipcode, float ratingAverage, int ratingCount, float distance, String businesstype,
                    String latitude, String longitude, BusinessesRspAttributes.CoverImage image, boolean imported) {
        this.id = id;
        this.name = name;
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
        this.image = image;
        this.imported = imported;
    }

    public Business(String id, String name) {
        this.id = id;
        this.name = name;
    }
}