package com.petbooking.UI.Widget;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.petbooking.Models.CalendarItem;
import com.petbooking.R;
import com.petbooking.UI.Widget.Adapters.HorizontalCalendarAdapter;
import com.petbooking.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Luciano José on 10/06/2017.
 */

public class HorizontalCalendar extends LinearLayout {

    /**
     * Components
     */
    private RecyclerView mRvCalendar;
    private LinearLayoutManager mLayoutManager;
    private HorizontalCalendarAdapter mAdapter;
    private SnapHelper mSnapHelper;
    private ArrayList<CalendarItem> mDateList;

    /**
     * Control
     */
    private int currentPosition;
    private int defaultPosition;
    private Date defaultDate;
    private Date startDate;
    private Date endDate;

    public HorizontalCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.widget_horizontal_calendar, this);

        mDateList = new ArrayList<>();
        currentPosition = 0;

        mRvCalendar = (RecyclerView) view.findViewById(R.id.date_list);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new HorizontalCalendarAdapter(context, mDateList);
        mSnapHelper = new LinearSnapHelper();

        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvCalendar.setHasFixedSize(true);
        mRvCalendar.setLayoutManager(mLayoutManager);
        mRvCalendar.setAdapter(mAdapter);
        mSnapHelper.attachToRecyclerView(mRvCalendar);

    }

    /**
     * Select the previous day
     */
    public void previousDay() {
        this.currentPosition = mLayoutManager.findFirstVisibleItemPosition();
        if (this.currentPosition > 0) {
            currentPosition--;
            mRvCalendar.smoothScrollToPosition(currentPosition);
        }
    }

    /**
     * Select the next day
     */
    public void nextDay() {
        this.currentPosition = mLayoutManager.findFirstVisibleItemPosition();
        if (this.currentPosition < mDateList.size()) {
            currentPosition++;
            mRvCalendar.smoothScrollToPosition(currentPosition);
        }
    }

    /**
     * Set default date
     *
     * @param defaultDate
     */
    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
    }

    /**
     * Set Date Interval
     */
    public void setDateInterval(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;

        new InitializeDatesList().execute();
    }

    private class InitializeDatesList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String dayName;
            String monthName;

            mDateList = new ArrayList<CalendarItem>();
            CalendarItem calendarItem;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(startDate);

            while (calendar.getTime().before(endDate)) {
                dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                calendarItem = new CalendarItem(calendar.get(calendar.DAY_OF_MONTH), calendar.get(calendar.MONTH) + 1, calendar.get(calendar.YEAR),
                        dayName,
                        monthName);

                mDateList.add(calendarItem);
                calendar.add(Calendar.DATE, 1);
            }

            mAdapter.updateList(mDateList);
            mAdapter.notifyDataSetChanged();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
