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

public final class BusinessTabsAdapter extends FragmentStatePagerAdapter {

    private String businessId;
    private float businessDistance;
    private OnLoadedListener mOnLoadedListener;

    private boolean mIsBusinessReviewsLoaded;
    private boolean mIsBusinessInfoLoaded;
    private boolean mIsListPetsLoaded;
    private boolean mIsListBusinessCategoriesLoaded;

    BusinessTabsAdapter(FragmentManager fragmentManager, String businessId, float businessDistance) {
        super(fragmentManager);
        this.businessId = businessId;
        this.businessDistance = businessDistance;
    }

    //region - Override

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

    //endregion

    //region - Public

    void setOnLoadedListener(OnLoadedListener onLoadedListener) {
        mOnLoadedListener = onLoadedListener;
    }

    public void businessReviewsLoaded() {
        mIsBusinessReviewsLoaded = true;
        checkIfLoaded();
    }

    public void businessInfoLoaded() {
        mIsBusinessInfoLoaded = true;
        checkIfLoaded();
    }

    public void listPetsLoaded() {
        mIsListPetsLoaded = true;
        checkIfLoaded();
    }

    public void listBusinessCategoriesLoaded() {
        mIsListBusinessCategoriesLoaded = true;
        checkIfLoaded();
    }

    //endregion

    //region - Private

    private void checkIfLoaded() {
        if (mOnLoadedListener != null) {
            if (mIsBusinessReviewsLoaded && mIsBusinessInfoLoaded && mIsListPetsLoaded && mIsListBusinessCategoriesLoaded) {
                mOnLoadedListener.endLoaded();
            }
        }
    }



    //endregion

    //region - Interface

    interface OnLoadedListener {
        void endLoaded();
    }

    //endregion


}
