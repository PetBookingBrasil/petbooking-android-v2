package com.petbooking.API.Auth.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Luciano José on 11/04/2017.
 */
public class SessionResp {

    public Data data;

    public static class Data {
        public String type;
        public Attributes attributes;
    }

    public static class Attributes {

        public String token;

        @SerializedName("user_id")
        public String userID;

        @SerializedName("user_valid_for_scheduling")
        public boolean userValid;

        @SerializedName("expires_at")
        public long expiresAt;

    }

}