package com.petbooking.UI.Dashboard.Business;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.petbooking.UI.Dashboard.Business.BusinessInformation.BusinessInformationFragment;
import com.petbooking.UI.Dashboard.Business.BusinessServices.BusinessServicesFragment;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class BusinessTabsAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private String businessId;
    private final int PAGES_COUNT = 2;

    public BusinessTabsAdapter(FragmentManager fm, Context context, String businessId) {
        super(fm);
        this.mContext = context;
        this.businessId = businessId;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return BusinessServicesFragment.newInstance(this.businessId);
        } else if (position == 1) {
            return BusinessInformationFragment.newInstance(this.businessId);
        }

        return null;
    }

    @Override
    public int getCount() {
        return PAGES_COUNT;
    }

}
