package com.petbooking.API.Auth.Models;

import com.petbooking.Constants.APIConstants;

public class AuthUserRqt {

    public Data data;
    public transient Attributes attributes;

    public AuthUserRqt(String email, String password) {
        this.attributes = new Attributes(email, password);
        this.data = new Data(attributes);
    }

    public static class Data {

        public String type;
        public Attributes attributes;

        public Data(Attributes attributes) {
            this.type = APIConstants.DATA_SESSIONS;
            this.attributes = attributes;
        }

    }

    public static class Attributes {

        public String provider;
        public String email;
        public String password;


        public Attributes(String email, String password) {
            this.provider = APIConstants.DATA_PROVIDER;
            this.email = email;
            this.password = password;
        }
    }

}
