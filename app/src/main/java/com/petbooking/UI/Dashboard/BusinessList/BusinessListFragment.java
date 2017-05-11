package com.petbooking.UI.Dashboard.BusinessList;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.petbooking.API.Business.Models.BusinessService;
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

    private BusinessListAdapter mAdapter;
    private RecyclerView mRvBusinessList;
    private ArrayList<Business> mBusinessList;

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
        mRvBusinessList = (RecyclerView) view.findViewById(R.id.businessList);
        mRvBusinessList.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRvBusinessList.setLayoutManager(mLayoutManager);

        mAdapter = new BusinessListAdapter(getContext(), mBusinessList);

        if (mAdapter != null) {
            mRvBusinessList.setAdapter(mAdapter);
        }

        listBusiness();

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
                            item.attributes.location.get(0), item.attributes.location.get(1), item.attributes.imported);
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

}
