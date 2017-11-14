package com.petbooking.UI.Dashboard.Business.Schedule.Models;

import android.content.Context;

import com.petbooking.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

public final class ScheduleSection extends ExpandableGroup<ScheduleItem> {

    private Type type;

    public ScheduleSection(String title, Type type, List<ScheduleItem> items) {
        super(title, items);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public static String getTitle(Context context, Type type) {
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

    public enum Type {
        SELECT_PET,
        SERVICE_CATEGORY,
        ADDITIONAL_SERVICES,
        PROFESSIONAL,
        DAY_AND_TIME
    }

}
