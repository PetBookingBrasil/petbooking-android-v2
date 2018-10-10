package com.petbooking.UI.Dashboard.Business;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.petbooking.Models.Category;
import com.petbooking.UI.Dashboard.Business.BusinessInformation.BusinessInformationFragment;
import com.petbooking.UI.Dashboard.Business.BusinessServices.BusinessServicesFragment;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingFragment;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class BusinessTabsAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private String businessId;
    private float businessDistance;
    private final int PAGES_COUNT = 2;
    Category category;

    public BusinessTabsAdapter(FragmentManager fm, Context context, String businessId, float businessDistance, Category category) {
        super(fm);
        this.mContext = context;
        this.businessId = businessId;
        this.businessDistance = businessDistance;
        this.category = category;

        if(category !=null){
            Log.i(getClass().getSimpleName(),"Category aqui é null");
        }else{
            Log.i(getClass().getSimpleName(),"Category aqui nao é null");
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return SchedulingFragment.newInstance(this.businessId,category);
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
