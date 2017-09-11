package com.petbooking.UI.Menu.Favorites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.petbooking.API.User.UserService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.BusinessList.BusinessListAdapter;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private UserService mUserService;
    private LocationManager mLocationManager;
    private String userId;

    private View mFavoritesPlaceholder;

    private ArrayList<Business> mFavoriteList;
    private LinearLayoutManager mLayoutManager;
    private BusinessListAdapter mAdapter;
    private RecyclerView mRvFavorites;

    private BusinessListAdapter.OnFavoriteAction favoriteListener = new BusinessListAdapter.OnFavoriteAction() {
        @Override
        public void onDelete(int position) {
            if (mFavoriteList.size() == 0) {
                mFavoritesPlaceholder.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mUserService = new UserService();
        mLocationManager = LocationManager.getInstance();
        userId = SessionManager.getInstance().getUserLogged().id;

        getSupportActionBar().setElevation(0);

        mFavoriteList = new ArrayList<>();
        listFavorites();
        mFavoritesPlaceholder = findViewById(R.id.favorites_placeholder);
        mRvFavorites = (RecyclerView) findViewById(R.id.favorites_list);
        mAdapter = new BusinessListAdapter(this, mFavoriteList, Glide.with(this));
        mAdapter.setFavoriteList(true);
        mAdapter.setOnFavoriteAction(favoriteListener);

        mLayoutManager = new LinearLayoutManager(this);

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvFavorites.setHasFixedSize(true);
        mRvFavorites.setLayoutManager(mLayoutManager);

        if (mAdapter != null) {
            mRvFavorites.setAdapter(mAdapter);
        }
    }

    /**
     * List Favorites Business
     */
    public void listFavorites() {
        mUserService.listFavorites(userId, mLocationManager.getLocationCoords(), 1, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mFavoriteList = (ArrayList<Business>) response;

                if (mFavoriteList.size() > 0) {
                    mAdapter.updateList(mFavoriteList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mFavoritesPlaceholder.setVisibility(View.VISIBLE);
                    mRvFavorites.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}
