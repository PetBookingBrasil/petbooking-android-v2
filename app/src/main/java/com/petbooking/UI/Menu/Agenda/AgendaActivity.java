package com.petbooking.UI.Menu.Agenda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.petbooking.API.Generic.AvatarResp;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Models.Appointment;
import com.petbooking.Models.AppointmentInfo;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.UI.Dialogs.ConfirmDialogFragment;
import com.petbooking.UI.Widget.HorizontalCalendar;

import java.util.ArrayList;
import java.util.Date;

public class AgendaActivity extends AppCompatActivity implements ConfirmDialogFragment.FinishDialogListener {

    private ConfirmDialogFragment mConfirmDialogFragment;
    private int serviceAction = -1;

    /**
     * Calendar
     */
    private HorizontalCalendar mHCCalendar;
    private ArrayList<Appointment> mAppointmentsList;
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
    private int currentAppointmentIndex = -1;
    private int currentPetIndex = -1;
    private Appointment currentAppointment;
    private AppointmentInfo currentInfo;

    AgendaServiceListAdapter.OnServiceActionListener onServiceActionListener = new AgendaServiceListAdapter.OnServiceActionListener() {
        @Override
        public void onAction(int position, int action) {
            serviceAction = action;
            showConfirmDialog();
        }
    };


    HorizontalCalendar.OnDateScrollListener onDateScrollListener = new HorizontalCalendar.OnDateScrollListener() {
        @Override
        public void onScroll(int position) {
            if (position != currentAppointmentIndex) {
                currentAppointmentIndex = position;
                currentAppointment = mAppointmentsList.get(currentAppointmentIndex);

                handleSelectedDate();
            }
        }
    };

    PetCalendarListAdapter.OnSelectPetListener selectPetListener = new PetCalendarListAdapter.OnSelectPetListener() {
        @Override
        public void onSelect(int position) {
            currentPetIndex = position;
            currentInfo = currentAppointment.appointmentsInfo.get(currentPetIndex);

            handleSelectedPet();
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

            currentAppointmentIndex = mHCCalendar.getCurrentDateIndex();
            currentAppointment = mAppointmentsList.get(currentAppointmentIndex);

            handleSelectedDate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        getSupportActionBar().setElevation(0);

        mPetList = new ArrayList<>();
        mAppointmentsList = new ArrayList<>();
        createData();

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
        mHCCalendar = (HorizontalCalendar) findViewById(R.id.horizontal_calendar);
        mHCCalendar.setAppointments(mAppointmentsList);
        mHCCalendar.setOnDateScrollListener(onDateScrollListener);

        mBtnPreviousDate = (ImageView) findViewById(R.id.previous_date);
        mBtnNextDate = (ImageView) findViewById(R.id.next_date);

        mBtnPreviousDate.setOnClickListener(btnDateListener);
        mBtnNextDate.setOnClickListener(btnDateListener);
    }

    /**
     * Handle selected date
     */
    public void handleSelectedDate() {
        Log.i("CDATE", currentAppointment.date.toString());

        showPets(currentAppointment.appointmentsInfo);
    }

    /**
     * Handle Selected Pet
     */
    private void handleSelectedPet() {
        Log.i("CPET", currentInfo.petName);
        mServicesAdapter.updateList(currentInfo.services);
        mServicesAdapter.notifyDataSetChanged();
        mRvServices.setVisibility(View.VISIBLE);
    }

    /**
     * Show pets
     *
     * @param appointmentsInfo
     */
    public void showPets(ArrayList<AppointmentInfo> appointmentsInfo) {
        ArrayList<Pet> pets = new ArrayList<>();

        for (AppointmentInfo info : appointmentsInfo) {
            AvatarResp avatar = new AvatarResp();
            avatar.url = "http://www.gaf.com.as";
            pets.add(new Pet(info.petId, info.petName, avatar));
        }

        mPetAdapter.updateList(pets);
        mPetAdapter.notifyDataSetChanged();
    }

    public void createData() {
        BusinessServices service;
        BusinessServices service1;
        Appointment appointment;
        AppointmentInfo info;
        ArrayList<BusinessServices> sub = new ArrayList<>();
        ArrayList<BusinessServices> mServiceList = null;
        ArrayList<AppointmentInfo> infoList = null;
        for (int i = 1; i < 4; i++) {
            mServiceList = new ArrayList<>();
            sub = new ArrayList<>();
            infoList = new ArrayList<>();
            service = new BusinessServices(i + "", "Serviço 1", "10:30", "12:00", i + "", "Serviço Número " + i, i * 20.33, "Petland", "Profissional " + i);
            service1 = new BusinessServices(i + "", "Serviço 2", "10:30", "12:00", i + "", "Serviço Número " + i, i * 20.33, "Petland", "Profissional " + i);
            sub.add(new BusinessServices(i + "", "SB " + i, i + "", "SB Número " + i, i * 20.33));
            sub.add(new BusinessServices(i + "", "SB " + i, i + "", "SB Número " + i, i * 20.33));
            sub.add(new BusinessServices(i + "", "SB " + i, i + "", "SB Número " + i, i * 20.33));
            service.setAdditionalServices(sub);


            mServiceList.add(service);
            mServiceList.add(service1);

            info = new AppointmentInfo(i + "", "Pet 1", mServiceList);
            infoList.add(info);
            info = new AppointmentInfo(i + "", "Pet 2", mServiceList);
            infoList.add(info);
            info = new AppointmentInfo(i + "", "Nome gigante do pet", mServiceList);
            infoList.add(info);
            appointment = new Appointment(new Date("05/0" + i + "/2017"), infoList);
            mAppointmentsList.add(appointment);
        }
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

    @Override
    public void onFinishDialog(int action) {
        if (action == AppConstants.CANCEL_SERVICE) {
            Log.d("CANCEL", "CANCEL");
        } else if (action == AppConstants.RESCHEDULE_SERVICE) {
            Log.d("RESCHEDULE", "RESCHEDULE");
        }
    }
}
