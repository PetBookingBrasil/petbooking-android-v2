package com.petbooking.UI.Menu.Favorites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.petbooking.Models.Business;
import com.petbooking.R;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private ArrayList<Business> mFavoriteList;
    private LinearLayoutManager mLayoutManager;
    private FavoritesListAdapter mAdapter;
    private RecyclerView mRvFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mFavoriteList = new ArrayList<>();
        initList();
        mRvFavorites = (RecyclerView) findViewById(R.id.favorites_list);
        mAdapter = new FavoritesListAdapter(this, mFavoriteList);
        mLayoutManager = new LinearLayoutManager(this);

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvFavorites.setHasFixedSize(true);
        mRvFavorites.setLayoutManager(mLayoutManager);

        if (mAdapter != null) {
            mRvFavorites.setAdapter(mAdapter);
        }
    }

    public void initList() {
        for (int i = 0; i < 10; i++) {
            mFavoriteList.add(new Business("" + i, "Business " + i));
        }
    }
}
