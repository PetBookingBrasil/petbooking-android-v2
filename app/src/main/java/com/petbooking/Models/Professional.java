package com.petbooking.Models;

import com.petbooking.API.Appointment.Models.ProfessionalResp;

import java.util.ArrayList;

/**
 * Created by Luciano José on 06/08/2017.
 */

public class Professional {

    public String id;
    public String name;
    public String imageUrl;
    public ArrayList<ProfessionalResp.Slot> availableSlots = new ArrayList<>();

    public Professional(String id, String name, String imageUrl, ArrayList<ProfessionalResp.Slot> availableSlots) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.availableSlots = availableSlots;
    }
}
