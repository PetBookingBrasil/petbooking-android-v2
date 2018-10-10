package com.petbooking.API.Review;

import com.google.gson.annotations.SerializedName;
import com.petbooking.API.Pet.APIPetConstants;
import com.petbooking.API.Pet.Models.CreatePetRqt;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Models.Pet;

/**
 * Created by Victor on 03/04/18.
 */

public class ReviewRequest {
    public Data data;
    public transient Attributes attributes;

    public ReviewRequest(String comment,int bussinessRating, int employmentRating, int serviceRating, String id) {
        this.attributes = new Attributes(comment,bussinessRating,employmentRating,serviceRating,id);
        this.data = new Data(this.attributes);
    }

    public static class Data {
        public String type;
        public Attributes attributes;

        public Data(Attributes attributes) {
            this.type = APIConstants.DATA_REVIEW_TYPE_REVIEWS;
            this.attributes = attributes;

        }

    }

    public static class Attributes {

        public String comment;
        public int business_rating;
        public int employment_rating;
        public int service_rating;
        public String event_id;

        public Attributes(String comment, int business_rating, int employment_rating, int service_rating, String event_id) {
            this.comment = comment;
            this.business_rating = business_rating;
            this.employment_rating = employment_rating;
            this.service_rating = service_rating;
            this.event_id = event_id;
        }
    }

}

