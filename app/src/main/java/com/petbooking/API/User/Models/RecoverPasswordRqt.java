package com.petbooking.API.User.Models;

import com.petbooking.Constants.APIConstants;

/**
 * Created by Luciano José on 16/04/2017.
 */

public class RecoverPasswordRqt {

    public Data data;
    public transient Attributes attributes;

    public RecoverPasswordRqt(String email) {
        this.attributes = new Attributes(email);
        this.data = new Data(attributes);
    }

    public static class Data {

        public String type;
        public Attributes attributes;

        public Data(Attributes attributes) {
            this.type = APIConstants.DATA_USERS;
            this.attributes = attributes;
        }

    }

    public static class Attributes {

        public String email;

        public Attributes() {
        }

        public Attributes(String email) {
            this.email = email;
        }

    }

}
