package com.petbooking.UI.Dashboard.BusinessList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Category;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Content.ContentTabsAdapter;
import com.petbooking.UI.Dashboard.DashboardActivity;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victor on 15/01/18.
 */

public class CategoryListFragment extends android.support.v4.app.Fragment {
    //List
    @BindView(R.id.listCategorys)
    RecyclerView categorys;

    Context context;
    //List
    private ArrayList<Category> mCategoryList;

    //Business
    private com.petbooking.API.Business.BusinessService mBusinessService;
    private String businessId = null;

    //Adapter
    CategoryListAdapter categoryAdapter;

    public CategoryListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryList = new ArrayList<>();
        mBusinessService = new com.petbooking.API.Business.BusinessService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_categories, container, false);
        ButterKnife.bind(this, view);
        categoryAdapter = new CategoryListAdapter(getActivity(),mCategoryList);
        categoryAdapter.setFragment(this);
        categorys.setLayoutManager(new GridLayoutManager(getActivity(),2));
        categorys.setAdapter(categoryAdapter);
        mCategoryList.clear();
        getCategories();
        return view;
    }

    public void getCategories() {
        AppUtils.showLoadingDialog(getActivity());
        mBusinessService.listCategories( new APICallback() {
            @Override
            public void onSuccess(Object response) {
                CategoryResp resp = (CategoryResp) response;
                for (CategoryResp.Item item : resp.data) {
                    mCategoryList.add(APIUtils.parseCategory(getActivity(), item));
                }

                categoryAdapter.setCategories(mCategoryList);
                categoryAdapter.notifyDataSetChanged();
                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    public void replaceFragment(String categoryId, String categoryName,Category category){
        android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        BusinessListFragment businessListFragment = new BusinessListFragment(category);
        businessListFragment.setCategoryId(categoryId);
        transaction.replace(R.id.root_frame, businessListFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(getClass().getSimpleName());
        transaction.commit();
        DashboardActivity activity =(DashboardActivity) getActivity();
        activity.setTitle(categoryName);
    }
}
