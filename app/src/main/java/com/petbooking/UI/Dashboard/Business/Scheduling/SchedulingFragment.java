package com.petbooking.UI.Dashboard.Business.Scheduling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.petbooking.API.Appointment.AppointmentService;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.API.Generic.APIError;
import com.petbooking.API.Pet.PetService;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Events.ShowSnackbarEvt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.AppointmentDate;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.CartItem;
import com.petbooking.Models.Category;
import com.petbooking.Models.Pet;
import com.petbooking.Models.Professional;
import com.petbooking.Models.Scheduling;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.BusinessActivity;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter.CategoryAdapter;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter.DateSchedulingAdapter;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter.PetAdapter;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter.ProfessionalAdapter;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter.ServiceAdapter;
import com.petbooking.UI.Dashboard.Business.Scheduling.model.AppointmentDateChild;
import com.petbooking.UI.Dashboard.Business.Scheduling.model.AppointmentDateSlot;
import com.petbooking.UI.Dashboard.Business.Scheduling.model.ClearFieldsSchedule;
import com.petbooking.UI.Dashboard.Business.Scheduling.model.CreatePetPojo;
import com.petbooking.UI.Dashboard.Business.Scheduling.widget.CustomLinearLayoutManager;
import com.petbooking.UI.Dashboard.Cart.CartActivity;
import com.petbooking.UI.Dashboard.DashboardActivity;
import com.petbooking.UI.Dialogs.ConfirmDialogSchedule;
import com.petbooking.UI.Menu.Pets.RegisterPet.RegisterPetActivity;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by victorneves on 30/11/17.
 */

public class SchedulingFragment extends Fragment implements ConfirmDialogSchedule.FinishDialogListener {
    private static final String TAGSERVICES = "services";
    private static final String TAGPROFESSIONALS = "professionals";
    private static final String TAGPET = "pet";
    private static final String TAGCATEGORY = "category";
    private static final String TAGDATES = "dates";

    //View
    Button btnAddCart;
    RecyclerView mRecyclerView;
    LinearLayout placeHolderPet;


    //Adapters
    private SectionedRecyclerViewAdapter mAdapter;
    PetAdapter petAdapter;
    CategoryAdapter categoryAdapter;
    ServiceAdapter serviceAdapter;
    ProfessionalAdapter professionalAdapter;
    DateSchedulingAdapter dateAdapter;

    //Lists
    private ArrayList<Pet> mPetList;
    private ArrayList<BusinessServices> mServiceList;
    private ArrayList<Category> mCategoryList;
    private ArrayList<BusinessServices> mServiceListCopy;
    private ArrayList<Professional> mProfessionalList;
    private ArrayList<BusinessServices> additionals;
    private List<AppointmentDateSlot> days;

    //Models
    private AppointmentService mAppointmentService;
    private AppointmentManager mAppointmentManager;
    private com.petbooking.API.Business.BusinessService mBusinessService;
    AppointmentDateChild appointmentDateChild;
    AppointmentDateSlot appointmentDateSlot;
    Professional professional;
    Category category;
    CartItem item;


    PetService mPetService;

    //Ids
    private String userId;
    private String businessId = null;
    String categoryId;
    String petId;
    String petSelectedId = "";
    String categorySelectred;
    private ConfirmDialogSchedule mConfirmDialogFragment;

    //flags
    boolean addToCart = false;
    boolean initial = true;
    boolean categoryConfig = false;
    boolean complete = false;
    boolean hasService = false;
    boolean finishService = false;
    boolean existPet = true;
    boolean edit = false;
    private int dateSelected;
    private int timerSelected;
    private int positionRemove;

    int professionalSelected;

    CategoryAdapter.OnSelectCategoryListener mSelectedCategory = new CategoryAdapter.OnSelectCategoryListener() {
        @Override
        public void onSelect(int position) {
            if (petId == null || petId.equals("")) {
                //EventBus.getDefault().post(new ShowSnackbarEvt(R.string.select_pet_place_holder, Snackbar.LENGTH_LONG));
                View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
                Snackbar.make(rootView, R.string.select_pet_place_holder, Snackbar.LENGTH_LONG).show();
                return;
            }
            categoryId = mCategoryList.get(position).id;
            categoryAdapter.setTitle(mCategoryList.get(position).categoryName);
            categoryAdapter.setExpanable(false);
            setCategory(mCategoryList.get(position));
            if (!hasService) {
                listServices(categoryId, petId);
            } else {
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
        public void selectedProfessional(Professional professional, AppointmentDate appointmentDate, AppointmentDateChild child, int position, boolean edit) {

            dateAdapter.setDays(professionalAdapter.getAppointmentDateSlots());
            dateAdapter.setExpanded(true);
            mAdapter.addSection(TAGDATES, dateAdapter);
            SchedulingFragment.this.professional = professional;
            SchedulingFragment.this.professionalSelected = position;
            professionalAdapter.setTitle(professional.name);
            professionalAdapter.setexpanded(false);
            dateAdapter.setExpanded(true);
            if (edit) {
                dateAdapter.setSelectedDate(dateSelected);
                dateAdapter.setSelectedHour(timerSelected);
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    DateSchedulingAdapter.OnDateSelected mDataSelected = new DateSchedulingAdapter.OnDateSelected() {
        @Override
        public void onSelectDate(AppointmentDateSlot appointmentDateSlot, int selectedPosition, int hourSelected, int datePosition) {
            if (btnAddCart.getVisibility() != View.VISIBLE) {
                btnAddCart.setText(R.string.add_cart);
                btnAddCart.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.ic_add_shopping_cart_white), null);
                btnAddCart.setVisibility(View.VISIBLE);
                addToCart = true;
            }
            SchedulingFragment.this.timerSelected = hourSelected;
            SchedulingFragment.this.dateSelected = datePosition;
            appointmentDateSlot.position = selectedPosition;
            SchedulingFragment.this.appointmentDateSlot = appointmentDateSlot;
        }
    };

    public static SchedulingFragment newInstance(String id, Category category) {
        SchedulingFragment fragment = new SchedulingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("businessId", id);
        bundle.putParcelable("category", category);
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
        days = new ArrayList<>();
        mAppointmentService = new AppointmentService();
        mBusinessService = new com.petbooking.API.Business.BusinessService();
        this.businessId = getArguments().getString("businessId", "0");
        Category category = (Category) getArguments().getParcelable("category");
        if (category != null) {
            setCategory(category);
        }
        if (businessId.equals("0") && mAppointmentManager.getCurrentBusinessId() != null) {
            this.businessId = mAppointmentManager.getCurrentBusinessId();
            mAppointmentManager.removeKey("businessId");
        }
        if (mAppointmentManager != null) {
            mAppointmentManager.reset();
        }
        mConfirmDialogFragment = ConfirmDialogSchedule.newInstance();
        EventBus.getDefault().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_scheduling, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listCategorys);
        mRecyclerView.setHasFixedSize(true);
        btnAddCart = (Button) view.findViewById(R.id.btn_add_cart);
        placeHolderPet = (LinearLayout) view.findViewById(R.id.layout_header);
        mAdapter = new SectionedRecyclerViewAdapter();
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        petAdapter = new PetAdapter("Pet", this, mPetList, getActivity());
        categoryAdapter = new CategoryAdapter(getString(R.string.category), getContext(), mCategoryList);
        categoryAdapter.setOnSelectCategoryListener(mSelectedCategory);
        categoryAdapter.setFragment(this);
        serviceAdapter = new ServiceAdapter(getContext(), mServiceList, getString(R.string.service_additional), mSelectedService, this);
        professionalAdapter = new ProfessionalAdapter(getContext(), mProfessionalList, getString(R.string.title_professionals));
        professionalAdapter.setOnProfessionalSelected(mProfessionalSelected);
        professionalAdapter.setFragment(this);
        dateAdapter = new DateSchedulingAdapter(getActivity(), days);
        dateAdapter.setOnDateSelected(mDataSelected);
        mAdapter.addSection(petAdapter);
        mAdapter.addSection(TAGCATEGORY, categoryAdapter);
        mRecyclerView.setAdapter(mAdapter);
        Log.i(getClass().getSimpleName(), "inicio pets");
        getPets();
        getCategories();
        placeHolderPet.setVisibility(View.VISIBLE);

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addToCart) {
                    serviceAdapter.setexpanded(false);
                    serviceAdapter.setTitle(mServiceListCopy.get(0).name);
                    if(edit){
                        this.additionals = serviceAdapter.getAdditionals();
                    }
                    listProfessional(true);
                } else {
                    String businessId = mAppointmentManager.getCurrentBusinessId();
                    String startDate = CommonUtils.formatDate(CommonUtils.DATEFORMATDEFAULT, appointmentDateSlot.date);
                    String startTime = appointmentDateSlot.timer.get(appointmentDateSlot.position);
                    BusinessServices businessServices = mServiceListCopy.get(0);
                    Scheduling scheduling = new Scheduling();
                    scheduling.setPetSelected(getPetById(getPet().id));
                    scheduling.setCategorySelected(categoryAdapter.getPositionSelected());
                    scheduling.setBussinesSelected(serviceAdapter.getPositionSelected());
                    scheduling.setProfessionalSelected(professionalSelected);
                    scheduling.setDateSlotselected(dateSelected);
                    scheduling.setTimerSelected(timerSelected);
                    CartItem item = new CartItem(startDate, startTime, SchedulingFragment.this.businessId, businessServices, categoryId, category.categoryName, professional, getPet());
                    //
                    item.setScheduling(scheduling);
                    item.totalPrice += businessServices.price;
                    if (SchedulingFragment.this.item == null)
                        SchedulingFragment.this.item = item;
                    else {
                        if (!edit)
                            item.id = SchedulingFragment.this.item.id;
                    }
                    if (edit) {
                        mAppointmentManager.removeItemByIndex(positionRemove);
                    }
                    mAppointmentManager.addItem(item);

                    for (int i = 0; i < additionals.size(); i++) {
                        BusinessServices additional = additionals.get(i);
                        mAppointmentManager.addNewAdditional(businessServices.id, additional, petId,item.id);
                    }
                    mConfirmDialogFragment.setDialogInfo(R.string.schedule_empty, R.string.sucess_schedule,
                            R.string.go_to_cart, R.string.other_schedule);
                    mConfirmDialogFragment.setCancelText(R.string.action_schedule);
                    mConfirmDialogFragment.setFinishDialogListener(SchedulingFragment.this);
                    mConfirmDialogFragment.animation();
                    mConfirmDialogFragment.show(getFragmentManager(), "SHOW_CONFIRM", getContext());
                    ((BusinessActivity) getActivity()).updateCartCount(mAppointmentManager.getCountCartCount());

                }
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void addServiceAdditional(BusinessServices additional) {
        this.additionals.add(additional);
    }

    public void removeAdditional(BusinessServices additional) {
        if(!edit)
            this.additionals.remove(additional);
        else{
            for (BusinessServices businessServices : this.additionals){
                if(additional.id.equals(businessServices.id)){
                    this.additionals.remove(businessServices);
                }
            }
        }

    }

    private Pet getPet() {
        for (Pet pet : mPetList) {
            if (pet.id.toLowerCase().equals(petId)) {
                return pet;
            }
        }
        return new Pet();
    }

    public void changeItens(int position) {

        if (position >= 0) {
            String petId = mPetList.get(position).id;
            this.petId = petId;
        }
        int sectionPosition = mAdapter.getSectionPosition(TAGCATEGORY);
        serviceAdapter.setPetId(petId);
        mAdapter.addSection(TAGSERVICES, serviceAdapter);
        if (categoryConfig) {
            categoryAdapter.setExpanable(false);
            categoryAdapter.setTitle(category.categoryName);
            categoryAdapter.setCategory(category);
            serviceAdapter.setexpanded(true);
            if (categoryConfig) {
                if (mCategoryList.size() > 0) {
                    if (position >= 0) {
                        listServices(getCategoryId(category), petId);
                    } else {
                        if (!existPet)
                            listServices(getCategoryId(category), "");
                    }
                } else {
                    complete = false;
                }
            }
        } else {
            categoryAdapter.setExpanable(true);
        }
        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(sectionPosition);
    }

    public int getCount() {
        return additionals != null ? additionals.size() : 0;
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
        appointmentDateSlot = null;
        professional = null;
        btnAddCart.setVisibility(View.GONE);
        btnAddCart.setText(R.string.next_service);
        btnAddCart.setCompoundDrawables(null, null, null, null);
        addToCart = false;
        dateAdapter.setExpanded(false);
        professionalAdapter.setProfessional(null);
        edit = false;
        mAdapter.notifyDataSetChanged();
        getPets();
        getCategories();

        //((BusinessActivity) getActivity()).hideCartMenu();
    }

    public void clearInfoProfessionals() {
        additionals.clear();
        professionalAdapter.setexpanded(false);
        mServiceListCopy.clear();
        mProfessionalList.clear();
        serviceAdapter.setServiceAdd(false);
        serviceAdapter.setServices(mServiceList, false);
        serviceAdapter.setSelectedPosition(-1);
        serviceAdapter.setChecked(false);
        serviceAdapter.setServices(new ArrayList<BusinessServices>(), false);
        serviceAdapter.setServices(mServiceList, false);
        professionalAdapter.setTitle(getString(R.string.title_professionals));
        professionalAdapter.setProfessionals(mProfessionalList);
        appointmentDateChild = null;
        appointmentDateSlot = null;
        professional = null;
        btnAddCart.setVisibility(View.GONE);
        btnAddCart.setText(R.string.next_service);
        btnAddCart.setCompoundDrawables(null, null, null, null);
        addToCart = false;
        dateAdapter.setExpanded(false);
        professionalAdapter.setProfessional(null);
        mAdapter.notifyDataSetChanged();
    }

    public void getPets() {
        AppUtils.showLoadingDialog(getContext());
        mPetService.listPets(userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                int size = mAppointmentManager.getCountCartCount();
                ((BusinessActivity) getActivity()).showCartMenu();
                if (size > 0) {
                    ((BusinessActivity) getActivity()).updateCartCount(size);
                }
                mPetList = (ArrayList<Pet>) response;
                serviceAdapter.setexpanded(true);
                petAdapter.addPets(mPetList);
                int petsSize = mPetList.size();
                //petsSize = 0;
                if (initial && petsSize > 0) {
                    if (petsSize == 1) {
                        initial = false;
                        petAdapter.setSelectedPosition(0);
                        petAdapter.setExpanded(false);
                        petAdapter.setTitle(mPetList.get(0).name);
                        petAdapter.setExistPet(true);
                        placeHolderPet.setVisibility(View.GONE);
                        existPet = true;
                        changeItens(0);

                    } else {
                        existPet = true;
                        changeItens(-1);
                        placeHolderPet.setVisibility(View.GONE);
                        petAdapter.setExistPet(true);

                    }
                } else {
                    mAdapter.notifyDataSetChanged();
                    if (petsSize <= 0) {
                        existPet = false;
                        placeHolderPet.setVisibility(View.VISIBLE);
                        petAdapter.setExistPet(false);
                        petAdapter.setTitle("Adicionar Pet");
                        mAdapter.notifyDataSetChanged();
                        changeItens(-1);
                    }
                }

                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                int size = mAppointmentManager.getCountCartCount();
                ((BusinessActivity) getActivity()).showCartMenu();
                if (size > 0) {
                    ((BusinessActivity) getActivity()).updateCartCount(size);
                }
                if (error != null) {
                    APIError apiError = (APIError) error;
                    if (apiError.status.equals("401")) {
                        CommonUtils.redirectLogin(getActivity());
                    }
                }
                placeHolderPet.setVisibility(View.VISIBLE);
                AppUtils.hideDialog();
                placeHolderPet.setVisibility(View.VISIBLE);
                petAdapter.setExistPet(false);
                petAdapter.setTitle("Adicionar Pet");
                mAdapter.notifyDataSetChanged();
                changeItens(-1);
            }
        });
    }


    public void getPets(final boolean edit, final Pet pet, final String categoryId, final BusinessServices services) {
        AppUtils.showLoadingDialog(getContext());
        mPetService.listPets(userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                int size = mAppointmentManager.getCountCartCount();
                ((BusinessActivity) getActivity()).showCartMenu();
                if (size > 0) {
                    ((BusinessActivity) getActivity()).updateCartCount(size);
                }
                mPetList = (ArrayList<Pet>) response;
                serviceAdapter.setexpanded(true);
                petAdapter.addPets(mPetList);
                int petsSize = mPetList.size();
                if (edit) {
                    if (petsSize > 0) {
                        for (int i = 0; i < petsSize; i++) {
                            if (mPetList.get(i).id.equals(pet.id)) {
                                initial = false;
                                petAdapter.setSelectedPosition(i);
                                petAdapter.setExpanded(false);
                                petAdapter.setTitle(mPetList.get(i).name);
                                petAdapter.setExistPet(true);
                                placeHolderPet.setVisibility(View.GONE);
                                existPet = true;
                                changeItens(i);
                                getCategories(categoryId, pet, services);
                            }
                        }
                    }
                }
                //AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
                Snackbar.make(rootView, "Falha ao recuperar agendamento.", Snackbar.LENGTH_LONG).show();
                initial = true;
                clearFields();
            }
        });
    }

    public void listServices(String categoryId, String petId) {
        AppUtils.showLoadingDialog(getContext());
        mAppointmentService.listServices(categoryId, petId, new APICallback() {
            @Override
            public void onSuccess(Object response) {

                mServiceList = (ArrayList<BusinessServices>) response;
                Log.i(getClass().getSimpleName(), "Qual o size " + mServiceList.size());
                serviceAdapter.setServices(mServiceList, false);
                mAdapter.addSection(TAGPROFESSIONALS, professionalAdapter);
                //final int sectionPosition = mAdapter.getSectionPosition(TAGCATEGORY);
                mAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //mRecyclerView.smoothScrollToPosition(sectionPosition);
                    }
                }, 200);
                serviceAdapter.setexpanded(true);

                AppUtils.hideDialog();
                //((BusinessActivity) getActivity()).showCartMenu();

            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();

            }
        });
    }


    public void listServices(String categoryId, String petId, final BusinessServices businessService) {
        AppUtils.showLoadingDialog(getContext());
        mAppointmentService.listServices(categoryId, petId, new APICallback() {
            @Override
            public void onSuccess(Object response) {

                mServiceList = (ArrayList<BusinessServices>) response;
                Log.i(getClass().getSimpleName(), "Qual o size " + mServiceList.size());
                serviceAdapter.setServices(mServiceList, false);
                serviceAdapter.setexpanded(false);
                for (int i = 0; i < mServiceList.size(); i++) {
                    if (mServiceList.get(i).id.equals(businessService.id)) {
                        serviceAdapter.setSelectedPosition(i);
                        serviceAdapter.setTitle(mServiceList.get(i).name);
                        serviceAdapter.setChecked(true);
                        serviceAdapter.setServiceAdd(true);
                        mServiceListCopy.add(mServiceList.get(i));
                        serviceAdapter.setServices(mServiceListCopy, true);
                        mAdapter.notifyDataSetChanged();
                        listProfessional(false);
                        break;
                    }
                }
                mAdapter.addSection(TAGPROFESSIONALS, professionalAdapter);
                //final int sectionPosition = mAdapter.getSectionPosition(TAGCATEGORY);
                mAdapter.notifyDataSetChanged();
                AppUtils.hideDialog();
                //((BusinessActivity) getActivity()).showCartMenu();

            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
                View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
                Snackbar.make(rootView, "Falha ao recuperar agendamento.", Snackbar.LENGTH_LONG).show();
                initial = true;
                clearFields();

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
                if (mPetList.size() >= 0 && !complete) {
                    if (mPetList.size() == 1)
                        changeItens(0);
                    else
                        changeItens(-1);
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

    public void getCategories(final String categoryId, final Pet pet, final BusinessServices service) {
        mBusinessService.listBusinessCategories(businessId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                CategoryResp resp = (CategoryResp) response;
                mCategoryList.clear();
                for (CategoryResp.Item item : resp.data) {
                    mCategoryList.add(APIUtils.parseCategory(getContext(), item));
                }
                if (mCategoryList.size() > 0) {
                    for (Category category : mCategoryList) {
                        if (category.categoryName.equals(categoryId)) {
                            setCategory(category);
                            categoryAdapter.setCategory(category);
                            categoryAdapter.setTitle(category.categoryName);
                            mAdapter.notifyDataSetChanged();
                            listServices(category.id, pet.id, service);
                            break;
                        }
                    }
                }
                categoryAdapter.setServices(mCategoryList);
                mAdapter.notifyDataSetChanged();
                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
                View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
                Snackbar.make(rootView, "Falha ao recuperar agendamento.", Snackbar.LENGTH_LONG).show();
                initial = true;
                clearFields();
            }
        });
    }

    public void listProfessional(final boolean expanded) {
        AppUtils.showLoadingDialog(getContext());
        mAppointmentService.listProfessional(this.mServiceListCopy.get(0).id, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mProfessionalList = (ArrayList<Professional>) response;
                professionalAdapter.setProfessionals(mProfessionalList);
                professionalAdapter.setexpanded(expanded);
                if (!expanded) {
                    professionalAdapter.checkProfessional();
                }
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
        if (action == AppConstants.CANCEL_ACTION) {
            initial = true;
            item = null;
            categoryConfig = false;
            categoryAdapter.setCategory(null);
            categoryAdapter.setPositionSelected(-1);
            clearInfoProfessionals();
            expandedCategory(false);

        } else if (action == AppConstants.NEWSCHEDULE_SERVICE) {
            if (mPetList.size() <= 1) {
                item = null;
                clearFields();
                Intent intent = new Intent(getContext(), RegisterPetActivity.class);
                intent.putExtra("schedule", true);
                getContext().startActivity(intent);

            } else {
                item = null;
                clearFields();
            }
        } else {
            finishService = true;
            Intent intent = new Intent(getContext(), CartActivity.class);
            getContext().startActivity(intent);
        }
    }

    public boolean hasPet() {
        return mPetList.size() > 0;
    }

    public void setCategory(Category category) {
        this.category = category;
        this.categoryId = category.id;
        this.categoryConfig = true;
    }

    public int getCategoryIdPosition(String categoryId) {
        int i = 0;
        if (mCategoryList.size() > 0) {

            for (Category category : mCategoryList) {
                if (categoryId.equals(category.categoryName)) {
                    return i;
                }
                i++;
            }
        }

        return -1;
    }

    public String getCategoryId(Category categoryId) {
        if (mCategoryList.size() > 0) {
            for (Category category : mCategoryList) {
                if (categoryId.categoryName.equals(category.categoryName)) {
                    return category.id;
                }
            }
        } else {
            complete = false;
        }
        return categoryId.id;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CreatePetPojo response) {
        getPets();
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ClearFieldsSchedule response) {
        if (finishService && !response.clear) {
            initial = true;
            clearFields();
        } else {
            try {
                addToCart = false;
                petAdapter.setExistPet(true);
                petAdapter.setPet(response.cartItem.pet);
                getPets(true, response.cartItem.pet, response.cartItem.categoryName, response.cartItem.service);
                professionalAdapter.setProfessional(response.cartItem.professional);
                professionalAdapter.setTitle(response.cartItem.professional.name);
                mServiceList.clear();
                mServiceListCopy.clear();
                additionals.clear();
                additionals = response.cartItem.additionalServices;
                serviceAdapter.setServicesAddicionais(response.cartItem.additionalServices);
                edit = true;
                positionRemove = response.position;
                dateSelected = response.cartItem.scheduling.getDateSlotselected();
                timerSelected = response.cartItem.scheduling.getTimerSelected();
                mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                initial = true;
                clearFields();
            }
        }
    }

    public Category getCategoryById(String id) {
        if (this.mCategoryList != null) {
            for (Category category : mCategoryList) {
                if (category.id.equals(id)) {
                    return category;
                }
            }
        }
        return null;
    }


    public int getPetById(String id) {
        int i = 0;
        if (mPetList != null) {
            for (Pet pet : mPetList) {
                if (pet.id.equals(id)) {
                    return i;
                }
                i++;
            }

            return 0;
        }
        return 0;
    }

    public void expandedCategory() {
        if (petId != null && !petId.equals("")) {
            petAdapter.setExpanded(false);
        }
        categoryAdapter.setExpanable(true);
        serviceAdapter.setexpanded(false);
        professionalAdapter.setexpanded(false);
        dateAdapter.setExpanded(false);
        btnAddCart.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
    }

    public void expandedService(boolean checked) {
        petAdapter.setExpanded(false);
        categoryAdapter.setExpanable(false);
        serviceAdapter.setexpanded(true);
        serviceAdapter.setChecked(checked);
        btnAddCart.setText(R.string.next_service);
        btnAddCart.setCompoundDrawables(null, null, null, null);
        btnAddCart.setVisibility(View.VISIBLE);
        addToCart = false;
        professionalAdapter.setexpanded(false);
        dateAdapter.setExpanded(false);
        mAdapter.notifyDataSetChanged();
    }

    public void expandedCategory(boolean checked) {
        serviceAdapter.setexpanded(false);
        serviceAdapter.setChecked(checked);
        btnAddCart.setText(R.string.next_service);
        btnAddCart.setCompoundDrawables(null, null, null, null);
        btnAddCart.setVisibility(View.VISIBLE);
        addToCart = false;
        professionalAdapter.setexpanded(false);
        dateAdapter.setExpanded(false);
        mAdapter.notifyDataSetChanged();
        expandedCategory();
    }

    public void expandedProfessional() {
        petAdapter.setExpanded(false);
        categoryAdapter.setExpanable(false);
        serviceAdapter.setexpanded(false);
        professionalAdapter.setexpanded(true);
        dateAdapter.setExpanded(false);
        btnAddCart.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
    }
}
