package com.petbooking.Models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

public final class Genre extends ExpandableGroup<Artist> {

    public Genre(String title, List<Artist> items) {
        super(title, items);
    }

}
