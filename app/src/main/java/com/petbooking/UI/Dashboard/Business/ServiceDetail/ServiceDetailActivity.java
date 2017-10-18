package com.petbooking.UI.Dashboard.Business.ServiceDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.petbooking.API.Appointment.AppointmentService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Pet;
import com.petbooking.Models.Professional;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.BusinessServices.AdditionalServiceListAdapter;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

public class ServiceDetailActivity extends AppCompatActivity {

    private AppointmentService mAppointmentService;
    private String businessName;
    private String categoryId;
    private BusinessServices selectedService;
    private Pet selectedPet;
    private ArrayList<BusinessServices> mAdditionalList;

    /**
     * Content
     */
    private AppointmentBottomFragment mAppointmentFragment;

    private CheckBox mCbPet;
    private CheckBox mCbService;
    private TextView mTvServiceName;
    private TextView mTvServicePrice;
    private TextView mTvServiceDescription;
    private TextView mTvAdditionalLabel;

    private RecyclerView mRvAdditionalServices;
    private AdditionalServiceListAdapter mAdditionalAdapter;

    /**
     * Professional Components
     */
    private int selectedProfessional = -1;
    private LinearLayoutManager mProfessionalLayout;
    private RelativeLayout mProfessionalContainer;
    private ArrayList<Professional> mProfessionalList;
    private ProfessionalListAdapter mProfessionalAdapter;
    private RecyclerView mRvProfessional;

    CompoundButton.OnCheckedChangeListener onCheckedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(!isChecked){
                onBackPressed();
            }
        }
    };

    AdditionalServiceListAdapter.OnAdditionalSelect onAdditionalSelect = new AdditionalServiceListAdapter.OnAdditionalSelect() {
        @Override
        public void onSelect(boolean selected, String additionalId) {
            if (selected) {
                addAdditional(additionalId);
            } else {
                removeAdditional(additionalId);
            }
        }
    };

    ProfessionalListAdapter.OnSelectProfessionaListener professionaListener = new ProfessionalListAdapter.OnSelectProfessionaListener() {
        @Override
        public void onSelect(int position) {
            if (position != -1 && position != selectedProfessional) {
                selectedProfessional = position;
                mProfessionalContainer.setVisibility(View.GONE);
                mAppointmentFragment.setSelectedProfessional(position);
                mAppointmentFragment.setAdditionalList(mAdditionalList);
                mAppointmentFragment.show(getSupportFragmentManager(), mAppointmentFragment.getTag());
            }
        }
    };


    AppointmentBottomFragment.OnAppointmentListener onAppointmentListener = new AppointmentBottomFragment.OnAppointmentListener() {
        @Override
        public void onAction(int code) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        Gson mGson = new Gson();
        mAppointmentFragment = new AppointmentBottomFragment();
        mAppointmentService = new AppointmentService();
        mProfessionalList = new ArrayList<>();
        mAdditionalList = new ArrayList<>();
        businessName = AppointmentManager.getInstance().getCurrentBusinessName();
        getSupportActionBar().setTitle(businessName);

        String serviceData = getIntent().getStringExtra("selected_service");
        String petData = getIntent().getStringExtra("selected_pet");
        categoryId = getIntent().getStringExtra("category_id");

        mProfessionalLayout = new LinearLayoutManager(this);
        mProfessionalLayout.setOrientation(LinearLayout.HORIZONTAL);

        selectedService = mGson.fromJson(serviceData, BusinessServices.class);
        selectedPet = mGson.fromJson(petData, Pet.class);

        /**
         * Professional Recyclerview
         */
        mProfessionalAdapter = new ProfessionalListAdapter(this, mProfessionalList, professionaListener);
        mProfessionalContainer = (RelativeLayout) findViewById(R.id.professional_container);
        mRvProfessional = (RecyclerView) findViewById(R.id.professional_list);
        mRvProfessional.setHasFixedSize(true);
        mRvProfessional.setLayoutManager(mProfessionalLayout);
        listProfessional();

        mRvAdditionalServices = (RecyclerView) findViewById(R.id.additional_services);

        mCbPet = (CheckBox) findViewById(R.id.pet_checkbox);
        mCbService = (CheckBox) findViewById(R.id.service_checkbox);
        mTvServiceName = (TextView) findViewById(R.id.service_name);
        mTvServicePrice = (TextView) findViewById(R.id.service_price);
        mTvServiceDescription = (TextView) findViewById(R.id.service_description);
        mTvAdditionalLabel = (TextView) findViewById(R.id.additional_label);

        String price = getResources().getString(R.string.business_service_price, String.format("%.2f", selectedService.price));

        mTvServiceName.setText(selectedService.name);
        mTvServicePrice.setText(price);
        mTvServiceDescription.setText(selectedService.description);

        if (selectedService.additionalServices.size() != 0) {
            LinearLayoutManager mAdditionalLayout = new LinearLayoutManager(this);
            mAdditionalLayout.setOrientation(LinearLayoutManager.VERTICAL);

            mAdditionalAdapter = new AdditionalServiceListAdapter(this, selectedService.additionalServices, onAdditionalSelect);

            mRvAdditionalServices.setHasFixedSize(true);
            mRvAdditionalServices.setLayoutManager(mAdditionalLayout);
            mRvAdditionalServices.setAdapter(mAdditionalAdapter);

            mTvAdditionalLabel.setVisibility(View.VISIBLE);
            mRvAdditionalServices.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(selectedService.description)) {
            mTvServiceDescription.setVisibility(View.GONE);
        }

        mCbPet.setText(selectedPet.name);
        mRvProfessional.setAdapter(mProfessionalAdapter);
        mAppointmentFragment.setService(selectedService);
        mAppointmentFragment.setPet(selectedPet);
        mAppointmentFragment.setCategoryId(categoryId);
        mAppointmentFragment.setOnAppointmentListener(onAppointmentListener);

        mCbPet.setOnCheckedChangeListener(onCheckedListener);
        mCbService.setOnCheckedChangeListener(onCheckedListener);
    }

    /**
     * List Professional
     */
    public void listProfessional() {
        AppUtils.showLoadingDialog(this);
        mAppointmentService.listProfessional(this.selectedService.id, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mProfessionalList = (ArrayList<Professional>) response;

                mProfessionalAdapter.updateList(mProfessionalList);
                mProfessionalAdapter.notifyDataSetChanged();

                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    public void addAdditional(String additionalId) {
        int index = 0;

        for (BusinessServices additional : selectedService.additionalServices) {
            if (additional.id.equals(additionalId)) {
                mAdditionalList.add(selectedService.additionalServices.get(index));
                break;
            }

            index++;
        }
    }

    public void removeAdditional(String additionalId) {
        int index = 0;
        for (BusinessServices additional : mAdditionalList) {
            if (additional.id.equals(additionalId)) {
                mAdditionalList.remove(index);
                break;
            }

            index++;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
