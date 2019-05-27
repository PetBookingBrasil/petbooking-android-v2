package com.petbooking.API.Business.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerResponse {

    public List<Item> data;


    public static class Attributes {

        public String title;
        public String image_url;
        public String discount_type;
        public String discount_amount;
        public String start_date;
        public String end_date;

    }

    public static class Item {

        public String id;
        public String type;
        public Attributes attributes;

    }
}
