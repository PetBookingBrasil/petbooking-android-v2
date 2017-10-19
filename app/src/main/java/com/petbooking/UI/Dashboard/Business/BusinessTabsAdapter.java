package com.petbooking.UI.Dashboard.Business;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.petbooking.UI.Dashboard.Business.BusinessInformation.BusinessInformationFragment;
import com.petbooking.UI.Dashboard.Business.Schedule.ScheduleFragment;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class BusinessTabsAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private String businessId;
    private float businessDistance;
    private final int PAGES_COUNT = 2;

    public BusinessTabsAdapter(FragmentManager fm, Context context, String businessId, float businessDistance) {
        super(fm);
        this.mContext = context;
        this.businessId = businessId;
        this.businessDistance = businessDistance;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ScheduleFragment.newInstance(this.businessId);
        } else if (position == 1) {
            return BusinessInformationFragment.newInstance(this.businessId, this.businessDistance);
        }

        return null;
    }

    @Override
    public int getCount() {
        return PAGES_COUNT;
    }

}
