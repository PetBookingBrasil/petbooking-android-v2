package com.petbooking.API.Auth.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.API.Generic.AvatarResp;

/**
 * Created by Luciano José on 11/04/2017.
 */
public class AuthUserResp {

    public Data data;

    public static class Data {

        public String id;
        public String type;
        public Attributes attributes;

    }

    public static class Attributes {

        @SerializedName("auth_token")
        public String authToken;

        public String name;
        public String birthday;
        public AvatarResp avatar;
        public String phone;

        @SerializedName("phone_activated")
        public boolean phoneActivated;

        @SerializedName("phone_code_created_at")
        public String phoneCodeCreatedAt;

        public String email;

        @SerializedName("future_events_count")
        public String futureEventsCount;

        @SerializedName("accepts_sms")
        public boolean acceptsSms;

        public String zipcode;
        public String street;
        public String neighborhood;

        @SerializedName("street_number")
        public String streetNumber;
        public String city;
        public String state;
        public String nickname;
        public String gender;
        public String cpf;

        @SerializedName("search_range")
        public String searchRange;

        @SerializedName("accepts_email")
        public boolean acceptsEmail;

        @SerializedName("accepts_push")
        public boolean acceptsNotifications;

        @SerializedName("accepts_terms")
        public boolean acceptsTerms;

        @SerializedName("valid_for_scheduling")
        public boolean validForScheduling;

    }

}