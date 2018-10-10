package com.petbooking.Models;

import com.petbooking.UI.Dashboard.Business.Scheduling.model.AppointmentDateSlot;

import java.util.ArrayList;

public class Scheduling {
    private int petSelected;
    private int categorySelected;
    private int professionalSelected;
    private int bussinesSelected;
    private int dateSlotselected;
    private int timerSelected;


    public Scheduling() {
    }

    public int getPetSelected() {
        return petSelected;
    }

    public void setPetSelected(int petSelected) {
        this.petSelected = petSelected;
    }

    public int getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(int categorySelected) {
        this.categorySelected = categorySelected;
    }

    public int getProfessionalSelected() {
        return professionalSelected;
    }

    public void setProfessionalSelected(int professionalSelected) {
        this.professionalSelected = professionalSelected;
    }

    public int getBussinesSelected() {
        return bussinesSelected;
    }

    public void setBussinesSelected(int bussinesSelected) {
        this.bussinesSelected = bussinesSelected;
    }

    public int getDateSlotselected() {
        return dateSlotselected;
    }

    public void setDateSlotselected(int dateSlotselected) {
        this.dateSlotselected = dateSlotselected;
    }

    public int getTimerSelected() {
        return timerSelected;
    }

    public void setTimerSelected(int timerSelected) {
        this.timerSelected = timerSelected;
    }
}
