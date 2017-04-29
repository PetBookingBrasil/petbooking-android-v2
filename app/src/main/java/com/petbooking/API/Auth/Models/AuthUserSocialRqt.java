package com.petbooking.API.Auth.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.Constants.APIConstants;

/**
 * Created by Luciano José on 27/04/2017.
 */

public class AuthUserSocialRqt {

    public transient Attributes attributes;
    public Data data;

    public AuthUserSocialRqt(String provider, String providerToken) {
        this.attributes = new Attributes(provider, providerToken);
        this.data = new Data(attributes);
    }

    public static class Data {

        public String type = APIConstants.DATA_SESSIONS;
        public Attributes attributes;


        public Data(Attributes attributes) {
            this.attributes = attributes;
        }
    }

    public static class Attributes {

        public String provider;

        @SerializedName("provider_token")
        public String providerToken;

        public Attributes() {
        }

        public Attributes(String provider, String providerToken) {
            this.provider = provider;
            this.providerToken = providerToken;
        }
    }
}
