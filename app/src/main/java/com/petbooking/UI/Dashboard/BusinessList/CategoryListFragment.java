package com.petbooking.UI.Dashboard.BusinessList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.petbooking.API.Business.Models.BannerResponse;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Banner;
import com.petbooking.Models.Category;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.DashboardActivity;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;


import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victor on 15/01/18.
 */

public class CategoryListFragment extends android.support.v4.app.Fragment {
    //List
    @BindView(R.id.listCategorys)
    RecyclerView categorys;

    @BindView(R.id.layout_banner)
    LinearLayout bannersLayout;

    @BindView(R.id.pagerBanner)
    ViewPager bannerList;

    @BindView(R.id.container)
    RelativeLayout container;


    Context context;
    //List
    private ArrayList<Category> mCategoryList;
    private ArrayList<Banner> banners;

    //Business
    private com.petbooking.API.Business.BusinessService mBusinessService;
    private String businessId = null;

    //Adapter
    CategoryListAdapter categoryAdapter;
    BannerListAdapter bannerAdapter;

    public CategoryListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryList = new ArrayList<>();
        banners = new ArrayList<>();
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
        bannerAdapter = new BannerListAdapter(getFragmentManager());
        bannerAdapter.setFragment(this);
        bannersLayout.setVisibility(View.VISIBLE);
        banners.clear();
        getCategories();
        getBanners();
        container.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
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
                configureCategoriesIcons();
                categoryAdapter.notifyDataSetChanged();
                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    public void getBanners() {
        mBusinessService.listBanners(new APICallback() {
            @Override
            public void onSuccess(Object response) {
                BannerResponse bannerResponse = (BannerResponse) response;
                Log.i("Teste", "Qual o sise aqui " + bannerResponse.data.size());
                for (BannerResponse.Item item : bannerResponse.data) {
                    Banner banner = new Banner();
                    banner.setId(item.id);
                    banner.setTitle(item.attributes.title);
                    banner.setImage(item.attributes.image_url);
                    banners.add(banner);
                }
                bannerAdapter.setBanners(banners);
                bannerList.setAdapter(bannerAdapter);
            }

            @Override
            public void onError(Object error) {
                bannersLayout.setVisibility(View.GONE);
            }
        });

    }

    private void configureCategoriesIcons(){
        HashMap<String,String> icons = new HashMap<>();
        for (int i = 0; i < mCategoryList.size(); i++){
            icons.put(mCategoryList.get(i).categoryName,mCategoryList.get(i).iconUrl);
        }
        CommonUtils.icons = icons;
    }

    public void replaceFragment(String categoryId, String categoryName,Category category){
        android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        BusinessListFragment businessListFragment = new BusinessListFragment(category);
        if(category == null){
            businessListFragment.setPromo(true);
        }
        businessListFragment.setCategoryId(categoryId);
        transaction.replace(R.id.root_frame, businessListFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(getClass().getSimpleName());
        transaction.commit();
        DashboardActivity activity =(DashboardActivity) getActivity();
        activity.setTitle(categoryName);
    }
}
