package com.petbooking.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victor on 02/04/18.
 */

public class ReviewServices implements Parcelable {
    public String petName;
    public String serviceName;
    public String bussinesName;
    public String professionalName;
    public String professionalAvatar;
    public String date;
    public String id;

    public ReviewServices(String petName, String serviceName, String bussinesName, String professionalName, String professionalAvatar, String date,String id) {
        this.petName = petName;
        this.serviceName = serviceName;
        this.bussinesName = bussinesName;
        this.professionalName = professionalName;
        this.professionalAvatar = professionalAvatar;
        this.date = date;
        this.id = id;
    }

    protected ReviewServices(Parcel in) {
        petName = in.readString();
        serviceName = in.readString();
        bussinesName = in.readString();
        professionalName = in.readString();
        professionalAvatar = in.readString();
        date = in.readString();
        id = in.readString();
    }

    public static final Creator<ReviewServices> CREATOR = new Creator<ReviewServices>() {
        @Override
        public ReviewServices createFromParcel(Parcel in) {
            return new ReviewServices(in);
        }

        @Override
        public ReviewServices[] newArray(int size) {
            return new ReviewServices[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(petName);
        parcel.writeString(serviceName);
        parcel.writeString(bussinesName);
        parcel.writeString(professionalName);
        parcel.writeString(professionalAvatar);
        parcel.writeString(date);
        parcel.writeString(id);
    }
}
