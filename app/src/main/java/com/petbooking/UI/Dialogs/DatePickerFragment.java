package com.petbooking.UI.Dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.petbooking.Utils.CommonUtils;

import java.util.Calendar;

/**
 * Created by Luciano José on 22/04/2017.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DatePickerListener mDatePickerListener;
    private DatePickerDialog instance;
    private Calendar mCalendar;
    private int year;
    private int month;
    private int day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCalendar = Calendar.getInstance();
        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH);
        day = mCalendar.get(Calendar.DAY_OF_MONTH);

        instance = new DatePickerDialog(getActivity(), this, year, month, day);
        mDatePickerListener = (DatePickerListener) getActivity();

        return instance;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = CommonUtils.formatDate(dayOfMonth, month, year);

        mDatePickerListener.onDateSet(date);
    }

    public static DatePickerFragment newInstance() {
        return new DatePickerFragment();
    }

    public interface DatePickerListener {
        void onDateSet(String date);
    }
}