package com.petbooking.UI.Dashboard.Content;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    TextView tabContent;
    TextView tabTreeText;
    TextView tabTwoText;
    TabLayout.Tab tabTree;
    TabLayout.Tab tabTwo;

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
        mViewPager.setCurrentItem(0);

        mTabLayout.setBackgroundColor(getResources().getColor(R.color.secondary_red));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        mTabLayout.setupWithViewPager(mViewPager);

        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.tab_item_layout, null);
        tabContent = (TextView) tabLinearLayout.findViewById(R.id.tabContent);
        tabContent.setText(getContext().getString(R.string.tab_category));
        tabContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_menu_list, 0, 0, 0);

        LinearLayout tabLinearLayoutTwo = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.tab_item_layout, null);
        tabTwoText = (TextView) tabLinearLayoutTwo.findViewById(R.id.tabContent);
        tabTwoText.setText(getContext().getString(R.string.tab_map));
        tabTwoText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_my_location, 0, 0, 0);

        LinearLayout tabLinearLayoutTree = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.tab_item_layout, null);
        tabTreeText = (TextView) tabLinearLayoutTree.findViewById(R.id.tabContent);
        tabTreeText.setText(getContext().getString(R.string.favorites));
        tabTreeText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_menu_favorite, 0, 0, 0);

        mTabLayout.getTabAt(0).setCustomView(tabContent);
        mTabLayout.getTabAt(1).setCustomView(tabTwoText);
        mTabLayout.getTabAt(2).setCustomView(tabTreeText);
        tabTree = mTabLayout.getTabAt(2);
        tabTwo = mTabLayout.getTabAt(1);

        return view;
    }


    public void setChangeNameTab(String nameTab, boolean removeTab) {
        tabContent.setText(nameTab);
        if (removeTab) {
            TabLayout.Tab hasTab = mTabLayout.getTabAt(2);
            if (hasTab != null) ;
            mTabLayout.removeTabAt(2);
        }
    }

    public void removeTabMap() {
        TabLayout.Tab hasTab = mTabLayout.getTabAt(1);
        if (hasTab != null) {
            mTabLayout.removeTabAt(1);
        }

    }

    public void addTabMap() {
        TabLayout.Tab hasTab = mTabLayout.getTabAt(1);
        if (hasTab == null && tabTwo != null) {
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabTwoText), 1);
        }
    }

    public void addTab() {
        if (tabTree != null) {
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabTreeText));
        }
    }

    public void backToList() {
        mViewPager.setCurrentItem(0);
    }

}
