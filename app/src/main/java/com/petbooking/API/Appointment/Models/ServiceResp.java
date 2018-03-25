package com.petbooking.API.Appointment.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ediposilva on 9/29/15.
 */
public class ServiceResp {

    public List<Item> data;

    public static class Item {

        public String id;
        public String type;
        public Attributes attributes;

    }

    public static class Additional {

        public String id;
        public String name;
        public String slug;
        public String description;
        public int duration;
        public float price;
    }

    public static class Attributes {

        public String name;
        public String slug;
        public String description;
        public int duration;
        public Price price;

        @SerializedName("price_range")
        public Price priceRange;

        //@SerializedName("service_price")

        @SerializedName("childs")
        public List<Additional> additionalServices;

        public Attributes() {
        }

        Attributes(String name, String slug, String description, int duration, Price price,
                   Price priceRange) {
            this.name = name;
            this.slug = slug;
            this.description = description;
            this.duration = duration;
            this.price = price;
            this.priceRange = priceRange;
        }

        public static class Price {
            public float min_service_price;
            public float max_service_price;
            public float service_price;
        }
    }

}
