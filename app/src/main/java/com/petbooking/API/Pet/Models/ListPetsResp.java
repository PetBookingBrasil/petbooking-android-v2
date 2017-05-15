package com.petbooking.API.Pet.Models;

import java.util.List;

/**
 * Created by Luciano José on 30/04/2017.
 */
public class ListPetsResp {

    public List<Item> data;

    public static class Item {

        public String id;
        public String type;
        public PetResp.Attributes attributes;

    }

}
