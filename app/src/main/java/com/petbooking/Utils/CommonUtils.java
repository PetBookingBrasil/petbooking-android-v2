package com.petbooking.Utils;

import android.util.Log;

import com.petbooking.Constants.AppConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luciano José on 21/04/2017.
 */

public class CommonUtils {


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

    public static String formatPhone(String phone) {
        switch (phone.length()) {
            case 8:
                return phone.substring(0, 4) + "." + phone.substring(4, 8);
            case 9:
                return phone.substring(0, 5) + "." + phone.substring(5, 9);
            case 10:
                return "(" + phone.substring(0, 2) + ") " + phone.substring(2, 6) + "." + phone.substring(6, 10);
            case 11:
                return "(" + phone.substring(0, 2) + ") " + phone.substring(2, 7) + "." + phone.substring(7, 11);
            default:
                return "";
        }
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

}
