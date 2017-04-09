package com.petbooking.Models;

import com.petbooking.API.Auth.APIAuthConstants;
import com.petbooking.Constants.APIConstants;

/**
 * Created by Luciano José on 09/04/2017.
 */

public class ConsumerRqt {

    public Data data;
    public transient Attributes attributes;

    public ConsumerRqt() {
        this.attributes = new Attributes();
        this.data = new Data(attributes);
    }

    public class Data {
        public String type;
        public Attributes attributes;

        public Data(Attributes attributes) {
            this.type = APIAuthConstants.DATA_CONSUMERS;
            this.attributes = attributes;
        }
    }

    public class Attributes {
        public String uuid;

        public Attributes() {
            uuid = APIConstants.UUID_PRODUCTION;
            //uuid = APIConstants.UUID_BETA;
        }
    }

}


