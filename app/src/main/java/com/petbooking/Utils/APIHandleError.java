package com.petbooking.Utils;

import com.petbooking.API.Generic.ErrorResp;
import com.petbooking.R;

/**
 * Created by Luciano José on 14/04/2017.
 */

public class APIHandleError {
    private static final String ERROR_STATUS_UNAUTHORIZED = "401";

    private static final int ERROR_CODE_INVALID_LOGIN = 124;
    private static final int ERROR_CODE_OBJECT_REQUEST = 125;
    private static final int ERROR_CODE_INVALID_AUTH_TOKEN = 126;
    private static final int ERROR_CODE_INVALID_USER_TOKEN = 127;

    public static int getErrorMessage(ErrorResp errorRsp) {
        if (errorRsp.errors != null && errorRsp.errors.size() > 0) {
            ErrorResp.Error firstError = errorRsp.errors.get(0);
            switch (firstError.status) {
                case ERROR_STATUS_UNAUTHORIZED:
                    return handlerUnauthorizedError(firstError.code);
            }
        }
        return -1;
    }

    private static int handlerUnauthorizedError(int code) {
        switch (code) {
            case ERROR_CODE_INVALID_AUTH_TOKEN:
                return R.string.error_invalid_login;
            case ERROR_CODE_INVALID_USER_TOKEN:
                return R.string.error_invalid_login;
            case ERROR_CODE_INVALID_LOGIN:
                return R.string.error_invalid_login;
        }
        return code;
    }
}
