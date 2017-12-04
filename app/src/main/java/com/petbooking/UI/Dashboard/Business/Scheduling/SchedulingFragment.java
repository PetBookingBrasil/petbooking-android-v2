package com.petbooking.UI.Dashboard.Business.Scheduling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petbooking.API.Appointment.AppointmentService;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.API.Pet.PetService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Category;
import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter.PetAdapter;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter.ServiceAdapter;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by victorneves on 30/11/17.
 */

public class SchedulingFragment extends Fragment {
    RecyclerView mRecyclerView;
    private SectionedRecyclerViewAdapter mAdapter;
    PetAdapter petAdapter;
    ServiceAdapter serviceAdapter;

    private ArrayList<Pet> mPetList;
    private ArrayList<BusinessServices> mServiceList;
    private ArrayList<Category> mCategoryList;

    private AppointmentService mAppointmentService;
    private AppointmentManager mAppointmentManager;
    private com.petbooking.API.Business.BusinessService mBusinessService;
    private String userId;
    private String businessId = null;
    PetService mPetService;

    public static SchedulingFragment newInstance(String id) {
        SchedulingFragment fragment = new SchedulingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("businessId", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppointmentManager = AppointmentManager.getInstance();
        this.userId = SessionManager.getInstance().getUserLogged().id;
        mPetList = new ArrayList<>();
        mPetService = new PetService();
        mServiceList = new ArrayList<>();
        mAppointmentService = new AppointmentService();
        mCategoryList = new ArrayList<>();
        mBusinessService = new com.petbooking.API.Business.BusinessService();
        this.businessId = getArguments().getString("businessId", "0");

        if (businessId.equals("0") && mAppointmentManager.getCurrentBusinessId() != null) {
            this.businessId = mAppointmentManager.getCurrentBusinessId();
            mAppointmentManager.removeKey("businessId");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_scheduling, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listCategorys);
        mAdapter = new SectionedRecyclerViewAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<String> pets = new ArrayList<>();
        List<String> services = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String pet = "Pet numero " + i;
            pets.add(pet);
        }

        for (int i = 0; i < 10; i++) {
            String service = "Service numero " + i;
            services.add(service);
        }
        petAdapter = new PetAdapter("Pet", pets, this,mPetList,getActivity());
        serviceAdapter = new ServiceAdapter("Service", services,getContext(),mCategoryList);
        mAdapter.addSection(petAdapter);
        mAdapter.addSection(serviceAdapter);
        mRecyclerView.setAdapter(mAdapter);
        getPets();
        getCategories();

        return view;
    }

    public void notifyChanged(int position) {
        serviceAdapter.setExpanable(true);
        mAdapter.notifyDataSetChanged();
    }

    public void getPets() {
        AppUtils.showLoadingDialog(getContext());
        mPetService.listPets(userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mPetList = (ArrayList<Pet>) response;

                petAdapter.addPets(mPetList);
                mAdapter.notifyDataSetChanged();

                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    public void listServices(String categoryId, String petId) {
        AppUtils.showLoadingDialog(getContext());
        mAppointmentService.listServices(categoryId, petId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mServiceList = (ArrayList<BusinessServices>) response;

                //serviceAdapter.setServices(mServiceList);
                mAdapter.notifyDataSetChanged();


                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    public void getCategories() {
        mBusinessService.listBusinessCategories(businessId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                CategoryResp resp = (CategoryResp) response;
                for (CategoryResp.Item item : resp.data) {
                    mCategoryList.add(APIUtils.parseCategory(getContext(), item));
                }

                serviceAdapter.setServices(mCategoryList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }
}
