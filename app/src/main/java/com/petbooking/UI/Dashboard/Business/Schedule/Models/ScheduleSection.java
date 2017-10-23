package com.petbooking.UI.Dashboard.Business.Schedule.Models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

public final class ScheduleSection extends ExpandableGroup<ScheduleItem> {

    private boolean isSelected;
    private int number;

    public ScheduleSection(int number, String title, List<ScheduleItem> items) {
        super(title, items);
        setSelected(false);
        setNumber(number);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getNumber() {
        return number;
    }

    private void setNumber(int number) {
        this.number = number;
    }
}
