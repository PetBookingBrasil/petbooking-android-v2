package com.petbooking.UI.Dashboard.Business.BusinessServices;


import android.animation.ObjectAnimator;
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

import com.petbooking.API.Business.APIBusinessConstants;
import com.petbooking.Models.Business;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Category;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessServicesFragment extends Fragment implements CategoryListAdapter.OnItemClick {

    private Context mContext;
    private com.petbooking.API.Business.BusinessService mBusinessService;
    private String businessId;
    private Business mBusiness;
    private AlertDialog mLoadingDialog;

    /**
     * BusinessServices Components
     */
    private ArrayList<Category> mCategoryList;
    private ArrayList<BusinessServices> mServiceList;
    private LinearLayoutManager mLayoutManager;
    private LinearLayoutManager mCategoryLayout;
    private RecyclerView mRvCategory;
    private RecyclerView mRvServices;
    private CategoryListAdapter mCategoryAdapter;
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
        mCategoryList = new ArrayList<>();
        prepareListData();
        initCategoryList();

        mRvCategory = (RecyclerView) view.findViewById(R.id.category_list);
        mRvServices = (RecyclerView) view.findViewById(R.id.service_list);

        mCategoryAdapter = new CategoryListAdapter(mContext, mCategoryList, this);
        mAdapter = new ServiceListAdapter(mContext, mServiceList);

        mLayoutManager = new LinearLayoutManager(mContext);
        mCategoryLayout = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCategoryLayout.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRvServices.setHasFixedSize(true);
        mRvServices.setLayoutManager(mLayoutManager);
        mRvCategory.setHasFixedSize(true);
        mRvCategory.setLayoutManager(mCategoryLayout);

        if (mAdapter != null) {
            mRvServices.setAdapter(mAdapter);
        }

        if (mCategoryAdapter != null) {
            mRvCategory.setAdapter(mCategoryAdapter);
        }

        mRvCategory.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);

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

    private void initCategoryList() {
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_CLINIC, R.drawable.ic_category_clinic));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_TRAINING, R.drawable.ic_category_trainer));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_BATH, R.drawable.ic_category_bath));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_TRANSPORT, R.drawable.ic_category_transport));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_WALKER, R.drawable.ic_category_walker));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_DAYCARE, R.drawable.ic_category_daycare));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_HOTEL, R.drawable.ic_category_hotel));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_EMERGENCY, R.drawable.ic_category_emergency));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_EXAMS, R.drawable.ic_category_exam));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_HOSPITAL, R.drawable.ic_category_hospital));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_DIAGNOSIS, R.drawable.ic_category_diagnosis));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_PETSHOP, R.drawable.ic_category_petshop));
        mCategoryList.add(new Category("1", APIBusinessConstants.DATA_PETSHOP_MOVEL, R.drawable.ic_category_petshop_delivery));
        mCategoryList.add(new Category("1", "Outros", R.drawable.ic_category_other));
    }

    @Override
    public void onItemClick(int position) {
        mCategoryLayout.scrollToPositionWithOffset(position, 10);
    }
}
