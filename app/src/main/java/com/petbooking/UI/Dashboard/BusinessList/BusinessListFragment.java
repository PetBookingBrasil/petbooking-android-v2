package com.petbooking.UI.Dashboard.BusinessList;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessListFragment extends Fragment {

    private BusinessService mBusinessService;
    private LocationManager mLocationManager;
    private String userId;

    /**
     * Business List
     */
    private LinearLayoutManager mVerticalLayoutManager;
    private BusinessListAdapter mAdapter;
    private RecyclerView mRvBusinessList;
    private ArrayList<Business> mBusinessList;

    /**
     * Hightlight Business
     */
    private LinearLayoutManager mHorizontalLayoutManager;
    private BusinessListAdapter mSliderAdapter;
    private RecyclerView mRvBusinessSlider;
    private ArrayList<Business> mBusinessSlider;


    /**
     * Auto Scroll
     */
    private int speedScroll = 8000;
    private Handler mHandler;
    private Runnable mRunnable;

    /**
     * Pagination
     */
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            int lastItemPosition = mVerticalLayoutManager.findLastVisibleItemPosition();

            if ((lastItemPosition == mAdapter.getItemCount() - 3) || (lastItemPosition == mAdapter.getItemCount() - 1)) {

                if (!isLastPage) {
                    currentPage++;
                    loadMore();
                }
            }
        }
    };

    public BusinessListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_list, container, false);

        mBusinessService = new BusinessService();
        mLocationManager = LocationManager.getInstance();
        userId = SessionManager.getInstance().getUserLogged().id;

        mBusinessList = new ArrayList<>();
        mBusinessSlider = new ArrayList<>();
        mRvBusinessList = (RecyclerView) view.findViewById(R.id.businessList);
        mRvBusinessSlider = (RecyclerView) view.findViewById(R.id.businessSlider);
        mRvBusinessList.setHasFixedSize(true);
        mRvBusinessSlider.setHasFixedSize(true);

        mVerticalLayoutManager = new LinearLayoutManager(getContext());
        mHorizontalLayoutManager = new LinearLayoutManager(getContext());
        mVerticalLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHorizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRvBusinessList.setLayoutManager(mVerticalLayoutManager);
        mRvBusinessSlider.setLayoutManager(mHorizontalLayoutManager);
        mAdapter = new BusinessListAdapter(getContext(), mBusinessList, Glide.with(getContext()));
        mSliderAdapter = new BusinessListAdapter(getContext(), mBusinessSlider, Glide.with(getContext()));

        if (mAdapter != null) {
            mRvBusinessList.setAdapter(mAdapter);
        }

        if (mSliderAdapter != null) {
            mRvBusinessSlider.setAdapter(mSliderAdapter);
        }

        mRvBusinessList.addOnScrollListener(scrollListener);

        listBusiness();
        listHighlightsBusiness();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listBusiness();
        listHighlightsBusiness();
    }

    /**
     * List Business
     */
    public void listBusiness() {
        currentPage = 1;
        mBusinessService.listBusiness(mLocationManager.getLocationCoords(), userId, currentPage, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mBusinessList = (ArrayList<Business>) response;

                mAdapter.updateList(mBusinessList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    /**
     * Load More Itens
     */
    public synchronized void loadMore() {
        isLoading = true;
        mBusinessService.listBusiness(mLocationManager.getLocationCoords(), userId, currentPage, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                ArrayList<Business> nextPage = (ArrayList<Business>) response;
                mBusinessList.addAll(nextPage);

                mAdapter.updateList(mBusinessList);
                mAdapter.notifyDataSetChanged();
                if (nextPage.size() == 0) {
                    isLastPage = true;
                }
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
        mBusinessService.listHighlightBusiness(userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mBusinessSlider = (ArrayList<Business>) response;

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
