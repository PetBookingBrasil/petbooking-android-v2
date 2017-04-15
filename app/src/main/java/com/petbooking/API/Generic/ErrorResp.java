package com.petbooking.API.Generic;

import java.util.List;

public class ErrorResp {

    public List<Error> errors;

    public static class Error {

        public String id;
        public int code;
        public String status;
        public String title;
        public Object detail;

    }

}
