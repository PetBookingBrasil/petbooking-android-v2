package com.petbooking.Models;

import com.petbooking.API.Generic.AvatarResp;

import java.util.ArrayList;

/**
 * Created by Luciano José on 27/05/2017.
 */

public class BusinessServices {

    public String id;
    public String name;
    public String startTime;
    public String endTime;
    public int duration;
    public String description;
    public double price;
    public String businessName;
    public String professionalName;
    public AvatarResp professionalAvatar;
    public ArrayList<BusinessServices> additionalServices;

    public BusinessServices(String id, String name, int duration, String description, double price) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.price = price;
        this.additionalServices = new ArrayList<>();
    }

    public BusinessServices(String id, String name, String startTime, String endTime, int duration, String description,
                            double price, String businessName, String professionalName, AvatarResp professionalAvatar) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.description = description;
        this.price = price;
        this.businessName = businessName;
        this.professionalName = professionalName;
        this.professionalAvatar = professionalAvatar;
        this.additionalServices = new ArrayList<>();
    }

    public void setAdditionalServices(ArrayList<BusinessServices> additionalServices) {
        this.additionalServices = additionalServices;
    }
}
