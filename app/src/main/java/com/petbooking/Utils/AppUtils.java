package com.petbooking.Utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Luciano José on 14/04/2017.
 */

public class AppUtils {

    private static Snackbar mSnackbar;

    public static void showSnackBar(View view, int message) {
        mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        mSnackbar.show();
    }
}
