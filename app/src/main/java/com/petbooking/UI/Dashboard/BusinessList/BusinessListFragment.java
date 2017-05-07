package com.petbooking.UI.Dashboard.BusinessList;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petbooking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessListFragment extends Fragment {


    public BusinessListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_business_list, container, false);
    }

}
