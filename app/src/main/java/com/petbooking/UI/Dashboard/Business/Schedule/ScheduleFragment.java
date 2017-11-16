package com.petbooking.UI.Dashboard.Business.Schedule;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Pet.PetService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.BusinessActivity;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleItem;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleSection;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private String mBusinessId;

    private final PetService mPetService = new PetService();
    private final BusinessService mBusinessService = new BusinessService();

    //region - Fragment

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("businessId", id);

        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBusinessId = getArguments().getString("businessId", "0");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        findViewsById(view);
        setupViews();
        getContent();

        return view;
    }

    //endregion

    //region - Private

    private void findViewsById(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
    }

    private void setupViews() {
        List<ScheduleSection> genres = getGenres();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        ScheduleAdapter adapter = new ScheduleAdapter(genres);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    private void getContent() {
        // pets
        String userID = SessionManager.getInstance().getUserLogged().id;
        mPetService.listPets(userID, mListPetsAPICallback);

        // business categories
        mBusinessService.listBusinessCategories(mBusinessId, mListBusinessCategoriesAPICallback);
    }

    private List<ScheduleSection> getGenres() {
        List<ScheduleItem> artistsA = new ArrayList<>();
        artistsA.add(new ScheduleItem("A1"));
        artistsA.add(new ScheduleItem("A2"));
        artistsA.add(new ScheduleItem("A3"));
        artistsA.add(new ScheduleItem("A4"));

        List<ScheduleItem> artistsB = new ArrayList<>();
        artistsB.add(new ScheduleItem("B1"));
        artistsB.add(new ScheduleItem("B2"));
        artistsB.add(new ScheduleItem("B3"));
        artistsB.add(new ScheduleItem("B4"));
        artistsB.add(new ScheduleItem("B5"));
        artistsB.add(new ScheduleItem("B6"));

        List<ScheduleItem> artistsC = new ArrayList<>();
        artistsC.add(new ScheduleItem("C1"));
        artistsC.add(new ScheduleItem("C2"));
        artistsC.add(new ScheduleItem("C3"));

        List<ScheduleItem> artistsD = new ArrayList<>();
        artistsD.add(new ScheduleItem("D1"));
        artistsD.add(new ScheduleItem("D2"));

        List<ScheduleItem> artistsE = new ArrayList<>();
        artistsE.add(new ScheduleItem("E1"));
        artistsE.add(new ScheduleItem("E2"));
        artistsE.add(new ScheduleItem("E3"));
        artistsE.add(new ScheduleItem("E4"));

        List<ScheduleSection> genres = new ArrayList<>();

        ScheduleSection.Type typeA = ScheduleSection.Type.SELECT_PET;
        String titleA = ScheduleSection.getTitle(getContext(), typeA);
        genres.add(new ScheduleSection(titleA, typeA, artistsA));

        ScheduleSection.Type typeB = ScheduleSection.Type.SERVICE_CATEGORY;
        String titleB = ScheduleSection.getTitle(getContext(), typeB);
        genres.add(new ScheduleSection(titleB, typeB, artistsB));

        ScheduleSection.Type typeC = ScheduleSection.Type.ADDITIONAL_SERVICES;
        String titleC = ScheduleSection.getTitle(getContext(), typeC);
        genres.add(new ScheduleSection(titleC, typeC, artistsC));

        ScheduleSection.Type typeD = ScheduleSection.Type.PROFESSIONAL;
        String titleD = ScheduleSection.getTitle(getContext(), typeD);
        genres.add(new ScheduleSection(titleD, typeD, artistsD));

        ScheduleSection.Type typeE = ScheduleSection.Type.DAY_AND_TIME;
        String titleE = ScheduleSection.getTitle(getContext(), typeE);
        genres.add(new ScheduleSection(titleE, typeE, artistsE));

        return genres;
    }

    private void listPetsLoaded() {
        BusinessActivity activity = (BusinessActivity) getActivity();
        activity.getAdapter().setListPetsLoaded(true);
    }

    private void listBusinessCategoriesLoaded() {
        BusinessActivity activity = (BusinessActivity) getActivity();
        activity.getAdapter().setListBusinessCategoriesLoaded(true);
    }

    //endregion

    //region - APICallback

    private final APICallback mListPetsAPICallback = new APICallback() {
        @Override
        public void onSuccess(Object response) {
            listPetsLoaded();
        }

        @Override
        public void onError(Object error) {
            listPetsLoaded();
        }
    };

    private final APICallback mListBusinessCategoriesAPICallback = new APICallback() {
        @Override
        public void onSuccess(Object response) {
            listBusinessCategoriesLoaded();
        }

        @Override
        public void onError(Object error) {
            listBusinessCategoriesLoaded();
        }
    };

    //endregion

}
