package com.petbooking.UI.Dashboard.Business.ServiceDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
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
     * Buttons
     */
    private ImageView mIvPreviousDate;
    private ImageView mIvNextDate;
    private Button mBtnConfirmAppointment;

    /**
     * Professional Components
     */
    private int selectedProfessional = -1;
    private LinearLayoutManager mProfessionalLayout;
    private ArrayList<Professional> mProfessionalList;
    private ProfessionalListAdapter mProfessionalAdapter;
    private RecyclerView mRvProfessional;

    /**
     * Dates Components
     */
    private int selectedMonth = 0;
    private ArrayList<AppointmentDate> mDateList;
    private LinearLayoutManager mDateLayout;
    private RelativeLayout mDateListLayout;
    private DateListAdapter mDateListAdapter;
    private RecyclerView mRvAppointmentDate;
    private SnapHelper mDateSnapHelper;

    /**
     * Days Components
     */
    private int selectedDay = -1;
    private LinearLayoutManager mDaysLayout;
    private DaysListAdapter mDaysListAdapter;
    private RecyclerView mRvDays;

    /**
     * Time Components
     */
    private int selectedTime = -1;
    private LinearLayoutManager mTimeLayout;
    private RecyclerView mRvTime;
    private TimeListAdapter mTimeAdapter;

    ProfessionalListAdapter.OnSelectProfessionaListener professionaListener = new ProfessionalListAdapter.OnSelectProfessionaListener() {
        @Override
        public void onSelect(int position) {
            if (position != -1 && position != selectedProfessional) {
                selectedProfessional = position;
                handleSelectProfessional();
            }
        }
    };

    View.OnClickListener btnDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemId = v.getId();
            if (itemId == mIvPreviousDate.getId()) {
                previousMonth();
            } else {
                nextMonth();
            }

            if (selectedMonth != -1) {
                mDaysListAdapter.updateList(mDateList.get(selectedMonth).days);
                mDaysListAdapter.notifyDataSetChanged();
                mDaysListAdapter.resetDay();
            }
        }
    };

    DaysListAdapter.OnSelectDayListener dayListener = new DaysListAdapter.OnSelectDayListener() {
        @Override
        public void onSelect(int position) {
            selectedDay = position;
            handleSelectDay(selectedDay);
        }
    };

    TimeListAdapter.OnSelectTimeListener timeListener = new TimeListAdapter.OnSelectTimeListener() {
        @Override
        public void onSelect(int position) {
            if (position != -1) {
                mBtnConfirmAppointment.setEnabled(true);
            } else {
                mBtnConfirmAppointment.setEnabled(false);
            }
        }
    };

    RecyclerView.OnScrollListener dateScroll = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mDateLayout.findFirstCompletelyVisibleItemPosition() != -1) {
                int itemPosition = mDateLayout.findFirstCompletelyVisibleItemPosition();
                selectedMonth = itemPosition;

                if (selectedMonth != -1) {
                    mDaysListAdapter.updateList(mDateList.get(selectedMonth).days);
                    mDaysListAdapter.notifyDataSetChanged();
                    mDaysListAdapter.resetDay();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_dialog, container, false);

        mAppointmentService = new AppointmentService();
        mProfessionalList = new ArrayList<>();
        mDateList = new ArrayList<>();
        listProfessional();

        mProfessionalLayout = new LinearLayoutManager(getContext());
        mProfessionalLayout.setOrientation(LinearLayout.HORIZONTAL);

        mDateLayout = new LinearLayoutManager(getContext());
        mDateLayout.setOrientation(LinearLayout.HORIZONTAL);

        mDaysLayout = new LinearLayoutManager(getContext());
        mDaysLayout.setOrientation(LinearLayout.HORIZONTAL);

        mTimeLayout = new LinearLayoutManager(getContext());
        mTimeLayout.setOrientation(LinearLayout.HORIZONTAL);

        mProfessionalAdapter = new ProfessionalListAdapter(getContext(), mProfessionalList, professionaListener);
        mDateListAdapter = new DateListAdapter(getContext(), new ArrayList<AppointmentDate>());
        mDaysListAdapter = new DaysListAdapter(getContext(), new ArrayList<ProfessionalResp.Slot>(), dayListener);
        mTimeAdapter = new TimeListAdapter(getContext(), new ArrayList<String>(), timeListener);

        /**
         * Buttons
         */
        mIvPreviousDate = (ImageView) view.findViewById(R.id.previous_date);
        mIvNextDate = (ImageView) view.findViewById(R.id.next_date);
        mBtnConfirmAppointment = (Button) view.findViewById(R.id.confirm_appointment);

        mIvPreviousDate.setOnClickListener(btnDateListener);
        mIvNextDate.setOnClickListener(btnDateListener);

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
        mRvAppointmentDate.addOnScrollListener(dateScroll);
        mDateSnapHelper.attachToRecyclerView(mRvAppointmentDate);

        /**
         * RecyclerView Days
         */
        mRvDays = (RecyclerView) view.findViewById(R.id.day_list);
        mRvDays.setHasFixedSize(true);
        mRvDays.setLayoutManager(mDaysLayout);


        /**
         * Time List
         */
        mRvTime = (RecyclerView) view.findViewById(R.id.time_list);
        mRvTime.setHasFixedSize(true);
        mRvTime.setLayoutManager(mTimeLayout);


        mRvProfessional.setAdapter(mProfessionalAdapter);
        mRvAppointmentDate.setAdapter(mDateListAdapter);
        mRvDays.setAdapter(mDaysListAdapter);
        mRvTime.setAdapter(mTimeAdapter);


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
        mDateList = professional.availableDates;

        mDateListAdapter.updateList(mDateList);
        mDateListAdapter.notifyDataSetChanged();

        mDaysListAdapter.updateList(mDateList.get(selectedMonth).days);
        mDaysListAdapter.notifyDataSetChanged();

        mDateListLayout.setVisibility(View.VISIBLE);
        mRvDays.setVisibility(View.VISIBLE);
    }

    /**
     * Show Times on select day
     */
    private void handleSelectDay(int position) {
        if (position != -1) {
            mTimeAdapter.resetTime();
            mTimeAdapter.updateList(mDateList.get(selectedMonth).days.get(position).times);
            mTimeAdapter.notifyDataSetChanged();
            mRvTime.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Select the previous day
     */
    public void previousMonth() {
        this.selectedMonth = mDateLayout.findFirstVisibleItemPosition();
        if (this.selectedMonth > 0) {
            selectedMonth--;
            mRvAppointmentDate.smoothScrollToPosition(selectedMonth);
        }
    }

    /**
     * Select the next day
     */
    public void nextMonth() {
        this.selectedMonth = mDateLayout.findFirstVisibleItemPosition();
        if (this.selectedMonth < (mDateList.size() - 1)) {
            selectedMonth++;
            mRvAppointmentDate.smoothScrollToPosition(selectedMonth);
        }
    }
}
