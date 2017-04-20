package com.petbooking.API.Auth.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Luciano José on 11/04/2017.
 */

public class AuthConsumerResp {

    public Data data;

    public static class Data {

        public String id;
        public String type;
        public Attributes attributes;

    }

    public static class Attributes {

        public String token;
        public String version;

        @SerializedName("token_expires_at")
        public long tokenExpiresAt;

    }
}
