package com.petbooking.API.User.Models;

import android.util.Log;

import com.petbooking.Constants.APIConstants;
import com.petbooking.Models.User;

public class CreateUserRqt {

    public Data data;
    public transient Attributes attributes;

    public CreateUserRqt(User user, boolean socialLogin) {
        if(!socialLogin) {

            this.attributes = new Attributes(user.name, user.phone, user.cpf,
                    user.email, user.password);
        }else {

            this.attributes = new Attributes(user.name, user.phone,
                    user.email);
        }
        this.data = new Data(attributes);
    }

    public static class Data {

        public String type = APIConstants.DATA_USERS;
        public Attributes attributes;

        public Data(Attributes attributes) {
            this.attributes = attributes;
        }
    }

    public static class Attributes {

        public String name;
        public String phone;
        //public String cpf;
        public String email;
        public String password;

        public Attributes(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.password = "null";
        }

        public Attributes(String name, String phone, String cpf, String email, String password) {
            this.name = name;
            this.phone = phone;
//            this.cpf = cpf;
            this.email = email;
            this.password = password;
        }
    }
}
