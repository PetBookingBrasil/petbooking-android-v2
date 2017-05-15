package com.petbooking.API.User.Models;

import com.petbooking.Constants.APIConstants;
import com.petbooking.Models.User;

public class CreateUserRqt {

    public Data data;
    public transient Attributes attributes;

    public CreateUserRqt(User user) {
        this.attributes = new Attributes(user.name, user.birthday, user.phone, user.cpf,
                user.email, user.password, user.zipcode, user.street, user.neighborhood,
                user.streetNumber, user.city, user.state, user.photo);
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
        public String cpf;
        public String birthday;
        public String email;
        public String password;
        public String zipcode;
        public String street;
        public String neighborhood;
        public String street_number;
        public String city;
        public String state;
        public String avatar;

        public Attributes(String name, String birthday, String phone, String cpf, String email, String password,
                          String zipcode, String street, String neighborhood, String street_number,
                          String city, String state, String photo) {
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
            this.city = city;
            this.state = state;
            this.avatar = photo;
        }
    }
}
