package com.petbooking.UI.Dashboard.Business.Schedule;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petbooking.API.Appointment.AppointmentService;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.API.Pet.PetService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Pet;
import com.petbooking.Models.Professional;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.BusinessActivity;
import com.petbooking.Utils.AppUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private String mBusinessId;

    private ScheduleAdapter mAdapter;

    private final PetService mPetService = new PetService();
    private final BusinessService mBusinessService = new BusinessService();
    private final AppointmentService mAppointmentService = new AppointmentService();

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

    //region - Private view

    private void findViewsById(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
    }

    private void setupViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new ScheduleAdapter(getContext(), mAdapterOnClickListener);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    //endregion

    //region - Private service

    private void getContent() {
        // pets
        String userID = SessionManager.getInstance().getUserLogged().id;
        mPetService.listPets(userID, mListPetsAPICallback);

        // business categories
        mBusinessService.listBusinessCategories(mBusinessId, mListBusinessCategoriesAPICallback);
    }

    private void listServices() {
        String petId = mAdapter.getPetId();
        String categoryId = mAdapter.getCategoryId();

        AppUtils.showLoadingDialog(getContext());
        mAppointmentService.listServices(categoryId, petId, mListServicesAPICallback);
    }

    private void listProfessionals() {
        String serviceId = mAdapter.getServiceId();

        AppUtils.showLoadingDialog(getContext());
        mAppointmentService.listProfessional(serviceId, mListProfessionalsAPICallback);
    }

    private void listPetsLoaded(List<Pet> pets) {
        mAdapter.listPetsLoaded(pets);

        BusinessActivity activity = (BusinessActivity) getActivity();
        activity.getAdapter().setListPetsLoaded(true);
    }

    //endregion

    //region - Private helper

    private void listBusinessCategoriesLoaded(CategoryResp categoryResp) {
        mAdapter.listBusinessCategoriesLoaded(categoryResp);

        BusinessActivity activity = (BusinessActivity) getActivity();
        activity.getAdapter().setListBusinessCategoriesLoaded(true);
    }

    private void listServidesLoaded(List<BusinessServices> services) {
        AppUtils.hideDialog();
        mAdapter.listServicesLoaded(services);
    }

    private void listProfessionalsLoaded(List<Professional> professionals) {
        AppUtils.hideDialog();
        mAdapter.listProfessionalsLoaded(professionals);
    }

    //endregion

    //region - Listener

    private final ScheduleAdapter.OnClickListener mAdapterOnClickListener = new ScheduleAdapter.OnClickListener() {
        @Override
        public void onBusinessCategoryClicked(String id) {
            listServices();
        }

        @Override
        public void onServicesClicked(String id) {
            listProfessionals();
        }

    };

    //endregion

    //region - APICallback

    private final APICallback mListPetsAPICallback = new APICallback() {
        @Override
        public void onSuccess(Object response) {
            List<Pet> pets = (List<Pet>) response;
            listPetsLoaded(pets);
        }

        @Override
        public void onError(Object error) {
            listPetsLoaded(null);
        }
    };

    private final APICallback mListBusinessCategoriesAPICallback = new APICallback() {
        @Override
        public void onSuccess(Object response) {
            CategoryResp categoryResp = (CategoryResp) response;
            listBusinessCategoriesLoaded(categoryResp);
        }

        @Override
        public void onError(Object error) {
            listBusinessCategoriesLoaded(null);
        }
    };

    //endregion

    private final APICallback mListServicesAPICallback = new APICallback() {
        @Override
        public void onSuccess(Object response) {
            List<BusinessServices> services = (List<BusinessServices>) response;
            listServidesLoaded(services);
        }

        @Override
        public void onError(Object error) {
            listServidesLoaded(null);
        }
    };

    private final APICallback mListProfessionalsAPICallback = new APICallback() {
        @Override
        public void onSuccess(Object response) {
            List<Professional> professionals = (List<Professional>) response;
            listProfessionalsLoaded(professionals);
        }

        @Override
        public void onError(Object error) {
            listProfessionalsLoaded(null);
        }
    };

}
