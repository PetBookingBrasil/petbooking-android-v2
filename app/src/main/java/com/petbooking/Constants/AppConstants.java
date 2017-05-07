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

    /**
     * Prefs
     */
    public static final String CONSUMER_TOKEN = "consumerToken";
    public static final String CONSUMER_EXPIRATION_DATE = "consumerExpirationDate";
    public static final String SESSION_TOKEN = "sessionToken";
    public static final String SESSION_EXPIRATION_DATE = "sessionExpirationDate";
    public static final String USER_LOGGED = "userLogged";
    public static final String ALREADY_LOGGED = "alreadyLogged";
    public static final String SOCIAL_LOGIN = "socialLogin";
    public static final String USER_WRAPPED = "userWrapped";
    public static final String LOGIN_TYPE = "loginType";
    public static final String FACEBOOK_TYPE = "facebook";
    public static final String EMAIL_TYPE = "email";
    public static final String LAST_USER_EMAIL = "lastUserEmailPref";
    public static final String LAST_USER_PASS = "lastUserPassPref";
    public static final String LAST_USER_FB_TOKEN = "lastUserFBTokenPref";
    public static final String USER_VALID_FOR_SCHEDULING = "userValidForScheduling";

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
    public static final int CONFIRM_ACTION = 9004;
    public static final int CANCEL_ACTION = 9005;
    public static final int REFRESH_SESSION = 9002;
    public static final int REFRESH_CONSUMER = 9003;

    /**
     * Permissions
     */
    public static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    /**
     * Configs
     */
    public static final int CONNECTION_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 30;
    public static final int READ_TIMEOUT = 30;
    public static final String BASE64 = "data:image/jpeg;base64,";

}
