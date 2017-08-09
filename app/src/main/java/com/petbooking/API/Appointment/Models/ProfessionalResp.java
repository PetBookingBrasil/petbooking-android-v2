package com.petbooking.API.Appointment.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.API.Generic.AvatarResp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfessionalResp {

    public List<Item> data;

    public static class Item {

        public String id;
        public String type;
        public Attributes attributes;

    }

    public static class Attributes {

        public String name;
        public String nickname;
        public String slug;
        public AvatarResp.Avatar_ avatar;

        @SerializedName("available_slots")
        public ArrayList<Slot> availableSlots;
    }

    public static class Slot {
        public Date date;
        public ArrayList<String> times;
    }

}
