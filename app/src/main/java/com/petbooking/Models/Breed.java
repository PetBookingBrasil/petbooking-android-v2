package com.petbooking.Models;

/**
 * Created by Luciano José on 01/05/2017.
 */

public class Breed {

    public String id;
    public String name;
    public String kind;
    public String size;

    public Breed(String id, String name, String kind, String size) {
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.size = size;
    }
}
