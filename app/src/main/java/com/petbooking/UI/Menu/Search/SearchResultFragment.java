package com.petbooking.UI.Menu.Search;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.BusinessList.BusinessListAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends Fragment {

    private OnBarClick mCallback;
    private BusinessService mBusinessService;
    private LocationManager mLocationManager;
    private String userId;
    private int currentPage = 1;

    private RelativeLayout mInfoBar;
    private TextView mInfoBarTitle;
    private ImageButton mBtnReset;
    private ImageButton mBtnNewSearch;

    private ArrayList<Business> mBusinessList;
    private RecyclerView mRvBusiness;
    private BusinessListAdapter mAdapter;

    View.OnClickListener mBtnResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallback.onReset();
        }
    };

    public SearchResultFragment() {
        // Required empty public constructor
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

        mBusinessList = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new BusinessListAdapter(getContext(), mBusinessList, Glide.with(getContext()));

        mRvBusiness = (RecyclerView) view.findViewById(R.id.result_list);
        mRvBusiness.setHasFixedSize(true);

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvBusiness.setLayoutManager(mLayoutManager);

        if (mAdapter != null) {
            mRvBusiness.setAdapter(mAdapter);
        }

        mBtnReset.setOnClickListener(mBtnResetListener);

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
        currentPage = 1;
        mBusinessService.listBusiness(mLocationManager.getLocationCoords(), userId, currentPage, 10, new APICallback() {
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


    public interface OnBarClick {

        void onReset();

        void onNewSearch();
    }

}
