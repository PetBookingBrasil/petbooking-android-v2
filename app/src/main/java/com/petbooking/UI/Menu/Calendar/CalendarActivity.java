package com.petbooking.UI.Menu.Calendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.petbooking.R;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        getSupportActionBar().setElevation(0);
    }
}
