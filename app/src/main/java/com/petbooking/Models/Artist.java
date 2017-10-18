package com.petbooking.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

public final class Artist implements Parcelable {

    private String title;

    public Artist(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //region - Parcelable

    protected Artist(Parcel in) {
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {

        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    //endregion

}
