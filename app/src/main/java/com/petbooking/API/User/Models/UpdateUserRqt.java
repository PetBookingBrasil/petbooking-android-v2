package com.petbooking.API.User.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Models.User;

public class UpdateUserRqt {

    public Data data;
    public transient Attributes attributes;

    public UpdateUserRqt(String id, User user) {
        this.attributes = new Attributes(user.name, user.birthday, user.phone, user.cpf,
                user.email, user.password, user.zipcode, user.street, user.neighborhood,
                user.streetNumber, user.complement, user.city, user.state, user.searchRange, user.acceptsEmail,
                user.acceptsNotifications, user.acceptsSms);
        this.data = new Data(id, attributes);
    }

    public static class Data {

        public String id;
        public String type = APIConstants.DATA_USERS;
        public Attributes attributes;

        public Data(String id, Attributes attributes) {
            this.id = id;
            this.attributes = attributes;
        }
    }

    public static class Attributes {

        public String name;
        public String phone;
        public String cpf;
        public String birthday;
        public String email;
        public String password;
        public String zipcode;
        public String street;
        public String neighborhood;
        public String street_number;
        public String complement;
        public String city;
        public String state;
        @SerializedName("search_range")
        public int searchRange;
        @SerializedName("accepts_email")
        public String acceptsEmail;
        @SerializedName("accepts_push")
        public String acceptsNotifications;
        @SerializedName("accepts_sms")
        public String acceptsSms;

        public Attributes(String name, String birthday, String phone, String cpf, String email, String password,
                          String zipcode, String street, String neighborhood, String street_number, String complement,
                          String city, String state, int searchRange, boolean acceptsEmail,
                          boolean acceptsNotifications, boolean acceptsSms) {
            this.name = name;
            this.birthday = birthday;
            this.phone = phone;
            this.cpf = cpf;
            this.email = email;
            this.password = password;
            this.zipcode = zipcode;
            this.street = street;
            this.neighborhood = neighborhood;
            this.street_number = street_number;
            this.complement = complement;
            this.city = city;
            this.state = state;
            this.searchRange = searchRange;
            this.acceptsEmail = acceptsEmail ? "true" : "false";
            this.acceptsNotifications = acceptsNotifications ? "true" : "false";
            this.acceptsSms = acceptsSms ? "true" : "false";
        }
    }
}
