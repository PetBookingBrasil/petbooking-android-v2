package com.petbooking.UI.Dashboard.Business.BusinessServices;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petbooking.Models.Business;
import com.petbooking.Models.BusinessServices;
import com.petbooking.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessServicesFragment extends Fragment {

    private Context mContext;
    private com.petbooking.API.Business.BusinessService mBusinessService;
    private String businessId;
    private Business mBusiness;
    private AlertDialog mLoadingDialog;

    /**
     * BusinessServices Components
     */
    private ArrayList<BusinessServices> mServiceList;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRvServices;
    private ServiceListAdapter mAdapter;

    public BusinessServicesFragment() {
        // Required empty public constructor
    }

    public static BusinessServicesFragment newInstance(String id) {
        BusinessServicesFragment fragment = new BusinessServicesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("businessId", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBusinessService = new com.petbooking.API.Business.BusinessService();
        this.mContext = getContext();
        this.businessId = getArguments().getString("businessId", "0");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_services, container, false);


        mServiceList = new ArrayList<>();
        prepareListData();
        mRvServices = (RecyclerView) view.findViewById(R.id.service_list);
        mAdapter = new ServiceListAdapter(mContext, mServiceList);

        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRvServices.setHasFixedSize(true);
        mRvServices.setLayoutManager(mLayoutManager);

        if (mAdapter != null) {
            mRvServices.setAdapter(mAdapter);
        }

        return view;
    }

    private void prepareListData() {
        BusinessServices service;
        ArrayList<BusinessServices> sub = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sub = new ArrayList<>();
            service = new BusinessServices(i + "", "Serviço " + i, i + "", "Serviço Número " + i, i * 20.33);
            sub.add(new BusinessServices(i + "", "SB " + i, i + "", "SB Número " + i, i * 20.33));
            sub.add(new BusinessServices(i + "", "SB " + i, i + "", "SB Número " + i, i * 20.33));
            sub.add(new BusinessServices(i + "", "SB " + i, i + "", "SB Número " + i, i * 20.33));
            service.setAdditionalServices(sub);
            mServiceList.add(service);
        }
    }

}
