package com.petbooking.UI.Dashboard.BusinessList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.Models.Category;
import com.petbooking.R;
import com.petbooking.UI.Menu.Search.SearchActivity;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessListFragment extends Fragment {

    private static final int SEARCH_REQUEST = 235;
    private static final int PAGE_SIZE = 10;

    private BusinessService mBusinessService;
    private LocationManager mLocationManager;
    private String userId;

    private FloatingActionButton mFBSearch;

    /**
     * Business List
     */
    private LinearLayoutManager mVerticalLayoutManager;
    private BusinessListAdapter mAdapter;
    private RecyclerView mRvBusinessList;
    private ArrayList<Business> mBusinessList;

    /**
     * Pagination
     */
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean promo = false;
    private int currentPage = 1;
    String categoryId;

    //Model
    Category category;

    View.OnClickListener filterButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startFilterActivity();
        }
    };

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mVerticalLayoutManager.getChildCount();
            int totalItemCount = mVerticalLayoutManager.getItemCount();
            int firstVisibleItemPosition = mVerticalLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                    loadMore();
                }
            }
        }
    };


    public BusinessListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public BusinessListFragment(Category category){
        this.category = category;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setPromo(boolean promo) {
        this.promo = promo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_list, container, false);

        mBusinessService = new BusinessService();
        mLocationManager = LocationManager.getInstance();
        userId = SessionManager.getInstance().getUserLogged().id;

        mFBSearch = (FloatingActionButton) view.findViewById(R.id.float_filter_button);
        mFBSearch.setOnClickListener(filterButtonListener);

        mBusinessList = new ArrayList<>();
        mRvBusinessList = (RecyclerView) view.findViewById(R.id.businessList);
        mRvBusinessList.setHasFixedSize(true);

        mVerticalLayoutManager = new LinearLayoutManager(getContext());
        mVerticalLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRvBusinessList.setLayoutManager(mVerticalLayoutManager);
        mAdapter = new BusinessListAdapter(getContext(), mBusinessList, Glide.with(getContext()));

        if (mAdapter != null) {
            mRvBusinessList.setAdapter(mAdapter);
            mAdapter.setCategory(category);
        }

        mRvBusinessList.addOnScrollListener(scrollListener);
        if(!promo)
            listBusiness();
        else
            listBussinesByPromo();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!promo)
            listBusiness();
        else
            listBussinesByPromo();
    }

    /**
     * List Business
     */
    public void listBusiness() {
        currentPage = 1;
        AppUtils.showLoadingDialog(getContext());
        mBusinessService.listBussinesByCategory(mLocationManager.getLocationCoords(), userId, currentPage, PAGE_SIZE, categoryId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mBusinessList = (ArrayList<Business>) response;

                mAdapter.updateList(mBusinessList);
                mAdapter.notifyDataSetChanged();
                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    public void listBussinesByPromo() {
        currentPage = 1;
        LocationManager manager = LocationManager.getInstance();
        AppUtils.showLoadingDialog(getContext());
        mBusinessService.listPromos(categoryId, userId, currentPage, PAGE_SIZE,manager.getLatitude(), manager.getLongitude(), new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mBusinessList = (ArrayList<Business>) response;

                mAdapter.updateList(mBusinessList);
                mAdapter.notifyDataSetChanged();
                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                Log.i("Qual o erro aqui ", "Esta errado " + error.toString());
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * Load More Itens
     */
    public synchronized void loadMore() {
        AppUtils.showLoadingDialog(getContext());
        currentPage++;
        isLoading = true;
        mBusinessService.listBussinesByCategory(mLocationManager.getLocationCoords(), userId, currentPage, PAGE_SIZE, categoryId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                ArrayList<Business> nextPage = (ArrayList<Business>) response;
                mBusinessList.addAll(nextPage);

                mAdapter.updateList(mBusinessList);
                mAdapter.notifyDataSetChanged();

                if (nextPage.size() == 0) {
                    isLastPage = true;
                }

                isLoading = false;
                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                currentPage--;
                isLoading = false;
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * Start Activity for filter
     */
    public void startFilterActivity() {
        Intent activity = new Intent(getActivity(), SearchActivity.class);
        getActivity().startActivityForResult(activity, SEARCH_REQUEST);
    }
}
