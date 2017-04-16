package com.petbooking.API.Auth.Models;

import com.petbooking.API.Auth.APIAuthConstants;

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
            this.type = APIAuthConstants.DATA_SESSIONS;
            this.attributes = attributes;
        }

    }

    public static class Attributes {

        public String provider;
        public String email;
        public String password;


        public Attributes(String email, String password) {
            this.provider = APIAuthConstants.DATA_PROVIDER;
            this.email = email;
            this.password = password;
        }
    }

}
