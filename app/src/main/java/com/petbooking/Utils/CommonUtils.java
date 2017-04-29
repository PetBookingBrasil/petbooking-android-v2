package com.petbooking.Utils;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.view.inputmethod.InputMethodManager;

import com.petbooking.Constants.AppConstants;
import com.petbooking.Models.UserAddress;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luciano José on 21/04/2017.
 */

public class CommonUtils {

    public static final int PASSWORD_LENGTH = 6;

    public static void hideKeyboard(Activity activity) {
        if (activity == null || activity.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isEmpty(String text) {
        if (text == null || text.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(AppConstants.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPhoneValid(String phone) {
        if (CommonUtils.isEmpty(phone)) {
            return false;
        }
        String phoneNumber = phone.replace("(", "").replace(")", "").replace(".", "").trim();
        return phoneNumber.length() >= 10 && phoneNumber.length() <= 11;
    }

    public static boolean isCPFValid(String cpf) {
        if (cpf == null) {
            return false;
        }
        cpf = cpf.replace(".", "").replace("-", "");
        if (cpf.length() != 11) {
            return false;
        }
        long dCPF = Long.parseLong(cpf);
        if (dCPF == 0L || dCPF == 111_111_111_11L || dCPF == 222_222_222_22L || dCPF == 333_333_333_33L
                || dCPF == 444_444_444_44L || dCPF == 555_555_555_55L || dCPF == 666_666_666_66L
                || dCPF == 777_777_777_77L || dCPF == 888_888_888_88L || dCPF == 999_999_999_99L) {
            return false;
        }
        char dig10, dig11;
        int sum, weight;
        try {
            sum = 0;
            weight = 10;
            for (int i = 0; i < 9; i++) {
                int num = cpf.charAt(i) - 48;
                sum = sum + (num * weight);
                weight = weight - 1;
            }
            int r = 11 - (sum % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48);
            }
            sum = 0;
            weight = 11;
            for (int i = 0; i < 10; i++) {
                int num = cpf.charAt(i) - 48;
                sum = sum + (num * weight);
                weight = weight - 1;
            }
            r = 11 - (sum % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }
            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
        } catch (Exception error) {
            return false;
        }
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < PASSWORD_LENGTH) {
            return false;
        }

        return true;
    }

    public static String formatDate(int day, int month, int year) {
        month++;
        String dayAux = day < 10 ? ("0" + day) : (day + "");
        String monthAux = month < 10 ? ("0" + month) : (month + "");
        String date = dayAux + "/" + monthAux + "/" + year;

        return date;
    }

    public static String formatDate(String date) {
        String formatedDate = "";

        if (date != null) {
            formatedDate = date.substring(8, 10) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4);
        }

        return formatedDate;
    }

    public static UserAddress parseAddress(Address address) {
        UserAddress userAddress = null;
        String state = address.getAdminArea().substring(0, 2);
        String postalCode = address.getPostalCode().replace("-", "");

        state = state.toUpperCase();

        userAddress = new UserAddress(postalCode, address.getLocality(),
                address.getThoroughfare(), address.getSubLocality(), state, address.getFeatureName());

        return userAddress;
    }

    public static Date getUTCDate(String timestamp) {
        long epoch = Long.parseLong(timestamp);
        Date date = new Date(epoch * 1000);

        return date;
    }

    public static long getRefreshDate(long timestamp) {
        String epochAuth = String.valueOf(timestamp);
        Date expirationDate = CommonUtils.getUTCDate(epochAuth);
        Calendar mCalendar = Calendar.getInstance();

        mCalendar.setTime(expirationDate);
        mCalendar.add(Calendar.MINUTE, -2);

        return mCalendar.getTimeInMillis();
    }

}
