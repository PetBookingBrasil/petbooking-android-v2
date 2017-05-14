package com.petbooking.UI.Dashboard.BusinessList;


import android.os.Bundle;
import android.os.Handler;
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


    /**
     * Auto Scroll
     */

    private int speedScroll = 8000;
    private Handler mHandler;
    private Runnable mRunnable;

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
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
                setAutoScroll();
            }

            @Override
            public void onError(Object error) {
            }
        });
    }

    /**
     * Set Auto Scroll
     */
    public void setAutoScroll() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            int count = 0;
            boolean flag = true;

            @Override
            public void run() {
                if (count < mSliderAdapter.getItemCount()) {
                    if (count == mSliderAdapter.getItemCount() - 1) {
                        flag = false;
                    } else if (count == 0) {
                        flag = true;
                    }
                    if (flag) count++;
                    else count--;

                    mRvBusinessSlider.smoothScrollToPosition(count);
                    mHandler.postDelayed(this, speedScroll);
                }
            }
        };

        mHandler.postDelayed(mRunnable, speedScroll);
    }

}
