package com.petbooking.API.Business.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.API.Generic.AvatarResp;

import java.util.List;

/**
 * Created by Victor on 02/04/18.
 */

public class ReviewableResp {
    public List<Item> data;
    public List<Included> included;

    public static class Item {

        public String id;
        public String type;
        public Attributes attributes;


    }

    public static class Attributes {

        public String service_name;
        public String professional_name;
        public String pet_id;
        public String date;

    }

    public static class Included {
        public String id;
        public String type;
        public AttributesIncluded attributes;
    }

    public static class AttributesIncluded {
        public String name;
        public AvatarResp avatar;

    }
}

