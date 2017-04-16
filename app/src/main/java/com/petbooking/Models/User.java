package com.petbooking.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.API.Generic.AvatarResp;

/**
 * Created by Luciano José on 14/04/2017.
 */

public class User {

    public String id;
    public String authToken;
    public String name;
    public String birthday;
    public String phone;
    public String phoneCodeCreatedAt;
    public String email;
    public String futureEventsCount;
    public String zipcode;
    public String street;
    public String neighborhood;
    public String streetNumber;
    public String city;
    public String state;
    public String nickname;
    public String gender;
    public String cpf;
    public String searchRange;
    public boolean phoneActivated;
    public boolean acceptsSms;
    public boolean acceptsEmail;
    public boolean acceptsNotifications;
    public boolean acceptsTerms;
    public boolean validForScheduling;
    public AvatarResp avatar;

    public User(String id, String authToken, String name, String birthday, String phone, boolean phoneActivated,
                String phoneCodeCreatedAt, String email, String futureEventsCount, boolean acceptsSms, String zipcode,
                String street, String neighborhood, String streetNumber, String city, String state, String nickname,
                String gender, String cpf, String searchRange, boolean acceptsEmail, boolean acceptsNotifications,
                boolean acceptsTerms, boolean validForScheduling, AvatarResp avatar) {
        this.id = id;
        this.authToken = authToken;
        this.name = name;
        this.birthday = birthday;
        this.phone = phone;
        this.phoneActivated = phoneActivated;
        this.phoneCodeCreatedAt = phoneCodeCreatedAt;
        this.email = email;
        this.futureEventsCount = futureEventsCount;
        this.acceptsSms = acceptsSms;
        this.zipcode = zipcode;
        this.street = street;
        this.neighborhood = neighborhood;
        this.streetNumber = streetNumber;
        this.city = city;
        this.state = state;
        this.nickname = nickname;
        this.gender = gender;
        this.cpf = cpf;
        this.searchRange = searchRange;
        this.acceptsEmail = acceptsEmail;
        this.acceptsNotifications = acceptsNotifications;
        this.acceptsTerms = acceptsTerms;
        this.validForScheduling = validForScheduling;
        this.avatar = avatar;
    }
}
