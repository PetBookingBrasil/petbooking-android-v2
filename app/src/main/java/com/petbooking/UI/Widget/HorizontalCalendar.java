package com.petbooking.UI.Widget;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.petbooking.Models.CalendarItem;
import com.petbooking.R;
import com.petbooking.UI.Widget.Adapters.HorizontalCalendarAdapter;
import com.petbooking.Utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

        defaultDate = new Date();
        CalendarItem item = CommonUtils.parseCalendarItem(defaultDate);

        mDateList = new ArrayList<>();
        mDateList.add(item);
        currentPosition = 0;

        mRvCalendar = (RecyclerView) view.findViewById(R.id.date_list);
        mLayoutManager = new LinearLayoutManager(context);
        mSnapHelper = new LinearSnapHelper();

        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvCalendar.setHasFixedSize(true);
        mRvCalendar.setLayoutManager(mLayoutManager);
        mAdapter = new HorizontalCalendarAdapter(context, mDateList);
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
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String dayName;
            String monthName;

            mDateList = new ArrayList<CalendarItem>();
            CalendarItem calendarItem;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(startDate);

            while (calendar.getTime().before(endDate)) {
                dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                calendarItem = new CalendarItem(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR),
                        dayName,
                        monthName);

                mDateList.add(calendarItem);

                if (df.format(calendar.getTime()).equals(df.format(defaultDate))) {
                    defaultPosition = mDateList.size() - 1;
                }

                calendar.add(Calendar.DATE, 1);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mAdapter.updateList(mDateList);
            mAdapter.notifyDataSetChanged();
            mRvCalendar.scrollToPosition(defaultPosition);
        }
    }
}
