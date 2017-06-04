package com.petbooking.UI.Menu.Favorites;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.petbooking.API.User.UserService;
import com.petbooking.BaseActivity;
import com.petbooking.Events.HideLoadingEvt;
import com.petbooking.Events.ShowLoadingEvt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.R;

import java.util.ArrayList;

public class FavoritesActivity extends BaseActivity {

    private UserService mUserService;
    private LocationManager mLocationManager;
    private String userId;

    private ArrayList<Business> mFavoriteList;
    private LinearLayoutManager mLayoutManager;
    private FavoritesListAdapter mAdapter;
    private RecyclerView mRvFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mUserService = new UserService();
        mLocationManager = LocationManager.getInstance();
        userId = SessionManager.getInstance().getUserLogged().id;

        mFavoriteList = new ArrayList<>();
        listFavorites();
        mRvFavorites = (RecyclerView) findViewById(R.id.favorites_list);
        mAdapter = new FavoritesListAdapter(this, mFavoriteList);
        mLayoutManager = new LinearLayoutManager(this);

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvFavorites.setHasFixedSize(true);
        mRvFavorites.setLayoutManager(mLayoutManager);
        mRvFavorites.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        if (mAdapter != null) {
            mRvFavorites.setAdapter(mAdapter);
        }
    }

    public void listFavorites() {
        mUserService.listFavorites(userId, mLocationManager.getLocationCoords(), 1, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mFavoriteList = (ArrayList<Business>) response;
                mAdapter.updateList(mFavoriteList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object error) {

            }
        });
    }
}
