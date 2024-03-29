package com.petbooking.Models;

import com.petbooking.API.Generic.AvatarResp;
import com.petbooking.Utils.CommonUtils;

/**
 * Created by Luciano José on 14/04/2017.
 */

public class User {

    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";

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
    public String complement;
    public String city;
    public String state;
    public String nickname;
    public String gender;
    public String cpf;
    public String password;
    public int searchRange;
    public boolean phoneActivated;
    public boolean acceptsSms;
    public boolean acceptsEmail;
    public boolean acceptsNotifications;
    public boolean acceptsTerms;
    public boolean validForScheduling;
    public AvatarResp avatar;
    public String providerToken;
    public String photo;

    public User() {
        this.avatar = new AvatarResp();
    }

    public User(String id, String authToken, String name, String birthday, String phone, boolean phoneActivated,
                String phoneCodeCreatedAt, String email, String futureEventsCount, boolean acceptsSms, String zipcode,
                String street, String neighborhood, String streetNumber, String complement, String city, String state,
                String nickname, String gender, String cpf, int searchRange, boolean acceptsEmail,
                boolean acceptsNotifications, boolean acceptsTerms, boolean validForScheduling, AvatarResp avatar) {
        this.id = id;
        this.authToken = authToken;
        this.name = name;
        this.birthday = CommonUtils.formatDate(birthday);
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
        this.complement = complement;
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

    public User(String id, int searchRange, boolean acceptsSms, boolean acceptsEmail, boolean acceptsNotifications) {
        this.id = id;
        this.searchRange = searchRange;
        this.acceptsSms = acceptsSms;
        this.acceptsEmail = acceptsEmail;
        this.acceptsNotifications = acceptsNotifications;
    }
}


