package com.petbooking.Utils;

import com.petbooking.Constants.AppConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luciano José on 15/04/2017.
 */

public class FormUtils {

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(AppConstants.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
