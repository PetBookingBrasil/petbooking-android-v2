package com.petbooking.Managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Models.CartItem;

import java.util.ArrayList;

/**
 * Created by Luciano José on 08/08/2017.
 */

public class AppointmentManager {

    private static Gson mJsonManager;
    private ArrayList<CartItem> cart;
    private static AppointmentManager mInstance;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public static AppointmentManager getInstance() {
        if (mInstance == null) {
            mInstance = new AppointmentManager();
        }

        return mInstance;
    }

    public void initialize(Context context) {
        pref = context.getSharedPreferences(AppConstants.PREF_APPOINTMENT_NAME, AppConstants.PREF_PRIVATE_MODE);
        editor = pref.edit();
        mJsonManager = new Gson();
        cart = new ArrayList<>();
    }

    public void addItem(CartItem item) {
        this.cart.add(item);
        setServiceSelected(item.service.id, item.pet.id);
        saveItem(item);
        incrementPetAppointment(item.pet.id);
        incrementCategoryAppointment(item.categoryId, item.pet.id);
        incrementTotalAppointments();
    }

    private void saveItem(CartItem item) {
        editor.putString(item.service.id + "_" + item.pet.id + "_ITEM", mJsonManager.toJson(item));
        editor.apply();
    }

    public CartItem getItem(String serviceId, String petId) {
        if (pref.getString(serviceId + "_" + petId + "_ITEM", "").equals("")) {
            return null;
        }

        return mJsonManager.fromJson(pref.getString(serviceId + "_" + petId + "_ITEM", ""), CartItem.class);
    }

    public void setCurrentBusinessId(String businessId) {
        editor.putString("businessId", businessId);
        editor.apply();
    }

    public void setCurrentBusinessName(String businessName) {
        editor.putString("businessName", businessName);
        editor.apply();
    }

    public void setCurrentBusinessDistance(float businessDistance) {
        editor.putFloat("businessDistance", businessDistance);
        editor.apply();
    }

    public String getCurrentBusinessId() {
        return pref.getString("businessId", "");
    }

    public String getCurrentBusinessName() {
        return pref.getString("businessName", "");
    }

    public float getCurrentBusinessDistance() {
        return pref.getFloat("businessDistance", 0);
    }

    private void incrementTotalAppointments() {
        incrementKey("TOTAL_APPOINTMENTS");
    }

    private void incrementPetAppointment(String petId) {
        incrementKey(petId + "_SERVICE");
    }

    private void incrementCategoryAppointment(String categoryId, String petId) {
        incrementKey(categoryId + "_" + petId + "_TOTAL");
    }

    private void setServiceSelected(String serviceId, String petId) {
        editor.putBoolean(serviceId + "_" + petId, true);
        editor.apply();
    }

    private void incrementKey(String key) {
        int total = pref.getInt(key, 0);
        total++;
        editor.putInt(key, total);
        editor.apply();
    }

    public int getTotalAppointments() {
        return pref.getInt("TOTAL_APPOINTMENTS", 0);
    }

    public int getTotalPetAppointments(String petId) {
        return pref.getInt(petId + "_SERVICE", 0);
    }

    public int getTotalCategoryAppointments(String categoryId, String petId) {
        return pref.getInt(categoryId + "_" + petId + "_TOTAL", 0);
    }

    public boolean isServiceSelected(String serviceId, String petId) {
        return pref.getBoolean(serviceId + "_" + petId, false);
    }

    public AppointmentManager removeKey(String key) {
        pref.edit().remove(key).apply();
        return this;
    }

    public void reset() {
        editor.clear();
        editor.commit();
    }
}
