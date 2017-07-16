package com.petbooking.Models;

import java.util.ArrayList;

/**
 * Created by Luciano José on 15/07/2017.
 */

public class AppointmentInfo {

    public String petId;
    public String petName;
    public ArrayList<BusinessServices> services;

    public AppointmentInfo(String petId, String petName, ArrayList<BusinessServices> services) {
        this.petId = petId;
        this.petName = petName;
        this.services = services;
    }
}
