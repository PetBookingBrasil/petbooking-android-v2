package com.petbooking.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.petbooking.Constants.AppConstants;
import com.petbooking.Models.CalendarItem;
import com.petbooking.Models.UserAddress;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luciano José on 21/04/2017.
 */

public class CommonUtils {

    public static final int PASSWORD_LENGTH = 6;
    public static final Locale BRAZIL = new Locale("pt","BR");
    final public static String DATEFORMATDEFAULT = "yyyy-MM-dd";
    final public static String MONTH_DESCRIPTION_FORMAT = "MMMM";
    final public static String DAY_FORMAT = "dd";
    final public static String DAY_FORMAT_DESCRIOTION = "EE";
    final public static String MONTH_NUMBER_FORMAT = "MM";
    final public static String YEAR_FORMAT = "yyyy";
    public static void hideKeyboard(Activity activity) {
        if (activity == null || activity.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isEmpty(String text) {
        return text == null || text.equals("");
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
        return password.length() >= PASSWORD_LENGTH;

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

    public static String formatDate(final String formatType, final Date date) {
        final SimpleDateFormat dateFormatterInstance = new SimpleDateFormat(formatType, BRAZIL);
        return dateFormatterInstance.format(date);
    }

    public static Date parseDate(String dateFormat,String dateParser){
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            Date date = format.parse(dateParser);
            System.out.println(date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
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

    public static String formatDay(int day) {
        return day < 10 ? "0" + day : day + "";
    }

    public static long getRefreshDate(long timestamp) {
        String epochAuth = String.valueOf(timestamp);
        Date expirationDate = CommonUtils.getUTCDate(epochAuth);
        Calendar mCalendar = Calendar.getInstance();

        mCalendar.setTime(expirationDate);
        mCalendar.add(Calendar.SECOND, 10);

        return mCalendar.getTimeInMillis();
    }

    public static String encodeBase64(Bitmap image) {
        String encoded = null;
        byte[] bytes;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        image.compress(Bitmap.CompressFormat.JPEG, 100, output);
        bytes = output.toByteArray();
        encoded = Base64.encodeToString(bytes, Base64.NO_WRAP);

        return encoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String formatZipcode(String zipcode) {
        return zipcode.substring(0, 5) + '-' + zipcode.substring(5);
    }

    public static String formatDayName(String name) {
        if (name.contains("-")) {
            String first = name.split("-")[0];
            String last = name.split("-")[1];

            first = first.substring(0, 1).toUpperCase() + first.substring(1).toLowerCase();
            last = last.substring(0, 1).toUpperCase() + last.substring(1).toLowerCase();

            return first + "-" + last;
        }

        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return name;

    }

    public static CalendarItem parseCalendarItem(Date date) {
        String dayName;
        String monthName;

        CalendarItem calendarItem;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        calendarItem = new CalendarItem(calendar.getTime(), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR),
                dayName,
                monthName);
        return calendarItem;
    }

    public static String uppercaseFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static int getDeviceWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        return deviceWidth;
    }
}
