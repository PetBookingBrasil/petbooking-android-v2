package com.petbooking.API.User.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.Models.BusinessServices;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Luciano José on 22/07/2017.
 */

public class ScheduleResp {

    public ArrayList<Schedule> data;

    public static class Schedule {
        public Date date;
        public ArrayList<PetSchedule> pets;
    }

    public static class PetSchedule {
        public String id;
        public String name;
        public ArrayList<Event> events;
    }

    public static class Event {
        public String id;
        @SerializedName("starts_at")
        public String startTime;
        @SerializedName("ends_at")
        public String endTime;
        public String duration;
        @SerializedName("business_name")
        public String businessName;
        @SerializedName("employment_name")
        public String professionalName;
        public Service service;
    }

    public static class Service {
        public String id;
        public String name;
        public double price;
        @SerializedName("additional_services")
        public ArrayList<BusinessServices> additionalServices;
    }

}