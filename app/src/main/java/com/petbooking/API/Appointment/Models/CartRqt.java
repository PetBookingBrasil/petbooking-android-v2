package com.petbooking.API.Appointment.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.API.Appointment.APIAppointmentConstants;
import com.petbooking.Models.CartItem;
import com.petbooking.Utils.APIUtils;

import java.util.ArrayList;

/**
 * Created by Luciano José on 17/08/2017.
 */

public class CartRqt {

    public Data data;
    public transient Attributes attributes;

    public CartRqt(ArrayList<CartItem> items) {
        attributes = new Attributes(items);
        data = new Data(attributes);
    }

    public static class Data {
        String type = APIAppointmentConstants.DATA_CART_TYPE;
        Attributes attributes;

        public Data(Attributes attributes) {
            this.attributes = attributes;
        }
    }

    public static class Attributes {
        ArrayList<Item> items;

        public Attributes(ArrayList<CartItem> cartitems) {
            items = APIUtils.getCartReqItems(cartitems);
        }
    }

    public static class Item {
        @SerializedName("start_date")
        String startDate;
        @SerializedName("start_time")
        String startTime;
        @SerializedName("business_id")
        String businessId;
        @SerializedName("service_id")
        String serviceId;
        @SerializedName("professional_id")
        String professionalId;
        @SerializedName("pet_id")
        String petId;
        String notes;
        @SerializedName("with_transportation")
        boolean withTransportation;
        @SerializedName("additional_service_ids")
        ArrayList<String> additionalServiceIds;

        public Item(String startDate, String startTime, String businessId, String serviceId, String professionalId,
                    String petId, String notes, boolean withTransportation, ArrayList<String> additionalServiceIds) {
            this.startDate = startDate;
            this.startTime = startTime;
            this.businessId = businessId;
            this.serviceId = serviceId;
            this.professionalId = professionalId;
            this.petId = petId;
            this.notes = notes;
            this.withTransportation = withTransportation;
            this.additionalServiceIds = additionalServiceIds;
        }
    }

}
