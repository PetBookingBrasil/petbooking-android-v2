package com.petbooking.Models;

import java.util.ArrayList;

/**
 * Created by Luciano José on 15/07/2017.
 */

public class AppointmentInfo {

    public String petId;
    public String petName;
    public String professionalId;
    public String professionalName;
    public ArrayList<BusinessServices> services;

    public AppointmentInfo(String petId, String petName, String professionalId, String professionalName, ArrayList<BusinessServices> services) {
        this.petId = petId;
        this.petName = petName;
        this.professionalId = professionalId;
        this.professionalName = professionalName;
        this.services = services;
    }
}
