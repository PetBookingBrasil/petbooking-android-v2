package com.petbooking.Models;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Luciano José on 08/08/2017.
 */

public class CartItem {

    public String id;
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
    public ArrayList<BusinessServices> additionalServices;

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
        this.additionalServices = new ArrayList<>();
        this.id = generateId();
    }

    public void addAdditional(BusinessServices additional) {
        additionalServices.add(additional);
    }

    public void removeAdditional(String additionalId) {
        int index = 0;
        for (BusinessServices additional : additionalServices) {
            if (additional.id.equals(additionalId)) {
                additionalServices.remove(index);
                break;
            }

            index++;
        }
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    private String generateId() {
        String id = UUID.randomUUID().toString();
        return id;
    }
}
