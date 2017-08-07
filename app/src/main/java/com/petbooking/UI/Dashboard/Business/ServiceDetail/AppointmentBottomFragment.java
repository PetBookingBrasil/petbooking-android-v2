package com.petbooking.UI.Dashboard.Business.ServiceDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.petbooking.API.Appointment.AppointmentService;
import com.petbooking.API.Generic.AvatarResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Professional;
import com.petbooking.R;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

public class AppointmentBottomFragment extends BottomSheetDialogFragment {

    private String serviceId;
    private AppointmentService mAppointmentService;
    private LinearLayout mFragmentLayout;

    private ArrayList<Professional> mProfessionalList;
    private ProfessionalListAdapter mProfessionalAdapter;
    private RecyclerView mRvProfessional;

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

        mProfessionalAdapter = new ProfessionalListAdapter(getContext(), mProfessionalList);

        mFragmentLayout = (LinearLayout) view.findViewById(R.id.fragment_layout);
        mRvProfessional = (RecyclerView) view.findViewById(R.id.professional_list);
        mRvProfessional.setHasFixedSize(true);
        mRvProfessional.setLayoutManager(mProfessionalLayout);

        mRvProfessional.setAdapter(mProfessionalAdapter);

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

}
