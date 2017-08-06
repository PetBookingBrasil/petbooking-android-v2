package com.petbooking.UI.Dashboard.Business.ServiceDetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.petbooking.Managers.PreferenceManager;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Pet;
import com.petbooking.R;

public class ServiceDetailActivity extends AppCompatActivity {

    private String businessName;
    private BusinessServices selectedService;
    private Pet selectedPet;

    /**
     * Content
     */
    private AppointmentBottomFragment mAppointmentFragment;

    private CheckBox mCbPet;
    private TextView mTvServiceName;
    private TextView mTvServicePrice;
    private TextView mTvServiceDescription;
    private TextView mTvAdditionalLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        Gson mGson = new Gson();
        mAppointmentFragment = new AppointmentBottomFragment();
        businessName = PreferenceManager.getInstance().getString("businessName");
        getSupportActionBar().setTitle(businessName);

        String serviceData = getIntent().getStringExtra("selected_service");
        String petData = getIntent().getStringExtra("selected_pet");

        selectedService = mGson.fromJson(serviceData, BusinessServices.class);
        selectedPet = mGson.fromJson(petData, Pet.class);

        mCbPet = (CheckBox) findViewById(R.id.pet_checkbox);
        mTvServiceName = (TextView) findViewById(R.id.service_name);
        mTvServicePrice = (TextView) findViewById(R.id.service_price);
        mTvServiceDescription = (TextView) findViewById(R.id.service_description);
        mTvAdditionalLabel = (TextView) findViewById(R.id.additional_label);

        String price = getResources().getString(R.string.business_service_price, String.format("%.2f", selectedService.price));

        mTvServiceName.setText(selectedService.name);
        mTvServicePrice.setText(price);
        mTvServiceDescription.setText(selectedService.description);

        if (selectedService.additionalServices.size() != 0) {
            mTvAdditionalLabel.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(selectedService.description)) {
            mTvServiceDescription.setVisibility(View.GONE);
        }

        mCbPet.setText(selectedPet.name);
        mAppointmentFragment.show(getSupportFragmentManager(), mAppointmentFragment.getTag());
    }
}
