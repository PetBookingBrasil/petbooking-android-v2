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

        mTabLayout.setBackgroundColor(getResources().getColor(R.color.brand_primary));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        mTabLayout.setupWithViewPager(mViewPager);

        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.tab_item_layout, null);
        tabContent = (TextView) tabLinearLayout.findViewById(R.id.tabContent);
        tabContent.setText(" " + getContext().getString(R.string.tab_category));
        tabContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_menu_list, 0, 0, 0);

        LinearLayout tabLinearLayoutTwo = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.tab_item_layout, null);
        TextView textView = (TextView) tabLinearLayoutTwo.findViewById(R.id.tabContent);
        textView.setText(" " +getContext().getString(R.string.tab_map));
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_on_white_24px, 0, 0, 0);
        mTabLayout.getTabAt(0).setCustomView(tabContent);
        mTabLayout.getTabAt(1).setCustomView(textView);
        //mTabLayout.getTabAt(0).setIcon(R.drawable.ic_menu_list).setText("Categorias");
        //mTabLayout.getTabAt(1).setIcon(R.drawable.ic_menu_map).setText("Mapa");


        return view;
    }


    public void setChangeNameTab(String nameTab){
        tabContent.setText(nameTab);
    }

    public void backToList() {
        mViewPager.setCurrentItem(0);
    }

}
