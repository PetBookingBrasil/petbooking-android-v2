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

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private String mBusinessId;

    private ScheduleAdapter mAdapter;

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new ScheduleAdapter(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getContent() {
        // pets
        String userID = SessionManager.getInstance().getUserLogged().id;
        mPetService.listPets(userID, mListPetsAPICallback);

        // business categories
        mBusinessService.listBusinessCategories(mBusinessId, mListBusinessCategoriesAPICallback);
    }

    private void listPetsLoaded() {
        mAdapter.listPetsLoaded();

        BusinessActivity activity = (BusinessActivity) getActivity();
        activity.getAdapter().setListPetsLoaded(true);
    }

    private void listBusinessCategoriesLoaded() {
        mAdapter.listBusinessCategoriesLoaded();

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
