package com.petbooking.UI.Dashboard.Business.Schedule.Models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

public final class ScheduleGroup extends ExpandableGroup<ScheduleGroupItem> {

    public ScheduleGroup(String title, List<ScheduleGroupItem> items) {
        super(title, items);
    }

}
