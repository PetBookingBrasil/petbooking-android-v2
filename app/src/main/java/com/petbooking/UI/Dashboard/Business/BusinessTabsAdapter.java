package com.petbooking.UI.Dashboard.Business;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.petbooking.UI.Dashboard.Business.BusinessInformation.BusinessInformationFragment;
import com.petbooking.UI.Dashboard.Business.Schedule.ScheduleFragment;

/**
 * Created by Luciano José on 29/01/2017.
 * Edited by Bruno Tortato Furtado on 16/11/2017.
 */

final class BusinessTabsAdapter extends FragmentStatePagerAdapter {

    private String businessId;
    private float businessDistance;

    BusinessTabsAdapter(FragmentManager fragmentManager, String businessId, float businessDistance) {
        super(fragmentManager);
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
        return 2;
    }

}
