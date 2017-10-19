package com.petbooking.UI.Dashboard.Business.Schedule;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleGroupItem;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleGroup;
import com.petbooking.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private RecyclerView mRecyclerView;

    //region - Fragment

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance(String id) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("businessId", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        findViewsById(view);
        setupViews();

        return view;
    }

    //endregion

    //region - Private

    private void findViewsById(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
    }

    private void setupViews() {
        List<ScheduleGroup> genres = getGenres();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        ScheduleAdapter adapter = new ScheduleAdapter(genres);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    private List<ScheduleGroup> getGenres() {
        List<ScheduleGroupItem> artistsA = new ArrayList<>();
        artistsA.add(new ScheduleGroupItem("A1"));
        artistsA.add(new ScheduleGroupItem("A2"));
        artistsA.add(new ScheduleGroupItem("A3"));
        artistsA.add(new ScheduleGroupItem("A4"));

        List<ScheduleGroupItem> artistsB = new ArrayList<>();
        artistsB.add(new ScheduleGroupItem("B1"));
        artistsB.add(new ScheduleGroupItem("B2"));
        artistsB.add(new ScheduleGroupItem("B3"));
        artistsB.add(new ScheduleGroupItem("B4"));
        artistsB.add(new ScheduleGroupItem("B5"));
        artistsB.add(new ScheduleGroupItem("B6"));

        List<ScheduleGroupItem> artistsC = new ArrayList<>();
        artistsC.add(new ScheduleGroupItem("C1"));
        artistsC.add(new ScheduleGroupItem("C2"));
        artistsC.add(new ScheduleGroupItem("C3"));

        List<ScheduleGroupItem> artistsD = new ArrayList<>();
        artistsD.add(new ScheduleGroupItem("D1"));
        artistsD.add(new ScheduleGroupItem("D2"));

        List<ScheduleGroupItem> artistsE = new ArrayList<>();
        artistsE.add(new ScheduleGroupItem("E1"));
        artistsE.add(new ScheduleGroupItem("E2"));
        artistsE.add(new ScheduleGroupItem("E3"));
        artistsE.add(new ScheduleGroupItem("E4"));

        List<ScheduleGroup> genres = new ArrayList<>();
        genres.add(new ScheduleGroup("Selecionar pet", artistsA));
        genres.add(new ScheduleGroup("Categoria do serviço", artistsB));
        genres.add(new ScheduleGroup("Serviço e adicionais", artistsC));
        genres.add(new ScheduleGroup("Profissional", artistsD));
        genres.add(new ScheduleGroup("Dia e horário", artistsE));

        return genres;
    }

    //endregion

}
