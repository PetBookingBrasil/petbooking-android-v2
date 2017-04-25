package com.petbooking.UI.Dashboard;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.User;
import com.petbooking.Models.UserAddress;
import com.petbooking.R;
import com.petbooking.UI.Menu.Profile.ProfileActivity;
import com.petbooking.UI.Dialogs.FeedbackDialogFragment;
import com.petbooking.UI.Login.LoginActivity;
import com.petbooking.Utils.APIUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, FeedbackDialogFragment.FinishDialogListener {

    private boolean permissionDenied = false;
    private static final int RC_PERMISSION = 234;

    private SessionManager mSessionManager;
    private LocationManager mLocationManager;

    private FeedbackDialogFragment mFeedbackDialogFragment;
    private UserAddress mUserAddress;


    private FragmentManager mFragmentManager;
    NavigationView mNavView;
    View mHeaderView;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;

    /**
     * Sidemenu content
     */
    private User currentUser;
    private ImageButton mIBtnProfile;
    private CircleImageView mCivSideMenuPicture;
    private TextView mTvSideMenuName;
    private TextView mTvSideMenuAddress;

    View.OnClickListener btnProfileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToProfile();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mSessionManager = SessionManager.getInstance();
        mLocationManager = LocationManager.getInstance();
        mFragmentManager = getSupportFragmentManager();

        mFeedbackDialogFragment = FeedbackDialogFragment.newInstance();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(this);

        mHeaderView = mNavView.getHeaderView(0);

        /**
         * Update Sidemenu Info
         */

        mIBtnProfile = (ImageButton) mHeaderView.findViewById(R.id.profileButton);
        mCivSideMenuPicture = (CircleImageView) mHeaderView.findViewById(R.id.sidemenu_picture);
        mTvSideMenuName = (TextView) mHeaderView.findViewById(R.id.sidemenu_name);
        mTvSideMenuAddress = (TextView) mHeaderView.findViewById(R.id.sidemenu_address);
        mIBtnProfile.setOnClickListener(btnProfileListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (permissionDenied) {
            permissionDenied = false;
            mFeedbackDialogFragment.setDialogInfo(R.string.permission_location_title, R.string.permission_location,
                    R.string.dialog_button_ok, AppConstants.OK_ACTION);
            mFeedbackDialogFragment.show(mFragmentManager, "LOCATION_PERMISSION");
        }

        updateUserInfo();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        checkLocationPermission();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.payments) {
            Log.d("PAYMENTS", "PAYMENTS");
        } else if (id == R.id.logout) {
            Intent logoutIntent = new Intent(this, LoginActivity.class);
            startActivity(logoutIntent);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION) {
            if (ContextCompat.checkSelfPermission(this, AppConstants.PERMISSION_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocation();
            } else {
                permissionDenied = true;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onFinishDialog(int action) {
        if (action == AppConstants.OK_ACTION) {
            checkLocationPermission();
        }
    }

    /**
     * Request Location Permission
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, AppConstants.PERMISSION_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{AppConstants.PERMISSION_LOCATION}, RC_PERMISSION);
        }
    }

    /**
     * Go to Profile Screen
     */
    private void goToProfile() {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    /**
     * Update User Info
     */
    private void updateUserInfo() {
        currentUser = mSessionManager.getUserLogged();

        mTvSideMenuName.setText(currentUser.name);
        mUserAddress = mLocationManager.getAddress();

        if (mUserAddress != null) {
            mTvSideMenuAddress.setText(mUserAddress.city + ", " + mUserAddress.state);
        } else if (currentUser.city != null && currentUser.state != null) {
            mTvSideMenuAddress.setText(currentUser.city + ", " + currentUser.state);
        } else {
            mTvSideMenuAddress.setText(R.string.prompt_loading);
        }

        Glide.with(this)
                .load(APIUtils.getAssetEndpoint(currentUser.avatar.large.url))
                .centerCrop()
                .dontAnimate()
                .into(mCivSideMenuPicture);
    }
}
