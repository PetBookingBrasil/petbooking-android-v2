package com.petbooking.UI.Dashboard.Business.Schedule.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

public final class ScheduleItem implements Parcelable {

    private String id;
    private String title;

    public ScheduleItem(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    //region - Parcelable

    protected ScheduleItem(Parcel in) {
        id = in.readString();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
    }

    public static final Parcelable.Creator<ScheduleItem> CREATOR = new Parcelable.Creator<ScheduleItem>() {

        @Override
        public ScheduleItem createFromParcel(Parcel source) {
            return new ScheduleItem(source);
        }

        @Override
        public ScheduleItem[] newArray(int size) {
            return new ScheduleItem[size];
        }
    };

    //endregion

}
