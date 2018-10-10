package com.petbooking.UI.Menu.Search;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.App;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.BusinessList.BusinessListAdapter;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends Fragment {

    private static final int PAGE_SIZE = 10;

    private View mBusinessPlaceholder;

    private Context mContext;
    private OnBarClick mCallback;
    private BusinessService mBusinessService;
    private LocationManager mLocationManager;
    private String userId;

    private String filterText;
    private String categoryId;
    private String categoryName;

    private RelativeLayout mInfoBar;
    private TextView mInfoBarTitle;
    private ImageButton mBtnReset;
    private ImageButton mBtnNewSearch;

    private ArrayList<Business> mBusinessList;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRvBusiness;
    private BusinessListAdapter mAdapter;

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean location;

    View.OnClickListener mBtnResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallback.onReset();
        }
    };
    View.OnClickListener mBtnNewSearchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallback.onNewSearch(filterText, categoryId,location);
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
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                    loadMore();
                }
            }
        }
    };

    public SearchResultFragment() {
        // Required empty public constructor
    }

    public static SearchResultFragment newInstance(String filterText, String categoryId, String categoryName, boolean location) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("filterText", filterText);
        bundle.putString("categoryId", categoryId);
        bundle.putString("categoryName", categoryName);
        bundle.putBoolean("location", location);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBusinessService = new BusinessService();
        this.mContext = getContext();
        this.filterText = getArguments().getString("filterText");
        this.categoryId = getArguments().getString("categoryId");
        this.categoryName = getArguments().getString("categoryName");
        this.location = getArguments().getBoolean("location",false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);


        mBusinessService = new BusinessService();
        mLocationManager = LocationManager.getInstance();
        userId = SessionManager.getInstance().getUserLogged().id;

        mInfoBar = (RelativeLayout) view.findViewById(R.id.info_bar);
        mInfoBarTitle = (TextView) view.findViewById(R.id.info_bar_title);
        mBtnReset = (ImageButton) view.findViewById(R.id.reset_button);
        mBtnNewSearch = (ImageButton) view.findViewById(R.id.new_search_button);
        mBusinessPlaceholder = view.findViewById(R.id.business_placeholder);

        if (categoryName != null && !TextUtils.isEmpty(categoryName)) {
            int categoryColor = AppUtils.getCategoryColor(mContext, categoryName);
            mInfoBar.setBackgroundColor(categoryColor);
            mInfoBarTitle.setText(AppUtils.getCategoryText(categoryName));
        } else {
            mInfoBarTitle.setText(filterText);
        }

        mBusinessList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new BusinessListAdapter(getContext(), mBusinessList, Glide.with(getContext()));

        mRvBusiness = (RecyclerView) view.findViewById(R.id.result_list);
        mRvBusiness.setHasFixedSize(true);

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvBusiness.setLayoutManager(mLayoutManager);

        if (mAdapter != null) {
            mRvBusiness.setAdapter(mAdapter);
        }

        mRvBusiness.addOnScrollListener(scrollListener);
        mBtnReset.setOnClickListener(mBtnResetListener);
        mBtnNewSearch.setOnClickListener(mBtnNewSearchListener);

        listBusiness();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnBarClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnBarClick");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    /**
     * List Business
     */
    public void listBusiness() {
        AppUtils.showLoadingDialog(getContext());
        isLoading = true;
        currentPage = 1;
        mBusinessService.searchBusiness(mLocationManager.getLocationCoords(), userId, currentPage, PAGE_SIZE, categoryId, filterText, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mBusinessList = (ArrayList<Business>) response;

                if (mBusinessList.size() != 0) {
                    mBusinessPlaceholder.setVisibility(View.GONE);
                    mRvBusiness.setVisibility(View.VISIBLE);
                } else {
                    mBusinessPlaceholder.setVisibility(View.VISIBLE);
                    mRvBusiness.setVisibility(View.GONE);
                }

                mAdapter.updateList(mBusinessList);
                mAdapter.notifyDataSetChanged();

                isLoading = false;
                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                isLoading = false;
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * List Business
     */
    public void loadMore() {
        AppUtils.showLoadingDialog(getContext());
        currentPage++;
        isLoading = true;
        mBusinessService.searchBusiness(mLocationManager.getLocationCoords(), userId, currentPage, PAGE_SIZE, categoryId, filterText, new APICallback() {
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

    public interface OnBarClick {

        void onReset();

        void onNewSearch(String filterText, String categoryId, boolean location);
    }

}
