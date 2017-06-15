package com.petbooking.Models;

/**
 * Created by Luciano José on 10/06/2017.
 */

public class CalendarItem {

    public int day;
    public int month;
    public int year;
    public String dayName;
    public String monthName;

    public CalendarItem(int day, int month, int year, String dayName, String monthName) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.dayName = dayName;
        this.monthName = monthName;
    }

}
