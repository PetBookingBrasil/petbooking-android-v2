package com.petbooking.API.Generic;

/**
 * Created by Luciano José on 28/04/2017.
 */

public class APIError {

    public String title;
    public String detail;
    public int code;
    public String status;

    public APIError(String title, String detail, int code, String status) {
        this.title = title;
        this.detail = detail;
        this.code = code;
        this.status = status;
    }
}
