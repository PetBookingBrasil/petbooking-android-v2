package com.petbooking.UI.Dashboard.BusinessList;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Models.Business;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessListFragment extends Fragment {

    private BusinessService mBusinessService;
    private LocationManager mLocationManager;

    /**
     * Business List
     */
    private BusinessListAdapter mAdapter;
    private RecyclerView mRvBusinessList;
    private ArrayList<Business> mBusinessList;

    /**
     * Hightlight Business
     */
    private BusinessListAdapter mSliderAdapter;
    private RecyclerView mRvBusinessSlider;
    private ArrayList<Business> mBusinessSlider;

    public BusinessListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_list, container, false);

        mBusinessService = new BusinessService();
        mLocationManager = LocationManager.getInstance();

        mBusinessList = new ArrayList<>();
        mBusinessSlider = new ArrayList<>();
        mRvBusinessList = (RecyclerView) view.findViewById(R.id.businessList);
        mRvBusinessSlider = (RecyclerView) view.findViewById(R.id.businessSlider);
        mRvBusinessList.setHasFixedSize(true);
        mRvBusinessSlider.setHasFixedSize(true);

        LinearLayoutManager mVerticalLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager mHorizontalLayoutManager = new LinearLayoutManager(getContext());
        mVerticalLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHorizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);


        mRvBusinessList.setLayoutManager(mVerticalLayoutManager);
        mRvBusinessSlider.setLayoutManager(mHorizontalLayoutManager);
        mAdapter = new BusinessListAdapter(getContext(), mBusinessList);
        mSliderAdapter = new BusinessListAdapter(getContext(), mBusinessSlider);

        if (mAdapter != null) {
            mRvBusinessList.setAdapter(mAdapter);
        }

        if (mSliderAdapter != null) {
            mRvBusinessSlider.setAdapter(mSliderAdapter);
        }

        listBusiness();
        listHighlightsBusiness();

        return view;
    }


    /**
     * List Business
     */
    public void listBusiness() {
        mBusinessService.listBusiness(mLocationManager.getLocationCoords(), 1, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                BusinessesResp businessList = (BusinessesResp) response;
                Business business;
                for (BusinessesResp.Item item : businessList.data) {
                    business = new Business(item.id, item.attributes.name, item.attributes.city, item.attributes.state,
                            item.attributes.street, item.attributes.neighborhood, item.attributes.streetNumber, item.attributes.zipcode,
                            item.attributes.ratingAverage, item.attributes.ratingCount, item.attributes.distance, item.attributes.businesstype,
                            item.attributes.location.get(0), item.attributes.location.get(1), item.attributes.coverImage, item.attributes.imported);
                    mBusinessList.add(business);
                }

                mAdapter.updateList(mBusinessList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    /**
     * List Highlights Business
     */
    public void listHighlightsBusiness() {
        mBusinessService.listHighlightBusiness(new APICallback() {
            @Override
            public void onSuccess(Object response) {
                BusinessesResp businessList = (BusinessesResp) response;
                Business business;
                for (BusinessesResp.Item item : businessList.data) {
                    business = new Business(item.id, item.attributes.name, item.attributes.city, item.attributes.state,
                            item.attributes.street, item.attributes.neighborhood, item.attributes.streetNumber, item.attributes.zipcode,
                            item.attributes.ratingAverage, item.attributes.ratingCount, item.attributes.distance, item.attributes.businesstype,
                            "", "", item.attributes.coverImage, item.attributes.imported);
                    mBusinessSlider.add(business);

                }

                mSliderAdapter.updateList(mBusinessSlider);
                mSliderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object error) {
            }
        });
    }

}
