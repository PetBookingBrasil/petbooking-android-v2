package com.petbooking.UI.Dashboard.Business;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.petbooking.UI.Dashboard.Business.BusinessInformation.BusinessInformationFragment;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class BusinessTabsAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    final int PAGES_COUNT = 2;

    public BusinessTabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new BusinessInformationFragment();
        } else if (position == 1) {
            return new BusinessInformationFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return PAGES_COUNT;
    }

}
