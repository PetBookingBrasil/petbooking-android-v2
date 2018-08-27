package com.petbooking.UI.Dashboard.Business.Scheduling.model;

import com.petbooking.Models.CartItem;

/**
 * Created by victor on 21/02/18.
 */

public class ClearFieldsSchedule {
    public boolean clear;
    public CartItem cartItem;
    public int position;
    public ClearFieldsSchedule(boolean clear, CartItem cartItem, int position) {
        this.clear = clear;
        this.cartItem = cartItem;
        this.position = position;
    }
}
