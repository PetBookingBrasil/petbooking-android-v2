package com.petbooking.UI.Menu.Calendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.petbooking.API.Pet.PetService;
import com.petbooking.Components.GravitySnapHelper.GravitySnapHelper;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.UI.Widget.HorizontalCalendar;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

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

    /**
     * Pet List
     */
    private RecyclerView mRvPets;
    private LinearLayoutManager mLayoutManager;
    private PetCalendarListAdapter mPetAdapter;
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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        getSupportActionBar().setElevation(0);

        mPetService = new PetService();
        mPetList = new ArrayList<>();
        userId = SessionManager.getInstance().getUserLogged().id;

        endDate.add(Calendar.MONTH, 3);
        startDate.add(Calendar.MONTH, -3);

        mRvPets = (RecyclerView) findViewById(R.id.pet_list);
        mLayoutManager = new LinearLayoutManager(this);
        mPetAdapter = new PetCalendarListAdapter(this, mPetList);
        mPetSnapHelper = new GravitySnapHelper(Gravity.START);

        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPets.setLayoutManager(mLayoutManager);
        mRvPets.setHasFixedSize(true);
        mRvPets.setAdapter(mPetAdapter);
        mPetSnapHelper.attachToRecyclerView(mRvPets);


        mHCCalendar = (HorizontalCalendar) findViewById(R.id.horizontal_calendar);
        mHCCalendar.setDateInterval(startDate.getTime(), endDate.getTime());

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
}
