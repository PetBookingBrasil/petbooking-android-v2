package com.petbooking.Models;

import android.graphics.drawable.Drawable;

/**
 * Created by Luciano José on 28/05/2017.
 */

public class Category {

    public String id;
    public int categoryText;
    public String categoryName;
    public Drawable icon;

    public Category(String id, int categoryText, String categoryName, Drawable icon) {
        this.id = id;
        this.categoryText = categoryText;
        this.categoryName = categoryName;
        this.icon = icon;
    }

}
