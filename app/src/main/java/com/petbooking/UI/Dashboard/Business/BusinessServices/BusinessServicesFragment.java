package com.petbooking.UI.Dashboard.Business.BusinessServices;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petbooking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessServicesFragment extends Fragment {

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_business_services, container, false);
    }

}
