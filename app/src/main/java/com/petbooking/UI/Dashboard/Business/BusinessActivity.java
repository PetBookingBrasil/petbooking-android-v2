package com.petbooking.UI.Dashboard.Business;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petbooking.Constants.AppConstants;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Managers.PreferenceManager;
import com.petbooking.Models.Category;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Cart.CartActivity;
import com.petbooking.UI.Dashboard.Content.ContentTabsAdapter;
import com.petbooking.UI.Dialogs.ConfirmDialogFragment;
import com.petbooking.UI.Menu.Search.SearchActivity;

public class BusinessActivity extends AppCompatActivity implements ConfirmDialogFragment.FinishDialogListener {

    private Toolbar mToolbar;
    private AppointmentManager mAppointmentManager;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BusinessTabsAdapter mAdapter;
    private String businessId;
    private String businessName;
    private float businessDistance;
    private boolean alreadyShow = false;
    private Menu menu;
    private Category category;

    private ConfirmDialogFragment mConfirmDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAppointmentManager = AppointmentManager.getInstance();

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        businessId = getIntent().getStringExtra("businessId");
        businessName = getIntent().getStringExtra("businessName");
        businessDistance = getIntent().getFloatExtra("businessDistance", 0);
        category = (Category) getIntent().getParcelableExtra("category");


        if (!getIntent().hasExtra("businessId")) {
            businessId = mAppointmentManager.getCurrentBusinessId();
            businessName = mAppointmentManager.getCurrentBusinessName();
            businessDistance = mAppointmentManager.getCurrentBusinessDistance();
            mAppointmentManager.removeKey("businessId");
            mAppointmentManager.removeKey("businessName");
            mAppointmentManager.removeKey("businessDistance");
        }

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(businessName);

        mAdapter = new BusinessTabsAdapter(getSupportFragmentManager(), this, businessId, businessDistance,category);
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setBackgroundColor(getResources().getColor(R.color.secondary_red));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setText(R.string.business_tab_services);
        mTabLayout.getTabAt(1).setText(R.string.business_tab_information);

        mConfirmDialogFragment = ConfirmDialogFragment.newInstance();
    }

    public void setTitle(String name){
        getSupportActionBar().setTitle(businessName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.cart);
        MenuItemCompat.setActionView(item, R.layout.action_cart);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        TextView tv = (TextView) notifCount.findViewById(R.id.count_cart);
        tv.setVisibility(View.INVISIBLE);
        item.setVisible(false);
        this.menu = menu;
        return true;
    }

    public void showCartMenu(){
        if(menu != null){
            MenuItem item = menu.findItem(R.id.cart);
            RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
            TextView tv = (TextView) notifCount.findViewById(R.id.count_cart);
            tv.setVisibility(View.INVISIBLE);
            notifCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToCartActivity();
                }
            });
            menu.findItem(R.id.search).setVisible(false);
            menu.findItem(R.id.notifications).setVisible(false);
            item.setVisible(true);
        }
    }
    public void hideCartMenu(){
        MenuItem item = menu.findItem(R.id.cart);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        TextView tv = (TextView) notifCount.findViewById(R.id.count_cart);
        tv.setVisibility(View.INVISIBLE);
        menu.findItem(R.id.search).setVisible(true);
        menu.findItem(R.id.notifications).setVisible(true);
        item.setVisible(false);
    }
    public void updateCartCount(int size){
        MenuItem item = menu.findItem(R.id.cart);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        TextView tv = (TextView) notifCount.findViewById(R.id.count_cart);
        tv.setVisibility(View.VISIBLE);
        tv.setText(String.valueOf(size));
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.notifications).setVisible(false);
        item.setVisible(true);
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.search) {
            //Intent activity = new Intent(this, SearchActivity.class);
            //startActivity(activity);
        } else if (item.getItemId() == R.id.notifications) {
            Log.d("ITEM SELECTED", "NOTIFICATIONS");
        }else if(item.getItemId() == R.id.cart){
            goToCartActivity();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (alreadyShow) {
            mAppointmentManager.reset();
            super.onBackPressed();
        } else {
            alreadyShow = true;
            mConfirmDialogFragment.setDialogInfo(R.string.cancel_appointment_title, R.string.cancel_appointment_text,
                    R.string.confirm_cancel_appointment);
            mConfirmDialogFragment.setCancelText(R.string.dialog_back);
            mConfirmDialogFragment.show(getSupportFragmentManager(), "CANCEL_APPOINTMENT");
        }
    }

    @Override
    protected void onPause() {
        mAppointmentManager.setCurrentBusinessId(this.businessId);
        mAppointmentManager.setCurrentBusinessName(this.businessName);
        mAppointmentManager.setCurrentBusinessDistance(this.businessDistance);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Go to cart activity
     */
    public void goToCartActivity() {
        Intent cartIntent = new Intent(this, CartActivity.class);
        startActivity(cartIntent);
    }

    @Override
    public void onFinishDialog(int action) {
        if (action == AppConstants.CONFIRM_ACTION) {
            mConfirmDialogFragment.dismiss();
            mAppointmentManager.reset();
            onBackPressed();
        } else if (action == AppConstants.CANCEL_ACTION) {
            mConfirmDialogFragment.dismiss();
            alreadyShow = false;
        }
    }
}
