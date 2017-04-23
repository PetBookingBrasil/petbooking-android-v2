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
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
        mHeaderView = mNavView.getHeaderView(0);

        /**
         * Update Sidemenu Info
         */

        mCivSideMenuPicture = (CircleImageView) mHeaderView.findViewById(R.id.sidemenu_picture);
        mTvSideMenuName = (TextView) mHeaderView.findViewById(R.id.sidemenu_name);
        mTvSideMenuAddress = (TextView) mHeaderView.findViewById(R.id.sidemenu_address);

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
