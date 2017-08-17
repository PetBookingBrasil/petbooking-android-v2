package com.petbooking.Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Luciano José on 15/07/2017.
 */

public class Appointment {

    public Date date;
    public ArrayList<AppointmentInfo> appointmentsInfo;

    public Appointment(Date date, ArrayList<AppointmentInfo> appointmentsInfo) {
        this.date = date;
        this.appointmentsInfo = appointmentsInfo;
    }
}
