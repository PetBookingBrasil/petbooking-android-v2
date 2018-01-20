package com.petbooking.UI.Dashboard.Content;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.petbooking.Models.Category;
import com.petbooking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {

    private ContentTabsAdapter mAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Category category;

    public ContentFragment() {
        // Required empty public constructor
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);

        mAdapter = new ContentTabsAdapter(getFragmentManager(), getContext(), category);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setBackgroundColor(getResources().getColor(R.color.brand_primary));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_menu_list);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_menu_map);


        return view;
    }



    public void backToList() {
        mViewPager.setCurrentItem(0);
    }

}
