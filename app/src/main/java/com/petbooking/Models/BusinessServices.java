package com.petbooking.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.petbooking.API.Generic.AvatarResp;

import java.util.ArrayList;

/**
 * Created by Luciano José on 27/05/2017.
 */

public class BusinessServices implements Parcelable{

    public String id;
    public String name;
    public String startTime;
    public String endTime;
    public int duration;
    public String description;
    public double price;
    public double maxPrice;
    public double minPrice;
    public String businessName;
    public String professionalName;
    public AvatarResp professionalAvatar;
    public ArrayList<BusinessServices> additionalServices;

    public BusinessServices(String id, String name, int duration, String description, double price) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.price = price;
        this.additionalServices = new ArrayList<>();
    }

    public BusinessServices(String id, String name, int duration, String description, double price, double maxPrice, double minPrice) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.price = price;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.additionalServices = new ArrayList<>();
    }

    public BusinessServices(String id, String name, String startTime, String endTime, int duration, String description,
                            double price, String businessName, String professionalName, AvatarResp professionalAvatar) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.description = description;
        this.price = price;
        this.businessName = businessName;
        this.professionalName = professionalName;
        this.professionalAvatar = professionalAvatar;
        this.additionalServices = new ArrayList<>();
    }

    protected BusinessServices(Parcel in) {
        id = in.readString();
        name = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        duration = in.readInt();
        description = in.readString();
        price = in.readDouble();
        maxPrice = in.readDouble();
        minPrice = in.readDouble();
        businessName = in.readString();
        professionalName = in.readString();
        additionalServices = in.createTypedArrayList(BusinessServices.CREATOR);
    }

    public static final Creator<BusinessServices> CREATOR = new Creator<BusinessServices>() {
        @Override
        public BusinessServices createFromParcel(Parcel in) {
            return new BusinessServices(in);
        }

        @Override
        public BusinessServices[] newArray(int size) {
            return new BusinessServices[size];
        }
    };

    public void setAdditionalServices(ArrayList<BusinessServices> additionalServices) {
        this.additionalServices = additionalServices;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeInt(duration);
        parcel.writeString(description);
        parcel.writeDouble(price);
        parcel.writeDouble(maxPrice);
        parcel.writeDouble(minPrice);
        parcel.writeString(businessName);
        parcel.writeString(professionalName);
        parcel.writeTypedList(additionalServices);
    }
}
