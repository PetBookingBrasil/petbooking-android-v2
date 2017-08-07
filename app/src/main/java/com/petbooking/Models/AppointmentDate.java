package com.petbooking.Models;

import com.petbooking.API.Appointment.Models.ProfessionalResp;

import java.util.ArrayList;

/**
 * Created by Luciano José on 07/08/2017.
 */

public class AppointmentDate {

    public String monthName;
    public ArrayList<ProfessionalResp.Slot> days;

    public AppointmentDate(String monthName) {
        this.monthName = monthName;
        this.days = new ArrayList<>();
    }
}
