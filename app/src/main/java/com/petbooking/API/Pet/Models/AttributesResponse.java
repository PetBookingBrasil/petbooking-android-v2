package com.petbooking.API.Pet.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Victor on 05/02/18.
 */

public class AttributesResponse {
    public Item data;

    public static class Item {
        public Attributes attributes;

    }

    public class Attributes {

        @SerializedName("genders")
        @Expose
        public List<String> genders;
        public List<String> sizes;
        public List<String> coat_types;
        public HashMap<String,Integer> coat_colors;

    }

    public class ColorPet {
        String name;
        int position;
    }
}
