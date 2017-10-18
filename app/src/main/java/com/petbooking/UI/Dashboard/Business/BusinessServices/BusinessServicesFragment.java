package com.petbooking.UI.Dashboard.Business.BusinessServices;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petbooking.Models.Artist;
import com.petbooking.Models.Genre;
import com.petbooking.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessServicesFragment extends Fragment {

    private RecyclerView mRecyclerView;

    //region - Fragment

    public BusinessServicesFragment() {
        // Required empty public constructor
    }

    public static BusinessServicesFragment newInstance(String id) {
        BusinessServicesFragment fragment = new BusinessServicesFragment();
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
        View view = inflater.inflate(R.layout.fragment_business_services, container, false);

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
        List<Genre> genres = getGenres();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        GenreAdapter adapter = new GenreAdapter(genres);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    private List<Genre> getGenres() {
        List<Artist> artistsA = new ArrayList<>();
        artistsA.add(new Artist("A1"));
        artistsA.add(new Artist("A2"));
        artistsA.add(new Artist("A3"));
        artistsA.add(new Artist("A4"));

        List<Artist> artistsB = new ArrayList<>();
        artistsB.add(new Artist("B1"));
        artistsB.add(new Artist("B2"));
        artistsB.add(new Artist("B3"));
        artistsB.add(new Artist("B4"));
        artistsB.add(new Artist("B5"));
        artistsB.add(new Artist("B6"));

        List<Artist> artistsC = new ArrayList<>();
        artistsC.add(new Artist("C1"));
        artistsC.add(new Artist("C2"));
        artistsC.add(new Artist("C3"));

        List<Artist> artistsD = new ArrayList<>();
        artistsD.add(new Artist("D1"));
        artistsD.add(new Artist("D2"));

        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("A", artistsA));
        genres.add(new Genre("B", artistsB));
        genres.add(new Genre("C", artistsC));
        genres.add(new Genre("D", artistsD));

        return genres;
    }

    //endregion

}
