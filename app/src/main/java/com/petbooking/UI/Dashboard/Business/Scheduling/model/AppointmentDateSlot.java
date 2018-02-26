package com.petbooking.UI.Dashboard.Business.Scheduling.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Victor on 25/02/18.
 */

public class AppointmentDateSlot {
    public int position;
    public Date date;
    public ArrayList<String> timer;

    public AppointmentDateSlot(Date date, ArrayList<String> timer) {
        this.date = date;
        this.timer = timer;

    }
}
