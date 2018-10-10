package com.petbooking.API.User.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Models.User;

public class UpdateUserRqt {

    public Data data;
    public transient Attributes attributes;

    public UpdateUserRqt(String id, User user) {
        this.attributes = new Attributes(user.name, user.phone, user.cpf,
                user.email, user.password, user.searchRange, user.acceptsEmail,
                user.acceptsNotifications, user.acceptsSms, user.photo);
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
        public String email;
        public String password;
        @SerializedName("search_range")
        public int searchRange;
        @SerializedName("accepts_email")
        public String acceptsEmail;
        @SerializedName("accepts_push")
        public String acceptsNotifications;
        @SerializedName("accepts_sms")
        public String acceptsSms;
        public String avatar;

        public Attributes(String name, String phone, String cpf, String email, String password,
                           int searchRange, boolean acceptsEmail,
                          boolean acceptsNotifications, boolean acceptsSms, String photo) {
            this.name = name;
            this.phone = phone;
            this.cpf = cpf;
            this.email = email;
            this.password = password;
            this.searchRange = searchRange;
            this.acceptsEmail = acceptsEmail ? "true" : "false";
            this.acceptsNotifications = acceptsNotifications ? "true" : "false";
            this.acceptsSms = acceptsSms ? "true" : "false";
            this.avatar = photo;
        }
    }
}
