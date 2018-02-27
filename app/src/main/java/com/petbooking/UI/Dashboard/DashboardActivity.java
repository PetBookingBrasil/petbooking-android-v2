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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Scheduling.ReviewSchedule;
import com.petbooking.UI.Dashboard.Content.ContentFragment;
import com.petbooking.UI.Dashboard.Notifications.NotificationsActivity;
import com.petbooking.UI.Dashboard.PaymentWebview.PaymentListActivity;
import com.petbooking.UI.Dialogs.FeedbackDialogFragment;
import com.petbooking.UI.Login.LoginActivity;
import com.petbooking.UI.Menu.Agenda.AgendaActivity;
import com.petbooking.UI.Menu.Favorites.FavoritesActivity;
import com.petbooking.UI.Menu.Pets.PetsActivity;
import com.petbooking.UI.Menu.Profile.ProfileActivity;
import com.petbooking.UI.Menu.Search.SearchActivity;
import com.petbooking.UI.Menu.Search.SearchResultFragment;
import com.petbooking.UI.Menu.Settings.SettingsActivity;
import com.petbooking.UI.Widget.CircleTransformation;
import com.petbooking.Utils.APIUtils;

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
    ActionBarDrawerToggle toggle;

    /**
     * Sidemenu content
     */
    private User currentUser;
    private ImageButton mIBtnProfile;
    private ImageView mIvSideMenuPicture;
    private TextView mTvSideMenuName;
    private TextView mTvSideMenuAddress;
    TextView toolbarTitle;

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
    private BusinessService mBusinessService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mSessionManager = SessionManager.getInstance();
        mLocationManager = LocationManager.getInstance();
        mLocationManager.setCallback(locationCallback);
        mFragmentManager = getSupportFragmentManager();

        mBusinessService = new BusinessService();

        mFeedbackDialogFragment = FeedbackDialogFragment.newInstance();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.dashboard_title);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = getResources().getString(R.string.dashboard_title);
                if(!toolbarTitle.getText().toString().equals(text)){
                    onBackPressed();
                }else{
                    if(!mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            }
        });


        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(this);

        mHeaderView = mNavView.getHeaderView(0);

        /**
         * Update Sidemenu Info
         */

        mIBtnProfile = (ImageButton) mHeaderView.findViewById(R.id.profileButton);
        mIvSideMenuPicture = (ImageView) mHeaderView.findViewById(R.id.sidemenu_picture);
        mTvSideMenuName = (TextView) mHeaderView.findViewById(R.id.sidemenu_name);
        mTvSideMenuAddress = (TextView) mHeaderView.findViewById(R.id.sidemenu_address);
        mIBtnProfile.setOnClickListener(btnProfileListener);

        inflateBusinessFragment();
        /*mBusinessService.getReviews("1", new APICallback() {
            @Override
            public void onSuccess(Object response) {
                Log.i(getClass().getSimpleName(),"On Sucess");
            }

            @Override
            public void onError(Object error) {
                Log.i(getClass().getSimpleName(),"On error");
            }
        });*/
        //inflateSearchResultFragment();
    }

    public void setTitle(String title){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);
        toolbarTitle.setText(title);
        contentFragment.setChangeNameTab(" "+getString(R.string.list_tab_name));
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
            getSupportActionBar().setHomeButtonEnabled(true);
            toggle.syncState();
            toolbarTitle.setText(R.string.dashboard_title);
            contentFragment.setChangeNameTab("  " + getString(R.string.tab_category));
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.cart).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            Intent activity = new Intent(this, SearchActivity.class);
            startActivityForResult(activity, SEARCH_REQUEST);
        } else if (item.getItemId() == R.id.notifications) {
            Log.d("ITEM SELECTED", "NOTIFICATIONS");
            Intent activity = new Intent(this, NotificationsActivity.class);
            startActivity(activity);
        }else if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent activity;

        if (id == R.id.my_pets) {
            activity = new Intent(this, PetsActivity.class);
            startActivity(activity);
        } else if (id == R.id.payments) {
            activity = new Intent(this, PaymentListActivity.class);
            startActivity(activity);
        } else if (id == R.id.favorites) {
            activity = new Intent(this, FavoritesActivity.class);
            startActivity(activity);
        } else if (id == R.id.settings) {
            activity = new Intent(this, SettingsActivity.class);
            startActivity(activity);
        } else if (id == R.id.calendar) {
            activity = new Intent(this, AgendaActivity.class);
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

            filterText = data.getStringExtra("FILTER_TEXT");
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
        int userAvatar;

        if (currentUser.gender == null || currentUser.gender.equals(User.GENDER_MALE)) {
            userAvatar = R.drawable.ic_placeholder_man;
        } else {
            userAvatar = R.drawable.ic_placeholder_woman;
        }

        mTvSideMenuName.setText(currentUser.name);
        mUserAddress = mLocationManager.getLocationCityState();

        if (mUserAddress != null) {
            mTvSideMenuAddress.setText(mUserAddress);
        } else if (currentUser.city != null && currentUser.state != null) {
            mTvSideMenuAddress.setText(currentUser.city + ", " + currentUser.state);
        } else {
            mTvSideMenuAddress.setText(R.string.prompt_loading);
        }

        if (currentUser.avatar.url.contains(APIConstants.FALLBACK_TAG)) {
            mIvSideMenuPicture.setImageResource(userAvatar);
            return;
        }

        Glide.with(this)
                .load(APIUtils.getAssetEndpoint(currentUser.avatar.url))
                .placeholder(userAvatar)
                .error(userAvatar)
                .bitmapTransform(new CircleTransformation(this))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(mIvSideMenuPicture);
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
    public void onNewSearch(String filterText, String categoryId) {
        Intent activity = new Intent(this, SearchActivity.class);
        activity.putExtra("newSearch", true);
        activity.putExtra("filterText", filterText);
        activity.putExtra("categoryId", categoryId);
        startActivityForResult(activity, SEARCH_REQUEST);
    }
}
