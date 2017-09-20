package com.petbooking.API.User.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Models.User;


public class CreateSocialUserRqt {

    public Data data;
    public transient Attributes attributes;

    public CreateSocialUserRqt(User user, String provider, String providerToken) {
        this.attributes = new Attributes(user.name, user.birthday, user.phone, user.cpf,
                user.email, user.password, user.zipcode, user.street, user.neighborhood,
                user.streetNumber, user.complement, user.city, user.state, provider, providerToken);
        this.data = new Data(attributes);
    }


    public static class Data {

        public String type = APIConstants.DATA_USERS;
        public Attributes attributes;

        public Data() {
        }

        public Data(Attributes attributes) {
            this.attributes = attributes;
        }

    }

    public static class Attributes {

        public String name;
        public String birthday;
        public String phone;
        public String cpf;
        public String email;
        public String zipcode;
        public String street;
        public String street_number;
        public String complement;
        public String neighborhood;
        public String password;
        public String city;
        public String state;
        public String provider;
        @SerializedName("provider_token")
        public String providerToken;

        public Attributes(String name, String birthday, String phone, String cpf, String email, String password,
                          String zipcode, String street, String neighborhood, String street_number, String complement,
                          String city, String state, String provider, String providerToken) {
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
            this.provider = provider;
            this.providerToken = providerToken;
        }
    }
}
