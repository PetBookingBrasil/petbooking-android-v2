package com.petbooking.UI.Menu.Agenda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.petbooking.API.Generic.AvatarResp;
import com.petbooking.API.Pet.PetService;
import com.petbooking.Components.GravitySnapHelper.GravitySnapHelper;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Appointment;
import com.petbooking.Models.AppointmentInfo;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.UI.Widget.HorizontalCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AgendaActivity extends AppCompatActivity {

    private PetService mPetService;
    private String userId;
    private final Calendar startDate = Calendar.getInstance();
    private final Calendar endDate = Calendar.getInstance();

    /**
     * Calendar
     */
    private HorizontalCalendar mHCCalendar;
    private ImageView mBtnPreviousDate;
    private ImageView mBtnNextDate;
    private int currentDateIndex = -1;

    /**
     * Pet List
     */
    private RecyclerView mRvPets;
    private LinearLayoutManager mLayoutManager;
    private PetCalendarListAdapter mPetAdapter;
    private ArrayList<Appointment> appointmentsList;
    private ArrayList<Pet> mPetList;
    private SnapHelper mPetSnapHelper;

    View.OnClickListener btnDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemId = v.getId();
            if (itemId == mBtnPreviousDate.getId()) {
                mHCCalendar.previousDay();
            } else {
                mHCCalendar.nextDay();
            }

            currentDateIndex = mHCCalendar.getCurrentDateIndex();

            handleSelectedDate(currentDateIndex);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        getSupportActionBar().setElevation(0);

        mPetService = new PetService();
        mPetList = new ArrayList<>();
        userId = SessionManager.getInstance().getUserLogged().id;

        endDate.add(Calendar.MONTH, 3);
        startDate.add(Calendar.MONTH, -3);

        appointmentsList = new ArrayList<>();
        createData();

        mRvPets = (RecyclerView) findViewById(R.id.pet_list);
        mLayoutManager = new LinearLayoutManager(this);
        mPetAdapter = new PetCalendarListAdapter(this, mPetList);
        mPetSnapHelper = new GravitySnapHelper(Gravity.START);

        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPets.setLayoutManager(mLayoutManager);
        mRvPets.setHasFixedSize(true);
        mRvPets.setAdapter(mPetAdapter);
        //mPetSnapHelper.attachToRecyclerView(mRvPets);

        mHCCalendar = (HorizontalCalendar) findViewById(R.id.horizontal_calendar);
        mHCCalendar.setAppointments(appointmentsList);

        mBtnPreviousDate = (ImageView) findViewById(R.id.previous_date);
        mBtnNextDate = (ImageView) findViewById(R.id.next_date);

        mBtnPreviousDate.setOnClickListener(btnDateListener);
        mBtnNextDate.setOnClickListener(btnDateListener);

        listPets(userId);
    }

    /**
     * List Pets
     *
     * @param userId
     */
    private void listPets(String userId) {
        mPetService.listPets(userId, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mPetList = (ArrayList<Pet>) response;
                mPetAdapter.updateList(mPetList);
                mPetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    /**
     * Handle selected date
     *
     * @param index
     */
    public void handleSelectedDate(int index) {
        Appointment currentAppointment = appointmentsList.get(index);
        Log.d("CURRENT", currentAppointment.date.toString());

        showPets(currentAppointment.appointmentsInfo);
    }

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
        Appointment appointment;
        AppointmentInfo info;
        ArrayList<BusinessServices> sub = new ArrayList<>();
        ArrayList<BusinessServices> mServiceList = null;
        ArrayList<AppointmentInfo> infoList = null;
        for (int i = 1; i < 4; i++) {
            mServiceList = new ArrayList<>();
            sub = new ArrayList<>();
            infoList = new ArrayList<>();
            service = new BusinessServices(i + "", "Serviço " + i, i + "", "Serviço Número " + i, i * 20.33);
            sub.add(new BusinessServices(i + "", "SB " + i, i + "", "SB Número " + i, i * 20.33));
            sub.add(new BusinessServices(i + "", "SB " + i, i + "", "SB Número " + i, i * 20.33));
            sub.add(new BusinessServices(i + "", "SB " + i, i + "", "SB Número " + i, i * 20.33));
            service.setAdditionalServices(sub);

            mServiceList.add(service);

            info = new AppointmentInfo(i + "", "Pet 1", i + "", "Profissional " + i, mServiceList);
            infoList.add(info);
            info = new AppointmentInfo(i + "", "Pet 2", i + "", "Profissional " + i, mServiceList);
            infoList.add(info);
            info = new AppointmentInfo(i + "", "Pet deo Nome gigante 3", i + "", "Profissional " + i, mServiceList);
            infoList.add(info);
            appointment = new Appointment(new Date("05/0" + i + "/2017"), infoList);
            appointmentsList.add(appointment);
        }
    }
}
