package com.petbooking.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.CartItem;

import java.util.ArrayList;
import java.util.Map;

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
        this.cart = getCart();
        this.cart.add(item);

        if (!isServiceSelected(item.service.id, item.pet.id)) {
            incrementPetAppointment(item.pet.id);
            incrementCategoryAppointment(item.categoryId, item.pet.id);
            incrementTotalAppointments();
        }

        setServiceSelected(item.service.id, item.pet.id, true);
        saveItem(item);
        saveCart(cart);
        clearAdditional(item.service.id);

        if (item.additionalServices.size() != 0) {
            for (BusinessServices additional : item.additionalServices) {
                setAdditionalSelected(additional.id, item.pet.id, true);
            }
        }
    }

    public void removeItem(String serviceId, String categoryKey, String petId) {
        removeKey(serviceId + "_" + petId + "_ITEM");
        setServiceSelected(serviceId, petId, false);
        removeCartItem(serviceId);
        saveCart(cart);
        clearAdditional(serviceId);
        decreasePetAppointment(petId);
        decreaseCategoryAppointment(categoryKey, petId);
        decreaseTotalAppointments();
    }

    public void removeCartItem(String serviceId) {
        int index = 0;
        this.cart = getCart();
        for (CartItem cartItem : this.cart) {
            if (cartItem.service.id.equals(serviceId)) {
                this.cart.remove(index);
                break;
            }

            index++;
        }
    }

    public void removeItemByIndex(int index) {
        cart = getCart();
        CartItem item = cart.get(index);

        cart.remove(index);

        setServiceSelected(item.service.id, item.pet.id, false);
        clearAdditional(item.service.id);
        removeKey(item.service.id + "_" + item.pet.id + "_ITEM");

        decreasePetAppointment(item.pet.id);
        decreaseCategoryAppointment(item.categoryId, item.pet.id);
        decreaseTotalAppointments();
        saveCart(cart);
    }

    public CartItem getItem(String serviceId, String petId) {
        if (pref.getString(serviceId + "_" + petId + "_ITEM", "").equals("")) {
            return null;
        }

        return mJsonManager.fromJson(pref.getString(serviceId + "_" + petId + "_ITEM", ""), CartItem.class);
    }

    public void addNewAdditional(String serviceId, BusinessServices additional, String petId) {
        cart = getCart();
        CartItem item = getCartItem(serviceId, petId);

        item.addAdditional(additional);
        item.totalPrice += additional.price;

        setAdditionalSelected(additional.id, petId, true);
        saveItem(item);
        saveCart(cart);
    }

    public void removeAdditional(String serviceId, BusinessServices additional, String petId) {
        CartItem item = getItem(serviceId, petId);
        item.removeAdditional(additional.id);
        item.totalPrice -= additional.price;
        setAdditionalSelected(additional.id, petId, false);
        saveItem(item);
        saveCart(cart);
    }

    public void removeAdditionalByIndex(int parentPosition, int additionalPosition) {
        cart = getCart();
        CartItem item = cart.get(parentPosition);
        BusinessServices additional = item.additionalServices.get(additionalPosition);

        item.totalPrice -= additional.price;
        setAdditionalSelected(additional.id, item.pet.id, false);
        item.additionalServices.remove(additionalPosition);

        saveItem(item);
        saveCart(cart);


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

    private void decreaseTotalAppointments() {
        decreaseKey("TOTAL_APPOINTMENTS");
    }

    private void decreasePetAppointment(String petId) {
        decreaseKey(petId + "_SERVICE");
    }

    private void decreaseCategoryAppointment(String categoryId, String petId) {
        decreaseKey(categoryId + "_" + petId + "_TOTAL");
    }

    private void setServiceSelected(String serviceId, String petId, boolean status) {
        editor.putBoolean(serviceId + "_" + petId, status);
        editor.apply();
    }

    private void setAdditionalSelected(String serviceId, String petId, boolean status) {
        editor.putBoolean("ADDITIONAL_" + serviceId + "_" + petId, status);
        editor.apply();
    }

    private void incrementKey(String key) {
        int total = pref.getInt(key, 0);
        total++;
        editor.putInt(key, total);
        editor.apply();
    }

    private void decreaseKey(String key) {
        int total = pref.getInt(key, 0);
        total--;
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

    public boolean isAdditionalSelected(String serviceId, String petId) {
        return pref.getBoolean("ADDITIONAL_" + serviceId + "_" + petId, false);
    }

    public void clearAdditional(String serviceId) {
        Map<String, ?> allEntries = pref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.contains("ADDITIONAL_" + serviceId)) {
                editor.remove(key);
            }
            editor.commit();
        }
    }

    private void saveCart(ArrayList<CartItem> cart) {
        editor.putString("CART", mJsonManager.toJson(cart));
        editor.apply();
    }

    private void saveItem(CartItem item) {
        editor.putString(item.service.id + "_" + item.pet.id + "_ITEM", mJsonManager.toJson(item));
        editor.apply();
    }

    public void saveCartId(String cartId) {
        editor.putString("CART_ID", mJsonManager.toJson(cartId));
        editor.apply();
    }

    public CartItem getCartItem(String serviceId, String petId) {
        int index = 0;

        for (CartItem item : cart) {
            if (item.service.id.equals(serviceId) && item.pet.id.equals(petId)) {
                return cart.get(index);
            }

            index++;
        }

        return null;
    }

    public int getCountCartPetId(String petId){
        int i = 0;

        ArrayList<CartItem> cart = new ArrayList<>();
        String cartStr = pref.getString("CART", null);

        if (cartStr != null) {
            cart = mJsonManager.fromJson(cartStr, new TypeToken<ArrayList<CartItem>>() {
            }.getType());
        }
            for (CartItem item : cart) {
                if (item.pet.id.equals(petId)) {
                    i++;
                }

        }
        return i;

    }

    public ArrayList<CartItem> getCart() {
        ArrayList<CartItem> cart = new ArrayList<>();
        String cartStr = pref.getString("CART", null);

        if (cartStr != null) {
            cart = mJsonManager.fromJson(cartStr, new TypeToken<ArrayList<CartItem>>() {
            }.getType());
        }

        return cart;
    }

    public String getCartId() {
       return pref.getString("CART_ID", "");
    }

    public AppointmentManager removeKey(String key) {
        pref.edit().remove(key).apply();
        return this;
    }

    public void reset() {
        this.cart = new ArrayList<>();
        editor.clear();
        editor.commit();
    }
}
