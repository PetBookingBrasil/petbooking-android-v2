package com.petbooking.Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.petbooking.API.Business.APIBusinessConstants;
import com.petbooking.API.Pet.APIPetConstants;
import com.petbooking.Models.AppointmentDate;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * Created by Luciano José on 06/05/2017.
 */

public abstract class AppUtils {

    private static AlertDialog mDialog;
    private static AlertDialog.Builder mBuilder;

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

    public static String getColorPet(Context context, String color){
        if (TextUtils.equals(color, context.getString(R.string.color_white))) {
            return APIPetConstants.DATA_TEMPER_NEEDY;
        }
        if (TextUtils.equals(color, context.getString(R.string.color_black))) {
            return APIPetConstants.DATA_TEMPER_AFFECTIONATE;
        }
        if (TextUtils.equals(color, context.getString(R.string.color_brown))) {
            return APIPetConstants.DATA_TEMPER_DOCILE;
        }
        if (TextUtils.equals(color, context.getString(R.string.color_gray))) {
            return APIPetConstants.DATA_TEMPER_QUIET;
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
    
    //TODO: Trocar ícone do programa da prefeitura
    public static Drawable getBusinessIcon(Context context, String businesstype) {
        if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_CLINIC)) {
            return context.getResources().getDrawable(R.drawable.ic_category_clinic);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_TRAINING)) {
            return context.getResources().getDrawable(R.drawable.ic_category_trainer);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_BATH)) {
            return context.getResources().getDrawable(R.drawable.ic_category_bath);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_TRANSPORT)) {
            return context.getResources().getDrawable(R.drawable.ic_category_transport);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_WALKER)) {
            return context.getResources().getDrawable(R.drawable.ic_category_walker);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_DAYCARE)) {
            return context.getResources().getDrawable(R.drawable.ic_category_daycare);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_HOTEL)) {
            return context.getResources().getDrawable(R.drawable.ic_category_hotel);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_EMERGENCY)) {
            return context.getResources().getDrawable(R.drawable.ic_category_emergency);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_EXAMS)) {
            return context.getResources().getDrawable(R.drawable.ic_category_exam);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_HOSPITAL)) {
            return context.getResources().getDrawable(R.drawable.ic_category_hospital);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_DIAGNOSIS)) {
            return context.getResources().getDrawable(R.drawable.ic_category_diagnosis);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_CONSULTATIONS)) {
            return context.getResources().getDrawable(R.drawable.ic_category_consultations);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_CITY_HALL)) {
            return context.getResources().getDrawable(R.drawable.ic_category_consultations);
        }

        return null;
    }

    public static int getCategoryColor(Context context, String businesstype) {
        if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_CLINIC)) {
            return context.getResources().getColor(R.color.category_clinic);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_TRAINING)) {
            return context.getResources().getColor(R.color.category_trainer);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_BATH)) {
            return context.getResources().getColor(R.color.category_bath);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_TRANSPORT)) {
            return context.getResources().getColor(R.color.category_transport);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_WALKER)) {
            return context.getResources().getColor(R.color.category_walker);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_DAYCARE)) {
            return context.getResources().getColor(R.color.category_daycare);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_HOTEL)) {
            return context.getResources().getColor(R.color.category_hotel);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_EMERGENCY)) {
            return context.getResources().getColor(R.color.category_emergency);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_EXAMS)) {
            return context.getResources().getColor(R.color.category_exam);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_HOSPITAL)) {
            return context.getResources().getColor(R.color.category_hospital);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_DIAGNOSIS)) {
            return context.getResources().getColor(R.color.category_diagnosis);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_CONSULTATIONS)) {
            return context.getResources().getColor(R.color.category_consultations);
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_CITY_HALL)) {
            return context.getResources().getColor(R.color.category_city_hall_program);
        }

        return -1;
    }

    public static int getCategoryText(String businesstype) {
        if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_CLINIC)) {
            return R.string.category_clinic;
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_TRAINING)) {
            return R.string.category_training;
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_BATH)) {
            return R.string.category_bath;
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_TRANSPORT)) {
            return R.string.category_transport;
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_WALKER)) {
            return R.string.category_walker;
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_DAYCARE)) {
            return R.string.category_daycare;
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_HOTEL)) {
            return R.string.category_hotel;
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_EMERGENCY)) {
            return R.string.category_emergency;
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_EXAMS)) {
            return R.string.category_exams;
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_HOSPITAL)) {
            return R.string.category_hospital;
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_DIAGNOSIS)) {
            return R.string.category_diagnosis;
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_CONSULTATIONS)) {
            return R.string.category_consultations;
        } else if (TextUtils.equals(businesstype, APIBusinessConstants.DATA_CITY_HALL)) {
            return R.string.category_city_hall_program;
        }

        return -1;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    public static void showLoadingDialog(Context context) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        mBuilder = new AlertDialog.Builder(context);
        mBuilder.setView(R.layout.dialog_loading);
        mDialog = mBuilder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public static void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public static int containsMonth(ArrayList<AppointmentDate> dates, String monthName, int year) {
        int index = 0;

        if (dates.size() == 0) {
            return -1;
        } else {
            for (AppointmentDate date : dates) {
                if (date.monthName.equals(monthName) && date.year == year) {
                    return index;
                }

                index++;
            }
        }

        return -1;
    }

}
