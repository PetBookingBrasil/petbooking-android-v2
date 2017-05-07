package com.petbooking.Utils;

import android.content.Context;
import android.text.TextUtils;

import com.petbooking.API.Pet.APIPetConstants;
import com.petbooking.R;

/**
 * Created by Luciano José on 06/05/2017.
 */

public abstract class PetUtils {

    public static String getGender(Context context, String gender) {
        if (TextUtils.equals(gender, context.getString(R.string.gender_male))) {
            return APIPetConstants.DATA_GENDER_MALE;
        }
        if (TextUtils.equals(gender, context.getString(R.string.gender_female))) {
            return APIPetConstants.DATA_GENDER_FEMALE;
        }

        return "";
    }

    public static String getSize(Context context, String size) {
        if (TextUtils.equals(size, context.getString(R.string.size_small))) {
            return APIPetConstants.DATA_SIZE_SMALL;
        }
        if (TextUtils.equals(size, context.getString(R.string.size_medium))) {
            return APIPetConstants.DATA_SIZE_MEDIUM;
        }
        if (TextUtils.equals(size, context.getString(R.string.size_big))) {
            return APIPetConstants.DATA_SIZE_BIG;
        }
        if (TextUtils.equals(size, context.getString(R.string.size_giant))) {
            return APIPetConstants.DATA_SIZE_GIANT;
        }

        return "";
    }

    public static String getCoatType(Context context, String coatType) {
        if (TextUtils.equals(coatType, context.getString(R.string.coat_short))) {
            return APIPetConstants.DATA_TYPE_SHORT;
        }
        if (TextUtils.equals(coatType, context.getString(R.string.coat_medium))) {
            return APIPetConstants.DATA_TYPE_MEDIUM;
        }
        if (TextUtils.equals(coatType, context.getString(R.string.coat_long))) {
            return APIPetConstants.DATA_TYPE_LONG;
        }

        if (TextUtils.equals(coatType, context.getString(R.string.coat_short))) {
            return APIPetConstants.DATA_TYPE_SHORT;
        }

        return "";
    }

    public static String getType(Context context, String type) {
        if (TextUtils.equals(type, context.getString(R.string.type_cat))) {
            return APIPetConstants.DATA_TYPE_CAT;
        }

        if (TextUtils.equals(type, context.getString(R.string.type_dog))) {
            return APIPetConstants.DATA_TYPE_DOG;
        }

        return "";
    }

    public static String getTemper(Context context, String temper) {
        if (TextUtils.equals(temper, context.getString(R.string.temper_agitated))) {
            return APIPetConstants.DATA_TEMPER_AGITATED;
        }
        if (TextUtils.equals(temper, context.getString(R.string.temper_happy))) {
            return APIPetConstants.DATA_TEMPER_HAPPY;
        }
        if (TextUtils.equals(temper, context.getString(R.string.temper_lovely))) {
            return APIPetConstants.DATA_TEMPER_LOVELY;
        }
        if (TextUtils.equals(temper, context.getString(R.string.temper_angry))) {
            return APIPetConstants.DATA_TEMPER_ANGRY;
        }
        if (TextUtils.equals(temper, context.getString(R.string.temper_playful))) {
            return APIPetConstants.DATA_TEMPER_PLAYFUL;
        }
        if (TextUtils.equals(temper, context.getString(R.string.temper_needy))) {
            return APIPetConstants.DATA_TEMPER_NEEDY;
        }
        if (TextUtils.equals(temper, context.getString(R.string.temper_affectionate))) {
            return APIPetConstants.DATA_TEMPER_AFFECTIONATE;
        }
        if (TextUtils.equals(temper, context.getString(R.string.temper_docile))) {
            return APIPetConstants.DATA_TEMPER_DOCILE;
        }
        if (TextUtils.equals(temper, context.getString(R.string.temper_quiet))) {
            return APIPetConstants.DATA_TEMPER_QUIET;
        }
        if (TextUtils.equals(temper, context.getString(R.string.temper_brave))) {
            return APIPetConstants.DATA_TEMPER_BRAVE;
        }

        return "";
    }

    public static String getDisplayTemper(Context context, String temper) {
        if (TextUtils.equals(temper, APIPetConstants.DATA_TEMPER_AGITATED)) {
            return context.getString(R.string.temper_agitated);
        }
        if (TextUtils.equals(temper, APIPetConstants.DATA_TEMPER_HAPPY)) {
            return context.getString(R.string.temper_happy);
        }
        if (TextUtils.equals(temper, APIPetConstants.DATA_TEMPER_LOVELY)) {
            return context.getString(R.string.temper_lovely);
        }
        if (TextUtils.equals(temper, APIPetConstants.DATA_TEMPER_ANGRY)) {
            return context.getString(R.string.temper_angry);
        }
        if (TextUtils.equals(temper, APIPetConstants.DATA_TEMPER_PLAYFUL)) {
            return context.getString(R.string.temper_playful);
        }
        if (TextUtils.equals(temper, APIPetConstants.DATA_TEMPER_NEEDY)) {
            return context.getString(R.string.temper_needy);
        }
        if (TextUtils.equals(temper, APIPetConstants.DATA_TEMPER_AFFECTIONATE)) {
            return context.getString(R.string.temper_affectionate);
        }
        if (TextUtils.equals(temper, APIPetConstants.DATA_TEMPER_DOCILE)) {
            return context.getString(R.string.temper_docile);
        }
        if (TextUtils.equals(temper, APIPetConstants.DATA_TEMPER_QUIET)) {
            return context.getString(R.string.temper_quiet);
        }
        if (TextUtils.equals(temper, APIPetConstants.DATA_TEMPER_BRAVE)) {
            return context.getString(R.string.temper_brave);
        }
        return "";
    }

    public static String getDisplayType(Context context, String type) {
        if (TextUtils.equals(type.toLowerCase(), APIPetConstants.DATA_TYPE_CAT)) {
            return context.getString(R.string.type_cat);
        }
        return context.getString(R.string.type_dog);
    }

    public static String getDisplayGender(Context context, String gender) {
        if (TextUtils.equals(gender.toLowerCase(), APIPetConstants.DATA_GENDER_FEMALE)) {
            return context.getString(R.string.gender_female);
        } else {
            return context.getString(R.string.gender_male);
        }
    }

    public static String getDisplaySize(Context context, String size) {
        if (TextUtils.equals(size.toLowerCase(), APIPetConstants.DATA_SIZE_MEDIUM)) {
            return context.getString(R.string.size_medium);
        } else if (TextUtils.equals(size.toLowerCase(), APIPetConstants.DATA_SIZE_BIG)) {
            return context.getString(R.string.size_big);
        } else if (TextUtils.equals(size.toLowerCase(), APIPetConstants.DATA_SIZE_GIANT)) {
            return context.getString(R.string.size_giant);
        } else {
            return context.getString(R.string.size_small);
        }
    }

    public static String getDisplayCoatType(Context context, String coatType) {
        if (TextUtils.equals(coatType.toLowerCase(), APIPetConstants.DATA_TYPE_MEDIUM)) {
            return context.getString(R.string.coat_medium);
        } else if (TextUtils.equals(coatType.toLowerCase(), APIPetConstants.DATA_TYPE_LONG)) {
            return context.getString(R.string.coat_long);
        } else {
            return context.getString(R.string.coat_short);
        }
    }
}
