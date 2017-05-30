package com.petbooking.UI.Widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petbooking.R;
import com.petbooking.UI.Widget.Adapters.DaysListAdapter;

import java.util.ArrayList;

/**
 * Created by Luciano José on 27/05/2017.
 */

public class AppointmentCalendar extends LinearLayout {

    private ArrayList<String> mDaysList;

    private ImageButton mIBtnPrevious;
    private ImageButton mIBtnNext;
    private TextView mTvMonth;

    /**
     * Days List
     */
    private DaysListAdapter mAdapter;
    private RecyclerView mRvDays;
    private LinearLayoutManager mLayoutManager;

    public AppointmentCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public void initView(Context context, AttributeSet attributes) {
        View view = View.inflate(context, R.layout.widget_appointment_calendar, this);

        mDaysList = new ArrayList<>();
        prepareListData();
        mIBtnPrevious = (ImageButton) view.findViewById(R.id.previousButton);
        mIBtnNext = (ImageButton) view.findViewById(R.id.nextButton);
        mTvMonth = (TextView) view.findViewById(R.id.month);
        mRvDays = (RecyclerView) view.findViewById(R.id.days_list);
        mAdapter = new DaysListAdapter(context, mDaysList);

        mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRvDays.setHasFixedSize(true);
        mRvDays.setLayoutManager(mLayoutManager);

        if (mAdapter != null) {
            mRvDays.setAdapter(mAdapter);
        }
    }

    private void prepareListData() {
        for (int i = 10; i < 20; i++) {
            mDaysList.add("" + i);
        }
    }
}
