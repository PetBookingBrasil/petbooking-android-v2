package com.petbooking.Models;

import java.util.ArrayList;

/**
 * Created by Luciano José on 27/05/2017.
 */

public class BusinessServices {

    public String id;
    public String name;
    public String duration;
    public String description;
    public double price;
    public ArrayList<BusinessServices> additionalServices;

    public BusinessServices(String id, String name, String duration, String description, double price) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.price = price;
        this.additionalServices = new ArrayList<>();
    }

    public void setAdditionalServices(ArrayList<BusinessServices> additionalServices) {
        this.additionalServices = additionalServices;
    }
}
