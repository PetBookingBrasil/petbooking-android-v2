package com.petbooking.API.User.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Models.User;


public class CreateSocialUserRqt {

    public Data data;
    public transient Attributes attributes;

    public CreateSocialUserRqt(User user, String provider, String providerToken) {
        this.attributes = new Attributes(user.name, user.phone,
                user.email, provider, providerToken);
        this.data = new Data(attributes);
    }


    public static class Data {

        public Attributes attributes;
        public String type = APIConstants.DATA_USERS;

        public Data() {
        }

        public Data(Attributes attributes) {
            this.attributes = attributes;
        }

    }

    public static class Attributes {

//        public String name;
//        public String phone;
//        public String email;
        public String provider;
        @SerializedName("provider_token")
        public String providerToken;

        public Attributes(String name, String phone, String email, String provider, String providerToken) {
//            this.name = name;
//            this.phone = phone;
//            this.email = email;
            this.provider = provider;
            this.providerToken = providerToken;
        }
    }
}
