package com.petbooking.UI.Dashboard.Business;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.petbooking.Managers.PreferenceManager;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Content.ContentTabsAdapter;

public class BusinessActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private PreferenceManager mPreferenceManager;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BusinessTabsAdapter mAdapter;
    private String businessId;
    private String businessName;
    private float businessDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        mPreferenceManager = PreferenceManager.getInstance();

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        businessId = getIntent().getStringExtra("businessId");
        businessName = getIntent().getStringExtra("businessName");
        businessDistance = getIntent().getFloatExtra("businessDistance", 0);

        if (!getIntent().hasExtra("businessId")) {
            businessId = mPreferenceManager.getString("businessId");
            businessName = mPreferenceManager.getString("businessName");
            businessDistance = mPreferenceManager.getFloat("businessDistance");
            mPreferenceManager.removeKey("businessId");
            mPreferenceManager.removeKey("businessName");
            mPreferenceManager.removeKey("businessDistance");
        }

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(businessName);

        mAdapter = new BusinessTabsAdapter(getSupportFragmentManager(), this, businessId, businessDistance);
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setBackgroundColor(getResources().getColor(R.color.secondary_red));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setText(R.string.business_tab_services);
        mTabLayout.getTabAt(1).setText(R.string.business_tab_information);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.schedules) {
            Log.d("ITEM SELECTED", "CART");
        } else if (item.getItemId() == R.id.notifications) {
            Log.d("ITEM SELECTED", "NOTIFICATIONS");
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        mPreferenceManager.putString("businessId", this.businessId);
        mPreferenceManager.putString("businessName", this.businessName);
        mPreferenceManager.putFloat("businessDistance", this.businessDistance);
        super.onPause();
    }
}
