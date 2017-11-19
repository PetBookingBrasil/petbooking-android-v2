package com.petbooking.UI.Dashboard.Business;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import com.petbooking.Constants.AppConstants;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Cart.CartActivity;
import com.petbooking.UI.Dialogs.ConfirmDialogFragment;
import com.petbooking.Utils.AppUtils;

public class BusinessActivity extends AppCompatActivity implements ConfirmDialogFragment.FinishDialogListener {

    private AppointmentManager mAppointmentManager;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BusinessTabsAdapter mAdapter;
    private String businessId;
    private String businessName;
    private float businessDistance;
    private boolean alreadyShow = false;

    private ConfirmDialogFragment mConfirmDialogFragment;

    //region - Override

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        mAppointmentManager = AppointmentManager.getInstance();

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        businessId = getIntent().getStringExtra("businessId");
        businessName = getIntent().getStringExtra("businessName");
        businessDistance = getIntent().getFloatExtra("businessDistance", 0);

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

        mAdapter = new BusinessTabsAdapter(getSupportFragmentManager(), businessId, businessDistance);
        mAdapter.setOnLoadedListener(mAdapterOnLoadedListener);

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mViewPagerOnPageChangeListener);

        mTabLayout.setBackgroundColor(getResources().getColor(R.color.secondary_red));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        mTabLayout.setupWithViewPager(mViewPager);

        String schedulingText = getString(R.string.business_tab_schedule).toUpperCase();
        String informationText = getString(R.string.business_tab_information).toUpperCase();
        mTabLayout.getTabAt(0).setText(schedulingText);
        mTabLayout.getTabAt(1).setText(informationText);

        mConfirmDialogFragment = ConfirmDialogFragment.newInstance();

        AppUtils.showLoadingDialog(this);
    }

    @Override
    protected void onPause() {
        mAppointmentManager.setCurrentBusinessId(this.businessId);
        mAppointmentManager.setCurrentBusinessName(this.businessName);
        mAppointmentManager.setCurrentBusinessDistance(this.businessDistance);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuId;

        if (mViewPager.getCurrentItem() == 0) {
            menuId = R.menu.menu_schedule;
        } else {
            menuId = R.menu.main;
        }

        getMenuInflater().inflate(menuId, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.schedules) {
            goToCartActivity();
        } else if (item.getItemId() == R.id.notifications) {
            Log.d("ITEM SELECTED", "NOTIFICATIONS");
        }

        return true;
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

    @Override
    public void onBackPressed() {
        if (alreadyShow) {
            super.onBackPressed();
        } else {
            alreadyShow = true;
            mConfirmDialogFragment.setDialogInfo(R.string.cancel_appointment_title, R.string.cancel_appointment_text,
                    R.string.confirm_cancel_appointment);
            mConfirmDialogFragment.setCancelText(R.string.dialog_back);
            mConfirmDialogFragment.show(getSupportFragmentManager(), "CANCEL_APPOINTMENT");
        }
    }

    //endregion

    //region - Public

    /**
     * Go to cart activity
     */
    public void goToCartActivity() {
        Intent cartIntent = new Intent(this, CartActivity.class);
        startActivity(cartIntent);
    }

    public BusinessTabsAdapter getAdapter() {
        return mAdapter;
    }

    //endregion

    //region - Listener

    private final ViewPager.OnPageChangeListener mViewPagerOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageScrollStateChanged(int state) { }

        @Override
        public void onPageSelected(int position) {
            invalidateOptionsMenu();
        }
    };

    private final BusinessTabsAdapter.OnLoadedListener mAdapterOnLoadedListener = AppUtils::hideDialog;

    //endregion

}
