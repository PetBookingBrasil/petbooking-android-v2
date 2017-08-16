package com.petbooking.Models;

import java.util.ArrayList;

/**
 * Created by Luciano José on 08/08/2017.
 */

public class CartItem {

    public String startDate;
    public String startTime;
    public String businessId;
    public BusinessServices service;
    public String categoryId;
    public Professional professional;
    public Pet pet;
    public String notes;
    public double totalPrice;
    public boolean withTransportation;
    public ArrayList<String> additionalServiceIds;

    public CartItem(String startDate, String startTime, String businessId, BusinessServices service,
                    String categoryId, Professional professional, Pet pet) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.businessId = businessId;
        this.service = service;
        this.categoryId = categoryId;
        this.professional = professional;
        this.pet = pet;
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
