package com.petbooking.Models;

/**
 * Created by Luciano José on 28/05/2017.
 */

public class Category {

    public String id;
    public int categoryText;
    public String categoryName;
    public Integer icon;

    public Category(String id, int categoryText, String categoryName, Integer icon) {
        this.id = id;
        this.categoryText = categoryText;
        this.categoryName = categoryName;
        this.icon = icon;
    }

}
