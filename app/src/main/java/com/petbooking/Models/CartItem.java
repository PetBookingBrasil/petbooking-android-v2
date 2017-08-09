package com.petbooking.Models;

import java.util.ArrayList;

/**
 * Created by Luciano José on 08/08/2017.
 */

public class CartItem {

    public String startDate;
    public String startTime;
    public String businessId;
    public String serviceId;
    public String professionalId;
    public String petId;
    public String notes;
    public boolean withTransportation;
    public ArrayList<String> additionalServiceIds;

    public CartItem(String startDate, String startTime, String businessId, String serviceId, String professionalId, String petId) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.businessId = businessId;
        this.serviceId = serviceId;
        this.professionalId = professionalId;
        this.petId = petId;
        this.withTransportation = false;
        this.additionalServiceIds = new ArrayList<>();
    }

    public void addAdditional(String additionalId) {
        this.additionalServiceIds.add(additionalId);
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
