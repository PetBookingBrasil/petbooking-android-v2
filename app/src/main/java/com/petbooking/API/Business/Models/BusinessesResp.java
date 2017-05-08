package com.petbooking.API.Business.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ediposilva on 10/8/15.
 */
public class BusinessesResp {

    public List<Item> data;
    public Meta meta;

    public static class Item {

        public String id;
        public String type;
        public BusinessesRspAttributes attributes;

    }


    public static class Picture implements Parcelable, Serializable {

        public int id;
        public String caption;
        public Image image;


        @SerializedName("depicted_id")
        public String depictedId;


        @SerializedName("depicted_type")
        public String depictedType;

        public Picture() {
        }

        public Picture(String url) {
            this.image = new Image(url);
        }

        public Picture(Parcel in) {
            id = in.readInt();
            caption = in.readString();
            image = in.readParcelable(Image.class.getClassLoader());
            depictedId = in.readString();
            depictedType = in.readString();
        }

        public static final Creator<Picture> CREATOR = new Creator<Picture>() {
            @Override
            public Picture createFromParcel(Parcel in) {
                return new Picture(in);
            }

            @Override
            public Picture[] newArray(int size) {
                return new Picture[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(caption);
            dest.writeParcelable(image, flags);
            dest.writeString(depictedId);
            dest.writeString(depictedType);
        }

    }

    public static class Image implements Parcelable, Serializable {

        public String url;

        public Image() {
        }

        public Image(String url) {
            this.url = url;
        }

        Image(Parcel in) {
            url = in.readString();
        }

        public static final Creator<Image> CREATOR = new Creator<Image>() {
            @Override
            public Image createFromParcel(Parcel in) {
                return new Image(in);
            }

            @Override
            public Image[] newArray(int size) {
                return new Image[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(url);
        }

    }


    public static class Meta {

        @SerializedName("record_count")
        public Integer recordCount;

    }

}
