package com.petbooking.UI.Dashboard.Cart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.petbooking.Managers.AppointmentManager;
import com.petbooking.R;

public class CartActivity extends AppCompatActivity {

    private AppointmentManager mAppointmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mAppointmentManager = AppointmentManager.getInstance();

        getSupportActionBar().setTitle(R.string.cart_title);
        getSupportActionBar().setSubtitle(mAppointmentManager.getCurrentBusinessName());
    }
}
