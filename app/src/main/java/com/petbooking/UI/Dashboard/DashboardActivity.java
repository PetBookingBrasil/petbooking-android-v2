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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Events.LocationChangedEvt;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Content.ContentFragment;
import com.petbooking.UI.Dialogs.FeedbackDialogFragment;
import com.petbooking.UI.Login.LoginActivity;
import com.petbooking.UI.Menu.Calendar.CalendarActivity;
import com.petbooking.UI.Menu.Favorites.FavoritesActivity;
import com.petbooking.UI.Menu.Pets.PetsActivity;
import com.petbooking.UI.Menu.Profile.ProfileActivity;
import com.petbooking.UI.Menu.Search.SearchActivity;
import com.petbooking.UI.Menu.Search.SearchResultFragment;
import com.petbooking.UI.Menu.Settings.SettingsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        FeedbackDialogFragment.FinishDialogListener,
        SearchResultFragment.OnBarClick {

    private boolean permissionDenied = false;
    private static final int RC_PERMISSION = 234;
    private static final int SEARCH_REQUEST = 235;

    private SessionManager mSessionManager;
    private LocationManager mLocationManager;

    private FeedbackDialogFragment mFeedbackDialogFragment;
    private String mUserAddress;

    private FragmentManager mFragmentManager;
    ContentFragment contentFragment;
    SearchResultFragment searchResultFragment;
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

    LocationManager.LocationReadyCallback locationCallback = new LocationManager.LocationReadyCallback() {
        @Override
        public void onLocationReady(String locationCityState) {
            mTvSideMenuAddress.setText(locationCityState);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mSessionManager = SessionManager.getInstance();
        mLocationManager = LocationManager.getInstance();
        mLocationManager.setCallback(locationCallback);
        mFragmentManager = getSupportFragmentManager();

        mFeedbackDialogFragment = FeedbackDialogFragment.newInstance();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        inflateBusinessFragment();
        //inflateSearchResultFragment();
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
        contentFragment.backToList();
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
        Intent activity;

        if (id == R.id.my_pets) {
            activity = new Intent(this, PetsActivity.class);
            startActivity(activity);
        } else if (id == R.id.search) {
            activity = new Intent(this, SearchActivity.class);
            startActivityForResult(activity, SEARCH_REQUEST);
        } else if (id == R.id.payments) {
            Log.d("PAYMENTS", "PAYMENTS");
        } else if (id == R.id.favorites) {
            activity = new Intent(this, FavoritesActivity.class);
            startActivity(activity);
        } else if (id == R.id.settings) {
            activity = new Intent(this, SettingsActivity.class);
            startActivity(activity);
        } else if (id == R.id.calendar) {
            activity = new Intent(this, CalendarActivity.class);
            startActivity(activity);
        } else if (id == R.id.logout) {
            mSessionManager.logout();
            activity = new Intent(this, LoginActivity.class);
            startActivity(activity);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEARCH_REQUEST && resultCode == RESULT_OK) {
            int categoryPosition = data.getIntExtra("CATEGORY_POSITION", -1);
            String categoryId = "";
            String categoryName = "";
            String filterText = "";

            categoryName = data.getStringExtra("FILTER_TEXT");
            if (categoryPosition != -1) {
                categoryName = data.getStringExtra("CATEGORY_NAME");
                categoryId = data.getStringExtra("CATEGORY_ID");
            }

            inflateSearchResultFragment(filterText, categoryId, categoryName);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
        mUserAddress = mLocationManager.getLocationCityState();

        if (mUserAddress != null) {
            mTvSideMenuAddress.setText(mUserAddress);
        } else if (currentUser.city != null && currentUser.state != null) {
            mTvSideMenuAddress.setText(currentUser.city + ", " + currentUser.state);
        } else {
            mTvSideMenuAddress.setText(R.string.prompt_loading);
        }

        Glide.with(this)
                .load(currentUser.avatar.large.url)
                .error(R.drawable.ic_placeholder_user)
                .placeholder(R.drawable.ic_placeholder_user)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .dontAnimate()
                .into(mCivSideMenuPicture);
    }

    /**
     * Inflate Business List
     */
    private void inflateBusinessFragment() {
        contentFragment = new ContentFragment();
        mFragmentManager.beginTransaction().replace(R.id.content_main, contentFragment).commit();
    }

    /**
     * Inflate Search Result List
     */
    private void inflateSearchResultFragment(String filterText, String categoryId, String categoryName) {
        searchResultFragment = SearchResultFragment.newInstance(filterText, categoryId, categoryName);
        mFragmentManager.beginTransaction().replace(R.id.content_main, searchResultFragment).commit();
    }


    @Override
    public void onReset() {
        inflateBusinessFragment();
    }

    @Override
    public void onNewSearch() {
        Intent activity = new Intent(this, SearchActivity.class);
        startActivityForResult(activity, SEARCH_REQUEST);
    }
}
