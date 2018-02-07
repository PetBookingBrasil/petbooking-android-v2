package com.petbooking.API.Pet.Models;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Victor on 05/02/18.
 */

public class AttributesResponse {
    public List<BreedResp.Item> data;

    public static class Item {


        public Attributes attributes;

    }

    public static class Attributes {

        public List<String> genders;
        public List<String> sizes;
        public List<String> coat_types;
        public HashMap<String,Integer> coat_colors;

    }
}
