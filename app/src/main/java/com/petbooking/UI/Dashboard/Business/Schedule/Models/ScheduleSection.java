package com.petbooking.UI.Dashboard.Business.Schedule.Models;

import android.content.Context;

import com.petbooking.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

public final class ScheduleSection extends ExpandableGroup<ScheduleItem> {

    private String id;
    private String title;
    private Type type;

    public ScheduleSection(Context context, Type type) {
        this(context, type, null);
    }

    public ScheduleSection(Context context, Type type, List<ScheduleItem> items) {
        super(ScheduleSection.getTitle(context, type), items);
        this.type = type;
    }

    //region - Static

    private static String getTitle(Context context, Type type) {
        switch (type) {
            case SELECT_PET:
                return context.getString(R.string.select_pet);
            case SERVICE_CATEGORY:
                return context.getString(R.string.service_category);
            case ADDITIONAL_SERVICES:
                return context.getString(R.string.additional_services);
            case PROFESSIONAL:
                return context.getString(R.string.professional_label);
            case DAY_AND_TIME:
                return context.getString(R.string.day_and_time);
            default:
                return null;
        }
    }

    //endregion

    //region - Public

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        if (title != null) {
            return title;
        }

        return super.getTitle();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }



    public Type getType() {
        return type;
    }

    //endregion

    //region - Enum

    public enum Type {
        SELECT_PET(0),
        SERVICE_CATEGORY(1),
        ADDITIONAL_SERVICES(2),
        PROFESSIONAL(3),
        DAY_AND_TIME(4);

        private int mValue;

        Type(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }

    }

    //endregion

}
