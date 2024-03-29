package com.petbooking.UI.Dashboard.Content;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.petbooking.Models.Category;
import com.petbooking.UI.Dashboard.BusinessList.BusinessListFragment;
import com.petbooking.UI.Dashboard.BusinessList.CategoryListFragment;
import com.petbooking.UI.Dashboard.BusinessList.RootFragment;
import com.petbooking.UI.Dashboard.BusinessMap.BusinessMap;
import com.petbooking.UI.Menu.Favorites.FavoritesFragment;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class ContentTabsAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    final int PAGES_COUNT = 3;
    Category category;
    FragmentManager fm;

    public ContentTabsAdapter(FragmentManager fm, Context context,  Category category) {
        super(fm);
        this.fm = fm;
        this.mContext = context;
        this.category = category;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new RootFragment();
        } else if (position == 1) {
            return new BusinessMap();
        }else if(position == 2){
            return new FavoritesFragment();
        }
        return null;
    }


    @Override
    public int getCount() {
        return PAGES_COUNT;
    }
}