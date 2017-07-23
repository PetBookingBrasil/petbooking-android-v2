package com.petbooking.Models;

import java.util.Date;

/**
 * Created by Luciano José on 10/06/2017.
 */

public class CalendarItem {

    public Date date;
    public int day;
    public int month;
    public int year;
    public String dayName;
    public String monthName;

    public CalendarItem(Date date, int day, int month, int year, String dayName, String monthName) {
        this.date = date;
        this.day = day;
        this.month = month;
        this.year = year;
        this.dayName = dayName;
        this.monthName = monthName;
    }

}
