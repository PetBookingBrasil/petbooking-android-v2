package com.petbooking.Models;

import java.util.ArrayList;

/**
 * Created by Luciano José on 06/08/2017.
 */

public class Professional {

    public String id;
    public String name;
    public String imageUrl;
    public ArrayList<AppointmentDate> availableDates = new ArrayList<>();

    public Professional(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void setAvailableDates(ArrayList<AppointmentDate> availableDates) {
        this.availableDates = availableDates;
    }
}
