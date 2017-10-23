package com.petbooking.UI.Dashboard.Business.Schedule;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleItem;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleSection;

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
        List<ScheduleSection> genres = getGenres();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        ScheduleAdapter adapter = new ScheduleAdapter(genres);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
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
        genres.add(new ScheduleSection(1, "Selecionar pet", artistsA));
        genres.add(new ScheduleSection(2, "Categoria do serviço", artistsB));
        genres.add(new ScheduleSection(3, "Serviço e adicionais", artistsC));
        genres.add(new ScheduleSection(4, "Profissional", artistsD));
        genres.add(new ScheduleSection(5, "Dia e horário", artistsE));

        return genres;
    }

    //endregion

}
