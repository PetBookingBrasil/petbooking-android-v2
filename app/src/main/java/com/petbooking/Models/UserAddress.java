package com.petbooking.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Luciano José on 23/04/2017.
 */

public class UserAddress {


    @SerializedName("cep")
    public String zipcode;

    @SerializedName("localidade")
    public String city;

    @SerializedName("logradouro")
    public String street;

    @SerializedName("bairro")
    public String neighborhood;

    @SerializedName("uf")
    public String state;

    public String streetNumber;

    public UserAddress(String zipcode, String city, String street, String neighborhood, String state, String streetNumber) {
        this.zipcode = zipcode;
        this.city = city;
        this.street = street;
        this.neighborhood = neighborhood;
        this.state = state.toUpperCase();
        this.streetNumber = streetNumber;
    }
}
