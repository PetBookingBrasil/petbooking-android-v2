package com.petbooking.UI.Dashboard.Business.Scheduling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.petbooking.API.Appointment.AppointmentService;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.API.Pet.PetService;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.AppointmentDate;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.CartItem;
import com.petbooking.Models.Category;
import com.petbooking.Models.Pet;
import com.petbooking.Models.Professional;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.BusinessActivity;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter.CategoryAdapter;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter.PetAdapter;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter.ProfessionalAdapter;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter.ServiceAdapter;
import com.petbooking.UI.Dashboard.Business.Scheduling.model.AppointmentDateChild;
import com.petbooking.UI.Dashboard.Business.Scheduling.widget.CustomLinearLayoutManager;
import com.petbooking.UI.Dashboard.Cart.CartActivity;
import com.petbooking.UI.Dialogs.ConfirmDialogSchedule;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by victorneves on 30/11/17.
 */

public class SchedulingFragment extends Fragment implements ConfirmDialogSchedule.FinishDialogListener{
    private static final String TAGSERVICES = "services";
    private static final String TAGPROFESSIONALS = "professionals";
    private static final String TAGPET = "pet";
    private static final String TAGCATEGORY = "category";

    //View
    Button btnAddCart;
    RecyclerView mRecyclerView;


    //Adapters
    private SectionedRecyclerViewAdapter mAdapter;
    PetAdapter petAdapter;
    CategoryAdapter categoryAdapter;
    ServiceAdapter serviceAdapter;
    ProfessionalAdapter professionalAdapter;

    //Lists
    private ArrayList<Pet> mPetList;
    private ArrayList<BusinessServices> mServiceList;
    private ArrayList<Category> mCategoryList;
    private ArrayList<BusinessServices> mServiceListCopy;
    private ArrayList<Professional> mProfessionalList;
    private ArrayList<BusinessServices> additionals;

    //Models
    private AppointmentService mAppointmentService;
    private AppointmentManager mAppointmentManager;
    private com.petbooking.API.Business.BusinessService mBusinessService;
    AppointmentDateChild appointmentDateChild;
    Professional professional;


    PetService mPetService;

    //Ids
    private String userId;
    private String businessId = null;
    String categoryId;
    String petId;
    private ConfirmDialogSchedule mConfirmDialogFragment;

    boolean addToCart = false;
    boolean initial = true;
    boolean hasService = false;


    CategoryAdapter.OnSelectCategoryListener mSelectedCategory = new CategoryAdapter.OnSelectCategoryListener() {
        @Override
        public void onSelect(int position) {
            categoryId = mCategoryList.get(position).id;
            categoryAdapter.setTitle(mCategoryList.get(position).categoryName);
            categoryAdapter.setExpanable(false);
            if(!hasService) {
                listServices(categoryId, petId);
            }else{
                btnAddCart.setVisibility(View.GONE);
                btnAddCart.setText(R.string.next_service);
                btnAddCart.setCompoundDrawables(null, null, null, null);
                mServiceList.clear();
                additionals.clear();
                mServiceListCopy.clear();
                serviceAdapter.setChecked(false);
                serviceAdapter.setServiceAdd(false);
                serviceAdapter.setTitle(getString(R.string.service_additional));
                serviceAdapter.setServices(mServiceList, false);
                mAdapter.notifyDataSetChanged();
                listServices(categoryId, petId);
            }

        }
    };

    ServiceAdapter.OnSelectecService mSelectedService = new ServiceAdapter.OnSelectecService() {
        @Override
        public void onSelect(String petId, BusinessServices service, boolean add) {
            if (add) {
                hasService = true;
                mServiceListCopy.add(service);
                serviceAdapter.setServices(mServiceListCopy, true);
                mAdapter.notifyDataSetChanged();
                btnAddCart.setVisibility(View.VISIBLE);
            } else {
                hasService = false;
                mServiceListCopy.remove(0);
                serviceAdapter.setServices(mServiceList, false);
                btnAddCart.setVisibility(View.INVISIBLE);
                mAdapter.notifyDataSetChanged();
                additionals.clear();
            }
        }
    };

    ProfessionalAdapter.OnProfessionalSelected mProfessionalSelected = new ProfessionalAdapter.OnProfessionalSelected() {
        @Override
        public void selectedProfessional(Professional professional, AppointmentDate appointmentDate, AppointmentDateChild child) {
            if (btnAddCart.getVisibility() != View.VISIBLE) {
                btnAddCart.setText(R.string.add_cart);
                btnAddCart.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.ic_add_shopping_cart_white), null);
                btnAddCart.setVisibility(View.VISIBLE);
                addToCart = true;
            }
            SchedulingFragment.this.professional = professional;
            SchedulingFragment.this.appointmentDateChild = child;
        }
    };

    public static SchedulingFragment newInstance(String id) {
        SchedulingFragment fragment = new SchedulingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("businessId", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppointmentManager = AppointmentManager.getInstance();
        this.userId = SessionManager.getInstance().getUserLogged().id;
        mPetList = new ArrayList<>();
        mPetService = new PetService();
        mServiceList = new ArrayList<>();
        mCategoryList = new ArrayList<>();
        mServiceListCopy = new ArrayList<>();
        mProfessionalList = new ArrayList<>();
        additionals = new ArrayList<>();
        mAppointmentService = new AppointmentService();
        mBusinessService = new com.petbooking.API.Business.BusinessService();
        this.businessId = getArguments().getString("businessId", "0");

        if (businessId.equals("0") && mAppointmentManager.getCurrentBusinessId() != null) {
            this.businessId = mAppointmentManager.getCurrentBusinessId();
            mAppointmentManager.removeKey("businessId");
        }
        mConfirmDialogFragment = ConfirmDialogSchedule.newInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_scheduling, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listCategorys);
        mRecyclerView.setHasFixedSize(true);
        btnAddCart = (Button) view.findViewById(R.id.btn_add_cart);
        mAdapter = new SectionedRecyclerViewAdapter();
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        petAdapter = new PetAdapter("Pet", this, mPetList, getActivity());
        categoryAdapter = new CategoryAdapter(getString(R.string.category),  getContext(), mCategoryList);
        categoryAdapter.setOnSelectCategoryListener(mSelectedCategory);
        categoryAdapter.setFragment(this);
        serviceAdapter = new ServiceAdapter(getContext(), mServiceList, getString(R.string.service_additional), mSelectedService,this);
        professionalAdapter = new ProfessionalAdapter(getContext(), mProfessionalList, getString(R.string.title_professionals));
        professionalAdapter.setOnProfessionalSelected(mProfessionalSelected);
        professionalAdapter.setFragment(this);
        mAdapter.addSection(petAdapter);
        mAdapter.addSection(TAGCATEGORY,categoryAdapter);
        mRecyclerView.setAdapter(mAdapter);
        getPets();
        getCategories();

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addToCart) {
                    serviceAdapter.setexpanded(false);
                    serviceAdapter.setTitle(mServiceListCopy.get(0).name);
                    listProfessional();

                } else {

                    String businessId = mAppointmentManager.getCurrentBusinessId();
                    String startDate = CommonUtils.formatDate(CommonUtils.DATEFORMATDEFAULT, appointmentDateChild.date);
                    String startTime = appointmentDateChild.Time;
                    BusinessServices businessServices = mServiceListCopy.get(0);
                    CartItem item = new CartItem(startDate, startTime, businessId, businessServices, categoryId, professional, getPet());
                    item.totalPrice += businessServices.price;
                    mAppointmentManager.addItem(item);

                    for(int i = 0; i< additionals.size(); i++){
                        BusinessServices additional = additionals.get(i);
                        mAppointmentManager.addNewAdditional(businessServices.id,additional,petId);
                    }
                    mConfirmDialogFragment.setDialogInfo(R.string.schedule_empty, R.string.sucess_schedule,
                            R.string.go_to_cart);
                    mConfirmDialogFragment.setCancelText(R.string.action_schedule);
                    mConfirmDialogFragment.setFinishDialogListener(SchedulingFragment.this);
                    mConfirmDialogFragment.animation();
                    mConfirmDialogFragment.show(getFragmentManager(), "SHOW_CONFIRM",getContext());

                    ((BusinessActivity) getActivity()).updateCartCount(mAppointmentManager.getCountCartPetId(petId));

                }
            }
        });

        return view;
    }

    public void addServiceAdditional(BusinessServices additional){
        this.additionals.add(additional);
    }

    public void removeAdditional(BusinessServices additional){
        this.additionals.remove(additional);
    }

    private Pet getPet() {
        for (Pet pet : mPetList) {
            if (pet.id.toLowerCase().equals(petId)) {
                return pet;
            }
        }
        return new Pet();
    }

    public void notifyChanged(int position) {
        String petId = mPetList.get(position).id;
        this.petId = petId;
        categoryAdapter.setExpanable(true);
        int sectionPosition = mAdapter.getSectionPosition(TAGCATEGORY);
        serviceAdapter.setPetId(petId);
        mAdapter.addSection(TAGSERVICES, serviceAdapter);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(sectionPosition);
    }

    public int getCount(){
        return additionals!=null? additionals.size() : 0;
    }

    public void clearFields() {
        petAdapter.setExpanded(true);
        petAdapter.setInitial(false);
        categoryAdapter.setExpanable(false);
        serviceAdapter.setexpanded(false);
        professionalAdapter.setexpanded(false);
        mServiceListCopy.clear();
        mProfessionalList.clear();
        mPetList.clear();
        mServiceList.clear();
        mCategoryList.clear();
        additionals.clear();
        serviceAdapter.setChecked(false);
        categoryAdapter.setPositionSelected(-1);
        serviceAdapter.setServiceAdd(false);
        professionalAdapter.setTitle(getString(R.string.title_professionals));
        categoryAdapter.setTitle(getString(R.string.category));
        serviceAdapter.setTitle(getString(R.string.service_additional));
        petAdapter.setTitle("Pet");
        petAdapter.setSelectedPosition(-1);
        petAdapter.addPets(mPetList);
        categoryAdapter.setServices(mCategoryList);
        serviceAdapter.setServices(mServiceList, false);
        professionalAdapter.setProfessionals(mProfessionalList);
        categoryId = "";
        petId = "";
        appointmentDateChild = null;
        professional = null;
        btnAddCart.setVisibility(View.GONE);
        btnAddCart.setText(R.string.next_service);
        btnAddCart.setCompoundDrawables(null, null, null, null);
        addToCart = false;
        mAdapter.notifyDataSetChanged();
        getPets();
        getCategories();
        ((BusinessActivity) getActivity()).hideCartMenu();

    }

    public void getPets() {
        AppUtils.showLoadingDialog(getContext());
        mPetService.listPets(userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mPetList = (ArrayList<Pet>) response;
                serviceAdapter.setexpanded(true);
                petAdapter.addPets(mPetList);
                if(initial && mPetList.size() >0) {
                    initial = false;
                    petAdapter.setSelectedPosition(0);
                    petAdapter.setTitle(mPetList.get(0).name);
                    petAdapter.setExpanded(false);
                    notifyChanged(0);

                }else
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
                serviceAdapter.setServices(mServiceList, false);
                mAdapter.addSection(TAGPROFESSIONALS, professionalAdapter);
                final int sectionPosition = mAdapter.getSectionPosition(TAGCATEGORY);
                mAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.smoothScrollToPosition(sectionPosition);
                    }
                },200);
                serviceAdapter.setexpanded(true);

                AppUtils.hideDialog();
                ((BusinessActivity) getActivity()).showCartMenu();

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

                categoryAdapter.setServices(mCategoryList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    public void listProfessional() {
        AppUtils.showLoadingDialog(getContext());
        mAppointmentService.listProfessional(this.mServiceListCopy.get(0).id, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mProfessionalList = (ArrayList<Professional>) response;
                professionalAdapter.setProfessionals(mProfessionalList);
                professionalAdapter.setexpanded(true);
                int sectionPosition = mAdapter.getSectionPosition(TAGCATEGORY);
                btnAddCart.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(sectionPosition);

                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    @Override
    public void onFinishDialog(int action) {
        if(action == AppConstants.CANCEL_ACTION){
            initial = true;
            clearFields();
        }else{
            Intent intent = new Intent(getContext(), CartActivity.class);
            getContext().startActivity(intent);
        }
    }

    public void expandedCategory(){
        petAdapter.setExpanded(false);
        categoryAdapter.setExpanable(true);
        serviceAdapter.setexpanded(false);
        professionalAdapter.setexpanded(false);
        mAdapter.notifyDataSetChanged();
    }

    public void expandedService(){
        petAdapter.setExpanded(false);
        categoryAdapter.setExpanable(false);
        serviceAdapter.setexpanded(true);
        serviceAdapter.setChecked(true);
        btnAddCart.setText(R.string.next_service);
        btnAddCart.setCompoundDrawables(null, null, null, null);
        addToCart = false;
        professionalAdapter.setexpanded(false);
        mAdapter.notifyDataSetChanged();
    }

    public void expandedProfessional(){
        petAdapter.setExpanded(false);
        categoryAdapter.setExpanable(false);
        serviceAdapter.setexpanded(false);
        professionalAdapter.setexpanded(true);
        mAdapter.notifyDataSetChanged();
    }
}
