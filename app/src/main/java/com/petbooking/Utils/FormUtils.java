package com.petbooking.Utils;

import com.petbooking.Models.User;
import com.petbooking.R;

/**
 * Created by Luciano José on 15/04/2017.
 */

public class FormUtils {

    public static int validateUser(User user, boolean checkPassword) {
        if (CommonUtils.isEmpty(user.name) || CommonUtils.isEmpty(user.birthday) || CommonUtils.isEmpty(user.cpf)
                || CommonUtils.isEmpty(user.email) || CommonUtils.isEmpty(user.zipcode) || CommonUtils.isEmpty(user.street)
                || CommonUtils.isEmpty(user.city) || CommonUtils.isEmpty(user.neighborhood)
                || CommonUtils.isEmpty(user.state) || CommonUtils.isEmpty(user.streetNumber)) {
            return R.string.error_fields_empty;
        } else if (!CommonUtils.isValidEmail(user.email)) {
            return R.string.error_invalid_email;
        } else if (!CommonUtils.isCPFValid(user.cpf)) {
            return R.string.error_invalid_cpf;
        } else if (!CommonUtils.isPhoneValid(user.phone)) {
            return R.string.error_invalid_phone;
        } else if (checkPassword && !CommonUtils.isValidPassword(user.password)) {
            return R.string.error_invalid_password;
        }

        return -1;
    }

}
