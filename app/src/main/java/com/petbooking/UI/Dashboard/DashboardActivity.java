package com.petbooking.UI.Dashboard;

import android.content.Intent;
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
import com.petbooking.Components.DrawerItem;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Login.LoginActivity;
import com.petbooking.Utils.APIUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    private SessionManager mSessionManager;

    NavigationView mNavView;
    View mHeaderView;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;

    /**
     * Sidemenu content
     */
    private User currentUser;
    private CircleImageView mCivSideMenuPicture;
    private TextView mTvSideMenuName;
    private TextView mTvSideMenuAddress;

    /**
     * Sidemenu Itens
     */

    private DrawerItem mMiPets;
    private DrawerItem mMiSearch;
    private DrawerItem mMiCalendar;
    private DrawerItem mMiPayments;
    private DrawerItem mMiFavorites;
    private DrawerItem mMiSettings;
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
        setContentView(R.layout.activity_dashboard);

        mSessionManager = SessionManager.getInstance();
        currentUser = mSessionManager.getUserLogged();

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
        mMiPets = (DrawerItem) findViewById(R.id.my_pets);
        mMiSearch = (DrawerItem) findViewById(R.id.search);
        mMiCalendar = (DrawerItem) findViewById(R.id.calendar);
        mMiPayments = (DrawerItem) findViewById(R.id.payments);
        mMiFavorites = (DrawerItem) findViewById(R.id.favorites);
        mMiSettings = (DrawerItem) findViewById(R.id.settings);
        mMiLogout = (LinearLayout) findViewById(R.id.logout);

        mMiPets.setOnClickListener(mMenuItemListener);
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

        mTvSideMenuName.setText(currentUser.name);
        mTvSideMenuAddress.setText(currentUser.city + ", " + currentUser.state);

        Glide.with(this)
                .load(APIUtils.getAssetEndpoint(currentUser.avatar.large.url))
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
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.schedules) {
            Log.d("ITEM SELECTED", "CART");
        } else if (item.getItemId() == R.id.notifications) {
            Log.d("ITEM SELECTED", "NOTIFICATIONS");
        }

        return true;
    }

    /**
     * On Select Sidemenu item
     *
     * @param id
     * @return
     */
    public boolean onMenuItemSelected(int id) {
        if (id == mMiPets.getId()) {
            Log.d("ITEM SELECTED", "PETS");
        } else if (id == mMiSearch.getId()) {
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
            logout();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Logout
     */
    private void logout() {
        Intent logoutIntent = new Intent(this, LoginActivity.class);
        startActivity(logoutIntent);
    }
}
