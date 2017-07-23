package com.petbooking.UI.Widget;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.petbooking.API.User.Models.ScheduleResp;
import com.petbooking.Components.GravitySnapHelper.GravitySnapHelper;
import com.petbooking.Models.Appointment;
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

    private OnDateScrollListener onDateScrollListener;

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
    private ArrayList<ScheduleResp.Schedule> schedules;

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mLayoutManager.findFirstCompletelyVisibleItemPosition() != -1) {
                int itemPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                currentPosition = itemPosition;
                onDateScrollListener.onScroll(itemPosition);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

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
        mRvCalendar.addOnScrollListener(scrollListener);
        mSnapHelper.attachToRecyclerView(mRvCalendar);

    }

    /**
     * Select Default date
     */
    public void selectDefaultDate() {
        int i = 0;
        int nextDate = -1;
        int beforeDate = -1;

        for (CalendarItem item : mDateList) {
            if ((nextDate == -1 && item.date.after(defaultDate)) ||
                    (nextDate != -1 && item.date.before(mDateList.get(nextDate).date))) {
                nextDate = i;
            }

            if ((beforeDate == -1 && item.date.before(defaultDate)) ||
                    (beforeDate != -1 && item.date.before(mDateList.get(beforeDate).date))) {
                beforeDate = i;
            }

            i++;
        }

        if (nextDate != -1) {
            defaultPosition = nextDate;
        } else if (beforeDate != -1) {
            defaultPosition = beforeDate;
        }
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
     * Get Current date index
     */
    public int getCurrentDateIndex() {
        return this.currentPosition != mDateList.size() ? this.currentPosition : (currentPosition - 1);
    }

    /**
     * Set Date Interval
     */
    public void setSchedules(ArrayList<ScheduleResp.Schedule> schedules) {
        this.schedules = schedules;
        initDateList();
    }

    /**
     * Set OnScrollListener
     *
     * @param onDateScrollListener
     */
    public void setOnDateScrollListener(OnDateScrollListener onDateScrollListener) {
        this.onDateScrollListener = onDateScrollListener;
    }

    /**
     * Init Date List
     */
    public void initDateList() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dayName;
        String monthName;

        mDateList = new ArrayList<>();
        CalendarItem calendarItem;
        Calendar calendar = new GregorianCalendar();

        /**
         * Create Dates for Calendar
         * based on Appointments
         */
        for (ScheduleResp.Schedule schedule : schedules) {
            calendar.setTime(schedule.date);

            dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
            monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            calendarItem = new CalendarItem(calendar.getTime(), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR),
                    dayName,
                    monthName);

            mDateList.add(calendarItem);
        }


        if (mDateList.size() != 0) {
            selectDefaultDate();

            mAdapter.updateList(mDateList);
            mAdapter.notifyDataSetChanged();
            mRvCalendar.scrollToPosition(defaultPosition);
            onDateScrollListener.onScroll(defaultPosition);
        }
    }

    public interface OnDateScrollListener {
        void onScroll(int position);
    }
}
