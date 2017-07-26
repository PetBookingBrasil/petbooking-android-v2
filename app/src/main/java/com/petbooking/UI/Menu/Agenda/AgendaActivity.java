package com.petbooking.UI.Menu.Agenda;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.petbooking.API.User.Models.ScheduleResp;
import com.petbooking.API.User.UserService;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.UI.Dialogs.ConfirmDialogFragment;
import com.petbooking.UI.Widget.HorizontalCalendar;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AgendaActivity extends AppCompatActivity implements ConfirmDialogFragment.FinishDialogListener {

    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Date today = new Date();
    private UserService mUserService;
    private String userId;

    private ConfirmDialogFragment mConfirmDialogFragment;
    private int serviceAction = -1;
    private boolean hasMorePast = true;
    private boolean hasMoreFuture = true;
    private boolean isFirstScroll = true;

    /**
     * Calendar
     */
    private RelativeLayout mRlCalendar;
    private HorizontalCalendar mHCCalendar;
    private ArrayList<ScheduleResp.Schedule> mScheduleList;
    private ImageView mBtnPreviousDate;
    private ImageView mBtnNextDate;


    /**
     * Pet List
     */
    private RecyclerView mRvPets;
    private LinearLayoutManager mPetLayoutManager;
    private PetCalendarListAdapter mPetAdapter;
    private ArrayList<Pet> mPetList;

    /**
     * Service List
     */
    private RecyclerView mRvServices;
    private LinearLayoutManager mServicesLayoutManager;
    private AgendaServiceListAdapter mServicesAdapter;

    /**
     * Controll
     */
    private int currentScheduleIndex = -1;
    private int currentPetIndex = -1;
    private ScheduleResp.Schedule currentSchedule;
    private ScheduleResp.PetSchedule currentPet;

    AgendaServiceListAdapter.OnServiceActionListener onServiceActionListener = new AgendaServiceListAdapter.OnServiceActionListener() {
        @Override
        public void onAction(int position, int action) {
            serviceAction = action;
            showConfirmDialog();
        }
    };


    HorizontalCalendar.OnDateScrollListener onDateScrollListener = new HorizontalCalendar.OnDateScrollListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScroll(int position) {
            if (position != currentScheduleIndex) {
                currentScheduleIndex = position;
                currentSchedule = mScheduleList.get(currentScheduleIndex);

                updateStyle(currentSchedule.date);
                handleSelectedDate();
                selectFirstPet();

                if (!isFirstScroll) {
                    if (mHCCalendar.isFirstDate() && hasMorePast) {
                        listPastSchedules(currentSchedule.date);
                    } else if (mHCCalendar.isLastDate() && hasMoreFuture) {
                        listFutureSchedules(currentSchedule.date);
                    }
                }

                isFirstScroll = false;
            }
        }
    };

    PetCalendarListAdapter.OnSelectPetListener selectPetListener = new PetCalendarListAdapter.OnSelectPetListener() {
        @Override
        public void onSelect(int position) {
            if (position != -1) {
                currentPetIndex = position;
                currentPet = currentSchedule.pets.get(currentPetIndex);

                handleSelectedPet();
            } else {
                currentPetIndex = -1;
                currentPet = null;
            }

        }
    };

    View.OnClickListener btnDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemId = v.getId();
            if (itemId == mBtnPreviousDate.getId()) {
                mHCCalendar.previousDay();
            } else {
                mHCCalendar.nextDay();
            }

            if (currentScheduleIndex != mHCCalendar.getCurrentDateIndex()) {
                handleSelectedDate();
            }

            currentScheduleIndex = mHCCalendar.getCurrentDateIndex();
            currentSchedule = mScheduleList.get(currentScheduleIndex);
            updateStyle(currentSchedule.date);
            selectFirstPet();

            if (mHCCalendar.isFirstDate() && hasMorePast) {
                listPastSchedules(currentSchedule.date);
            } else if (mHCCalendar.isLastDate() && hasMoreFuture) {
                listFutureSchedules(currentSchedule.date);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        mUserService = new UserService();
        userId = SessionManager.getInstance().getUserLogged().id;

        getSupportActionBar().setElevation(0);

        mPetList = new ArrayList<>();
        mScheduleList = new ArrayList<>();

        mConfirmDialogFragment = ConfirmDialogFragment.newInstance();

        /**
         * Create Pet RecyclerView
         */
        mRvPets = (RecyclerView) findViewById(R.id.pet_list);
        mPetLayoutManager = new LinearLayoutManager(this);
        mPetAdapter = new PetCalendarListAdapter(this, mPetList, selectPetListener);

        mPetLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPets.setLayoutManager(mPetLayoutManager);
        mRvPets.setHasFixedSize(true);
        mRvPets.setAdapter(mPetAdapter);

        /**
         * Create Services RecyclerView
         */
        mRvServices = (RecyclerView) findViewById(R.id.services_list);
        mServicesLayoutManager = new LinearLayoutManager(this);
        mServicesAdapter = new AgendaServiceListAdapter(this, new ArrayList<BusinessServices>(), onServiceActionListener);

        mServicesLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvServices.setLayoutManager(mServicesLayoutManager);
        mRvServices.setHasFixedSize(true);
        mRvServices.setAdapter(mServicesAdapter);

        /**
         * Create Calendar Widget
         */
        mRlCalendar = (RelativeLayout) findViewById(R.id.calendar_layout);
        mHCCalendar = (HorizontalCalendar) findViewById(R.id.horizontal_calendar);
        mHCCalendar.setOnDateScrollListener(onDateScrollListener);

        mBtnPreviousDate = (ImageView) findViewById(R.id.previous_date);
        mBtnNextDate = (ImageView) findViewById(R.id.next_date);

        mBtnPreviousDate.setOnClickListener(btnDateListener);
        mBtnNextDate.setOnClickListener(btnDateListener);

        listSchedules();
    }

    /**
     * Handle selected date
     */
    public void handleSelectedDate() {
        mPetAdapter.resetSelectedPosition();
        mRvServices.setVisibility(View.GONE);

        showPets(currentSchedule.pets);
    }

    /**
     * Handle Selected Pet
     */
    private void handleSelectedPet() {
        ArrayList<BusinessServices> services = new ArrayList<>();

        for (ScheduleResp.Event event : currentPet.events) {
            services.add(APIUtils.parseService(event));
        }

        mServicesAdapter.updateList(services);
        mServicesAdapter.notifyDataSetChanged();
        mRvServices.setVisibility(View.VISIBLE);
    }

    /**
     * Show pets
     *
     * @param petsSchedule
     */
    public void showPets(ArrayList<ScheduleResp.PetSchedule> petsSchedule) {
        ArrayList<Pet> pets = new ArrayList<>();

        for (ScheduleResp.PetSchedule petSchedule : petsSchedule) {
            pets.add(new Pet(petSchedule.id, petSchedule.name, petSchedule.photo));
        }

        mPetAdapter.updateList(pets);
        mPetAdapter.notifyDataSetChanged();
    }

    public void showConfirmDialog() {
        int dialogMessage = serviceAction == AppConstants.CANCEL_SERVICE ? R.string.cancel_service_message : R.string.remove_pet_text;
        int dialogTitle = serviceAction == AppConstants.CANCEL_SERVICE ? R.string.cancel_service_title : R.string.remove_pet_text;
        int confirmText = serviceAction == AppConstants.CANCEL_SERVICE ? R.string.cancel_service_button : R.string.remove_pet_text;

        mConfirmDialogFragment.setDialogInfo(dialogTitle, dialogMessage, confirmText);
        mConfirmDialogFragment.setAction(serviceAction);
        mConfirmDialogFragment.setCancelText(R.string.dialog_back);
        mConfirmDialogFragment.show(getSupportFragmentManager(), "CONFIRM_DIALOG");
    }

    /**
     * List Schedules
     */
    public void listSchedules() {
        AppUtils.showLoadingDialog(this);
        mUserService.listSchedules(userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mScheduleList = (ArrayList<ScheduleResp.Schedule>) response;
                mHCCalendar.setSchedules(mScheduleList);

                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * List Past Schedules
     *
     * @param date
     */
    public void listPastSchedules(Date date) {
        AppUtils.showLoadingDialog(this);
        mUserService.listPastSchedules(userId, mDateFormat.format(date), true, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                ArrayList<ScheduleResp.Schedule> scheduleList = (ArrayList<ScheduleResp.Schedule>) response;

                if (scheduleList.size() == 0) {
                    hasMorePast = false;
                } else {
                    scheduleList.addAll(mScheduleList);
                    mScheduleList = scheduleList;
                    mHCCalendar.addPastDate(scheduleList);
                }

                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * List Future Schedules
     *
     * @param date
     */
    public void listFutureSchedules(Date date) {
        AppUtils.showLoadingDialog(this);
        mUserService.listFutureSchedules(userId, mDateFormat.format(date), true, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                ArrayList<ScheduleResp.Schedule> scheduleList = (ArrayList<ScheduleResp.Schedule>) response;


                if (scheduleList.size() == 0) {
                    hasMoreFuture = false;
                } else {
                    mScheduleList.addAll(scheduleList);
                    mHCCalendar.addFutureDate(scheduleList);
                }

                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * Select first pet
     */
    public void selectFirstPet() {
        currentPetIndex = 0;
        currentPet = currentSchedule.pets.get(currentPetIndex);
        mPetAdapter.selectPet(currentPetIndex);

        handleSelectedPet();
    }

    /**
     * Update Style Based on Date
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateStyle(Date date) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        int pastColor = getResources().getColor(R.color.past_date_color);
        int futureColor = getResources().getColor(R.color.brand_primary);

        if (date.before(today)) {
            mRlCalendar.setBackgroundColor(pastColor);
            window.setStatusBarColor(pastColor);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(pastColor));
        } else if (date.after(today)) {
            mRlCalendar.setBackgroundColor(futureColor);
            window.setStatusBarColor(futureColor);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(futureColor));
        }
    }

    @Override
    public void onFinishDialog(int action) {
        if (action == AppConstants.CANCEL_SERVICE) {
            Log.d("CANCEL", "CANCEL");
        } else if (action == AppConstants.RESCHEDULE_SERVICE) {
            Log.d("RESCHEDULE", "RESCHEDULE");
        }
    }
}
