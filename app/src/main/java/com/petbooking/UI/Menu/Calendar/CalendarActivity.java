package com.petbooking.UI.Menu.Calendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.petbooking.R;
import com.petbooking.UI.Widget.HorizontalCalendar;

import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private final Calendar startDate = Calendar.getInstance();
    private final Calendar endDate = Calendar.getInstance();

    private HorizontalCalendar mHCCalendar;

    private ImageView mBtnPreviousDate;
    private ImageView mBtnNextDate;
    private TextView mTvDay;

    View.OnClickListener btnDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemId = v.getId();
            if (itemId == mBtnPreviousDate.getId()) {
                mHCCalendar.previousDay();
            } else {
                mHCCalendar.nextDay();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        getSupportActionBar().setElevation(0);

        endDate.add(Calendar.MONTH, 3);
        startDate.add(Calendar.MONTH, -3);

        mHCCalendar = (HorizontalCalendar) findViewById(R.id.horizontal_calendar);
        mHCCalendar.setDateInterval(startDate.getTime(), endDate.getTime());

        mBtnPreviousDate = (ImageView) findViewById(R.id.previous_date);
        mBtnNextDate = (ImageView) findViewById(R.id.next_date);
        mTvDay = (TextView) findViewById(R.id.day_number);

        mBtnPreviousDate.setOnClickListener(btnDateListener);
        mBtnNextDate.setOnClickListener(btnDateListener);
    }
}
