package com.petbooking.API.Business.Models;

import com.google.gson.annotations.SerializedName;
import com.petbooking.Constants.APIConstants;

public class FavoriteRqt {

    public Data data;

    public FavoriteRqt() {
    }

    public FavoriteRqt(String favorableType, String businessId) {
        this.data = new Data(new Attributes(favorableType, businessId));
    }

    public static class Data {

        public String type;
        public Attributes attributes;

        public Data() {
        }

        public Data(Attributes attributes) {
            this.type = APIConstants.DATA_FAVORITES;
            this.attributes = attributes;
        }

    }

    public static class Attributes {

        @SerializedName("favorable_type")
        public String favorableType;

        @SerializedName("favorable_id")
        public String favorableId;

        public Attributes() {
        }

        public Attributes(String favorableType, String favorableId) {
            this.favorableType = favorableType;
            this.favorableId = favorableId;
        }

    }

}
