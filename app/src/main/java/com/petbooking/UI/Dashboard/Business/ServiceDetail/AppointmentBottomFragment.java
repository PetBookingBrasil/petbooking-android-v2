package com.petbooking.UI.Dashboard.Business.ServiceDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.petbooking.API.Appointment.AppointmentService;
import com.petbooking.API.Appointment.Models.ProfessionalResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.AppointmentDate;
import com.petbooking.Models.Professional;
import com.petbooking.R;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

public class AppointmentBottomFragment extends BottomSheetDialogFragment {

    private String serviceId;
    private AppointmentService mAppointmentService;
    private LinearLayout mFragmentLayout;

    /**
     * Professional Components
     */
    private int selectedProfessional = -1;
    private ArrayList<Professional> mProfessionalList;
    private ProfessionalListAdapter mProfessionalAdapter;
    private RecyclerView mRvProfessional;

    /**
     * Dates Components
     */
    private int selectedMonth = -1;
    private RelativeLayout mDateListLayout;
    private DateListAdapter mDateListAdapter;
    private RecyclerView mRvAppointmentDate;
    private SnapHelper mDateSnapHelper;

    /**
     * Dates Components
     */
    private DaysListAdapter mDaysListAdapter;
    private RecyclerView mRvDays;

    ProfessionalListAdapter.OnSelectProfessionaListener professionaListener = new ProfessionalListAdapter.OnSelectProfessionaListener() {
        @Override
        public void onSelect(int position) {
            if (position != -1 && position != selectedProfessional) {
                selectedProfessional = position;
                handleSelectProfessional();
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_dialog, container, false);

        mAppointmentService = new AppointmentService();
        mProfessionalList = new ArrayList<>();
        listProfessional();

        LinearLayoutManager mProfessionalLayout = new LinearLayoutManager(getContext());
        mProfessionalLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayoutManager mDateLayout = new LinearLayoutManager(getContext());
        mDateLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayoutManager mDaysLayout = new LinearLayoutManager(getContext());
        mDaysLayout.setOrientation(LinearLayout.HORIZONTAL);

        mProfessionalAdapter = new ProfessionalListAdapter(getContext(), mProfessionalList, professionaListener);
        mDateListAdapter = new DateListAdapter(getContext(), new ArrayList<AppointmentDate>());
        mDaysListAdapter = new DaysListAdapter(getContext(), new ArrayList<ProfessionalResp.Slot>());

        /**
         * Professional Recyclerview
         */
        mFragmentLayout = (LinearLayout) view.findViewById(R.id.fragment_layout);
        mRvProfessional = (RecyclerView) view.findViewById(R.id.professional_list);
        mRvProfessional.setHasFixedSize(true);
        mRvProfessional.setLayoutManager(mProfessionalLayout);

        /**
         * Recycler Date
         */
        mDateSnapHelper = new LinearSnapHelper();
        mDateListLayout = (RelativeLayout) view.findViewById(R.id.date_layout);
        mRvAppointmentDate = (RecyclerView) view.findViewById(R.id.date_list);
        mRvAppointmentDate.setHasFixedSize(true);
        mRvAppointmentDate.setLayoutManager(mDateLayout);
        mDateSnapHelper.attachToRecyclerView(mRvAppointmentDate);

 /*       *//**
         * RecyclerView Days
         *//*
        mRvDays = (RecyclerView) view.findViewById(R.id.day_list);
        mRvDays.setHasFixedSize(true);
        mRvDays.setLayoutManager(mDaysLayout);*/

        mRvProfessional.setAdapter(mProfessionalAdapter);
        mRvAppointmentDate.setAdapter(mDateListAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * List Professional
     */
    public void listProfessional() {
        AppUtils.showLoadingDialog(getContext());
        mAppointmentService.listProfessional(this.serviceId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mProfessionalList = (ArrayList<Professional>) response;

                mProfessionalAdapter.updateList(mProfessionalList);
                mProfessionalAdapter.notifyDataSetChanged();
                mFragmentLayout.setVisibility(View.VISIBLE);

                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * Show days on select professional
     */
    private void handleSelectProfessional() {
        Professional professional = mProfessionalList.get(selectedProfessional);

        mDateListAdapter.updateList(professional.availableDates);
        mDateListAdapter.notifyDataSetChanged();

        mDateListLayout.setVisibility(View.VISIBLE);
    }
}
