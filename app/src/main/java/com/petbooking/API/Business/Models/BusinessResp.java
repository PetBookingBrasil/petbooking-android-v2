package com.petbooking.API.Business.Models;

import java.util.List;

/**
 * Created by Luciano José on 21/05/2017.
 */

public class BusinessResp {

    public BusinessesResp.Item data;

    public static class Item {

        public String id;
        public String type;
        public BusinessesRspAttributes attributes;

    }


}
