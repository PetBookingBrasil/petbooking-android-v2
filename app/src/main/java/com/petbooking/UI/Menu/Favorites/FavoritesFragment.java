package com.petbooking.UI.Menu.Favorites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.petbooking.API.User.UserService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.BusinessList.BusinessListAdapter;
import com.petbooking.UI.Dashboard.DashboardActivity;
import com.petbooking.UI.Menu.Agenda.AgendaActivity;

import java.util.ArrayList;

/**
 * Created by Victor on 19/03/18.
 */

public class FavoritesFragment extends Fragment{

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favorites, container, false);

        mUserService = new UserService();
        mLocationManager = LocationManager.getInstance();
        userId = SessionManager.getInstance().getUserLogged().id;


        mFavoriteList = new ArrayList<>();
        listFavorites();
        mFavoritesPlaceholder = view.findViewById(R.id.favorites_placeholder);
        mRvFavorites = (RecyclerView) view.findViewById(R.id.favorites_list);
        Button viewBussines = (Button) view.findViewById(R.id.view_bussines);
        viewBussines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });
        mAdapter = new BusinessListAdapter(getActivity(), mFavoriteList, Glide.with(this));
        mAdapter.setFavoriteList(true);
        mAdapter.setOnFavoriteAction(favoriteListener);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvFavorites.setHasFixedSize(true);
        mRvFavorites.setLayoutManager(mLayoutManager);

        if (mAdapter != null) {
            mRvFavorites.setAdapter(mAdapter);
        }
        return view;
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
