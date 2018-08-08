package com.petbooking.Utils;

import com.petbooking.Constants.AppConstants;
import com.petbooking.Models.Pet;
import com.petbooking.Models.User;
import com.petbooking.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Luciano José on 15/04/2017.
 */

public class FormUtils {
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static int validateUser(User user, boolean checkPassword) throws ParseException {
        Date today = new Date();

        if (CommonUtils.isEmpty(user.name)
                || CommonUtils.isEmpty(user.email) || CommonUtils.isEmpty(user.phone)
                ) {
            return R.string.error_fields_empty;
        } else if (!CommonUtils.isValidEmail(user.email)) {
            return R.string.error_invalid_email;
        } else if (!CommonUtils.isPhoneValid(user.phone)) {
            return R.string.error_invalid_phone;
        } else if (checkPassword && !CommonUtils.isValidPassword(user.password)) {
            return R.string.error_invalid_password;
        } else if (today.before(dateFormat.parse(user.birthday))) {
            return R.string.error_future_date;
        }

        return -1;
    }

    public static int newValidateUser(User user, boolean checkPassword) throws ParseException {
        Date today = new Date();

        if (CommonUtils.isEmpty(user.name)
                || CommonUtils.isEmpty(user.email)) {
            return R.string.error_fields_empty;
        } else if (!CommonUtils.isValidEmail(user.email)) {
            return R.string.error_invalid_email;
        } else if (!CommonUtils.isEmpty(user.phone) && !CommonUtils.isPhoneValid(user.phone)) {
            return R.string.error_invalid_phone;
        } else if (checkPassword && !CommonUtils.isEmpty(user.password) && !CommonUtils.isValidPassword(user.password)) {
            return R.string.error_invalid_password;
        }

        return -1;
    }

    public static int validateUserSocialLogin(User user){
        if(!CommonUtils.isEmpty(user.email)) {
            if (!CommonUtils.isValidEmail(user.email)) {
                return R.string.error_invalid_email;
            }
        }
        return -1;
    }

    public static int validatePet(Pet pet, boolean checkChip) throws ParseException {
        Date today = new Date();

        if (CommonUtils.isEmpty(pet.name) || CommonUtils.isEmpty(pet.birthday) || CommonUtils.isEmpty(pet.gender)
                || CommonUtils.isEmpty(pet.type) || CommonUtils.isEmpty(pet.size) || CommonUtils.isEmpty(pet.coatType)
                || CommonUtils.isEmpty(pet.breedId) || CommonUtils.isEmpty(pet.mood)) {
            return R.string.error_fields_empty;
        }else if(checkChip){
            if(CommonUtils.isEmpty(pet.chipNumber)){
                return R.string.error_fields_empty;
            }
        }

        if (today.before(dateFormat.parse(pet.birthday))) {
            return R.string.error_future_date;
        }

        return -1;
    }

    public static String getPhoto(String imageBase) {
        return AppConstants.BASE64 + imageBase;
    }

}
