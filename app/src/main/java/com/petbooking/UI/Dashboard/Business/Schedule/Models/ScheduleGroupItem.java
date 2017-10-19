package com.petbooking.UI.Dashboard.Business.Schedule.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

public final class ScheduleGroupItem implements Parcelable {

    private String title;

    public ScheduleGroupItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //region - Parcelable

    protected ScheduleGroupItem(Parcel in) {
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

    public static final Parcelable.Creator<ScheduleGroupItem> CREATOR = new Parcelable.Creator<ScheduleGroupItem>() {

        @Override
        public ScheduleGroupItem createFromParcel(Parcel source) {
            return new ScheduleGroupItem(source);
        }

        @Override
        public ScheduleGroupItem[] newArray(int size) {
            return new ScheduleGroupItem[size];
        }
    };

    //endregion

}
