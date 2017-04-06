package com.petbooking;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.petbooking.Components.MenuItem;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    NavigationView mNavView;
    View mHeaderView;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;

    /**
     * Sidemenu content
     */
    private CircleImageView mCivSideMenuPicture;
    private TextView mTvSideMenuName;
    private TextView mTvSideMenuAddress;

    /**
     * Sidemenu Itens
     */

    private MenuItem mMiSearch;
    private MenuItem mMiCalendar;
    private MenuItem mMiPayments;
    private MenuItem mMiFavorites;
    private MenuItem mMiSettings;
    private LinearLayout mMiLogout;

    View.OnClickListener mMenuItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onMenuItemSelected(v.getId());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavView = (NavigationView) findViewById(R.id.nav_view);

        /**
         * Set sidemenu actions
         */

        mMiSearch = (MenuItem) findViewById(R.id.search);
        mMiCalendar = (MenuItem) findViewById(R.id.calendar);
        mMiPayments = (MenuItem) findViewById(R.id.payments);
        mMiFavorites = (MenuItem) findViewById(R.id.favorites);
        mMiSettings = (MenuItem) findViewById(R.id.settings);
        mMiLogout = (LinearLayout) findViewById(R.id.logout);

        mMiSearch.setOnClickListener(mMenuItemListener);
        mMiCalendar.setOnClickListener(mMenuItemListener);
        mMiPayments.setOnClickListener(mMenuItemListener);
        mMiFavorites.setOnClickListener(mMenuItemListener);
        mMiSettings.setOnClickListener(mMenuItemListener);
        mMiLogout.setOnClickListener(mMenuItemListener);

        /**
         * Update Sidemenu Info
         */

        mCivSideMenuPicture = (CircleImageView) findViewById(R.id.sidemenu_picture);
        mTvSideMenuName = (TextView) findViewById(R.id.sidemenu_name);
        mTvSideMenuAddress = (TextView) findViewById(R.id.sidemenu_address);

        mTvSideMenuName.setText("Catioro");
        mTvSideMenuAddress.setText("São Paulo, SP");

        Glide.with(this)
                .load("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcTEH_PtPQloEqKvOToQB8uH3KFx0QljOq9kY6re2LL6fUsh-BkEbX5-LH0")
                .centerCrop()
                .dontAnimate()
                .into(mCivSideMenuPicture);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * On Select Sidemenu item
     * @param id
     * @return
     */
    public boolean onMenuItemSelected(int id) {
        if (id == mMiSearch.getId()) {
            Log.d("ITEM SELECTED", "SEARCH");
        } else if (id == mMiCalendar.getId()) {
            Log.d("ITEM SELECTED", "CALENDAR");
        } else if (id == mMiPayments.getId()) {
            Log.d("ITEM SELECTED", "PAYMENT");
        } else if (id == mMiFavorites.getId()) {
            Log.d("ITEM SELECTED", "FAVORITE");
        } else if (id == mMiSettings.getId()) {
            Log.d("ITEM SELECTED", "SETTING");
        } else if (id == mMiLogout.getId()) {
            Log.d("ITEM SELECTED", "LOGOUT");
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
