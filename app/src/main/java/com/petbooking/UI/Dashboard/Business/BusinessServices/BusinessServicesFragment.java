package com.petbooking.UI.Dashboard.Business.BusinessServices;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.petbooking.API.Business.BusinessService;
import com.petbooking.Models.Business;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.BusinessInformation.BusinessInformationFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessServicesFragment extends Fragment {

    private Context mContext;
    private BusinessService mBusinessService;
    private String businessId;
    private Business mBusiness;
    private AlertDialog mLoadingDialog;

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

        mBusinessService = new BusinessService();
        this.mContext = getContext();
        this.businessId = getArguments().getString("businessId", "0");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_services, container, false);
        return view;
    }

}
