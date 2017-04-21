package com.petbooking.UI.Presentation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Luciano José on 20/04/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 3;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TourFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return this.NUM_PAGES;
    }
}
