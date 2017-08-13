package com.petbooking.UI.Dashboard.Business.BusinessServices;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.petbooking.API.Appointment.AppointmentService;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.API.Pet.PetService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Managers.PreferenceManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Category;
import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.ServiceDetail.ServiceDetailActivity;
import com.petbooking.UI.Menu.Agenda.PetCalendarListAdapter;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessServicesFragment extends Fragment {

    private String userId;
    private Context mContext;
    private PetService mPetService;
    private AppointmentManager mAppointmentManager;
    private AppointmentService mAppointmentService;
    private com.petbooking.API.Business.BusinessService mBusinessService;

    /**
     * Flow Control
     */
    private Button mBtnFinishScheduling;
    private String businessId = null;
    private int selectedPet = -1;
    private int selectedCategory = -1;
    private int totalAppointments = 0;

    /**
     * Pet List
     */
    private RecyclerView mRvPets;
    private LinearLayoutManager mPetLayoutManager;
    private PetCalendarListAdapter mPetAdapter;
    private ArrayList<Pet> mPetList;

    /**
     * Services Components
     */
    private RecyclerView mRvServices;
    private ArrayList<BusinessServices> mServiceList;
    private LinearLayoutManager mServicesLayoutManager;
    private ServiceListAdapter mServiceAdapter;

    /**
     * Categories Components
     */
    private RecyclerView mRvCategory;
    private ArrayList<Category> mCategoryList;
    private LinearLayoutManager mCategoryLayout;
    private CategoryListAdapter mCategoryAdapter;


    CategoryListAdapter.OnSelectCategoryListener categoryListener = new CategoryListAdapter.OnSelectCategoryListener() {
        @Override
        public void onSelect(int position) {
            if (position != -1 && position != selectedCategory) {
                selectedCategory = position;
                listServices(mCategoryList.get(selectedCategory).id, mPetList.get(selectedPet).id);
            }
        }
    };

    PetCalendarListAdapter.OnSelectPetListener selectPetListener = new PetCalendarListAdapter.OnSelectPetListener() {
        @Override
        public void onSelect(int position) {
            if (position != -1) {
                selectedPet = position;
                mCategoryAdapter.setPetId(mPetList.get(selectedPet).id);
                mCategoryAdapter.notifyDataSetChanged();
                mRvCategory.setVisibility(View.VISIBLE);
            }
        }
    };

    ServiceListAdapter.OnServiceListener mServiceListener = new ServiceListAdapter.OnServiceListener() {
        @Override
        public void onSelect(int position) {
            showDetail(position);
        }
    };


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
        mPetService = new PetService();
        mAppointmentService = new AppointmentService();
        mAppointmentManager = AppointmentManager.getInstance();
        this.mContext = getContext();
        this.businessId = getArguments().getString("businessId", "0");
        this.userId = SessionManager.getInstance().getUserLogged().id;
        this.totalAppointments = mAppointmentManager.getTotalAppointments();

        if (businessId.equals("0") && mAppointmentManager.getCurrentBusinessId() != null) {
            this.businessId = mAppointmentManager.getCurrentBusinessId();
            mAppointmentManager.removeKey("businessId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_services, container, false);

        mPetList = new ArrayList<>();
        mServiceList = new ArrayList<>();
        mCategoryList = new ArrayList<>();
        getCategories();

        mBtnFinishScheduling = (Button) view.findViewById(R.id.btnFinishSchedule);

        if (totalAppointments > 0) {
            mBtnFinishScheduling.setEnabled(true);
        }

        /**
         * Create Pet RecyclerView
         */
        mRvPets = (RecyclerView) view.findViewById(R.id.pet_list);
        mPetLayoutManager = new LinearLayoutManager(mContext);
        mPetAdapter = new PetCalendarListAdapter(mContext, mPetList, selectPetListener);

        mPetLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPets.setLayoutManager(mPetLayoutManager);
        mRvPets.setHasFixedSize(true);
        mRvPets.setAdapter(mPetAdapter);

        /**
         * Create Categories Recyclerview
         */
        mRvServices = (RecyclerView) view.findViewById(R.id.service_list);
        mServiceAdapter = new ServiceListAdapter(mContext, mServiceList, mServiceListener);
        mServicesLayoutManager = new LinearLayoutManager(mContext);
        mServicesLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRvCategory = (RecyclerView) view.findViewById(R.id.category_list);
        mCategoryAdapter = new CategoryListAdapter(mContext, mCategoryList);
        mCategoryAdapter.setOnSelectCategoryListener(categoryListener);
        mCategoryLayout = new LinearLayoutManager(mContext);
        mCategoryLayout.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRvServices.setHasFixedSize(true);
        mRvServices.setLayoutManager(mServicesLayoutManager);

        mRvCategory.setHasFixedSize(true);
        mRvCategory.setLayoutManager(mCategoryLayout);

        if (mServiceAdapter != null) {
            mRvServices.setAdapter(mServiceAdapter);
        }

        if (mCategoryAdapter != null) {
            mRvCategory.setAdapter(mCategoryAdapter);
        }

        mRvCategory.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);

        getPets();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPets();
        mPetAdapter.resetSelectedPosition();
        mCategoryAdapter.resetCategorySelected();
        selectedCategory = -1;
        selectedPet = -1;
        mRvCategory.setVisibility(View.GONE);
        mRvServices.setVisibility(View.GONE);

        totalAppointments = mAppointmentManager.getTotalAppointments();
        if (totalAppointments > 0) {
            mBtnFinishScheduling.setEnabled(true);
        }
    }

    /**
     * Get Categories
     */
    public void getCategories() {
        mBusinessService.listBusinessCategories(businessId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                CategoryResp resp = (CategoryResp) response;
                for (CategoryResp.Item item : resp.data) {
                    mCategoryList.add(APIUtils.parseCategory(getContext(), item));
                }

                mCategoryAdapter.updateList(mCategoryList);
                mServiceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * Get User Pets
     */
    public void getPets() {
        AppUtils.showLoadingDialog(mContext);
        mPetService.listPets(userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mPetList = (ArrayList<Pet>) response;

                mPetAdapter.updateList(mPetList);
                mPetAdapter.notifyDataSetChanged();

                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * List Services
     */
    public void listServices(String categoryId, String petId) {
        AppUtils.showLoadingDialog(mContext);
        mAppointmentService.listServices(categoryId, petId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mServiceList = (ArrayList<BusinessServices>) response;

                mServiceAdapter.updateList(mServiceList);
                mServiceAdapter.notifyDataSetChanged();
                mRvServices.setVisibility(View.VISIBLE);

                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * Show Service Detail
     */
    public void showDetail(int position) {
        Intent detailItent = new Intent(mContext, ServiceDetailActivity.class);
        mAppointmentManager.setCurrentBusinessId(this.businessId);
        detailItent.putExtra("selected_pet", new Gson().toJson(mPetList.get(selectedPet)));
        detailItent.putExtra("selected_service", new Gson().toJson(mServiceList.get(position)));
        detailItent.putExtra("category_id", mCategoryList.get(selectedCategory).id);
        mContext.startActivity(detailItent);
    }

}
