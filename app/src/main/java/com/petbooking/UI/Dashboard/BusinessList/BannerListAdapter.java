package com.petbooking.UI.Dashboard.BusinessList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.petbooking.Models.Banner;

import java.util.ArrayList;

public class BannerListAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Banner> banners;
    CategoryListFragment fragment;

    public void setFragment(CategoryListFragment fragment) {
        this.fragment = fragment;
    }

    public BannerListAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setBanners(ArrayList<Banner> banners) {
        this.banners = banners;
    }

    @Override
    public Fragment getItem(int position) {
        BannerFragment fragment = new BannerFragment();
        fragment.setBanners(banners);
        fragment.setPosition(position);
        fragment.setFragment(this.fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return banners.size();
    }
}
