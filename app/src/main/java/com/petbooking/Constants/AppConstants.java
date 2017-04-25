package com.petbooking.Constants;

import android.Manifest;
import android.os.Build;

import com.petbooking.R;

/**
 * Created by Luciano José on 13/04/2017.
 */

public class AppConstants {

    public static final int SDK_VERSION = Build.VERSION.SDK_INT;
    public static final int PREF_PRIVATE_MODE = 0;
    public static final String PREF_NAME = "PBPref";

    public static final String CONSUMER_TOKEN = "consumerToken";
    public static final String CONSUMER_EXPIRATION_DATE = "consumerExpirationDate";
    public static final String SESSION_TOKEN = "sessionToken";
    public static final String USER_LOGGED = "userLogged";
    public static final String ALREADY_LOGGED = "alreadyLogged";

    public static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Facebook Properties
     */
    public static final String FACEBOOK_EMAIL_PERMISSION = "email";
    public static final String USER_PICTURE_URL = "https://graph.facebook.com/%s/picture?type=large";

    /**
     * Pages Contants
     */
    public static final int PRESENTATION_COLORS[] = {R.color.presentation_1, R.color.presentation_2, R.color.presentation_3};

    /**
     * Intent Codes
     */
    public static final int PICK_PHOTO = 1;
    public static final int TAKE_PHOTO = 2;
    public static final int OK_ACTION = 9000;
    public static final int BACK_SCREEN_ACTION = 9001;

    /**
     * Permissions
     */
    public static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    /**
     * Configs
     */
    public static final int CONNECTION_TIMEOUT = 30;
    public static final int READ_TIMEOUT = 30;
}
