package com.petbooking.UI.Dashboard.BusinessMap;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.petbooking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessMap extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    GoogleMap mGMap = null;

    public BusinessMap() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_map, container, false);

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGMap = googleMap;
        LatLng center = new LatLng(-8.0578381, -34.8828969);
        googleMap.addMarker(new MarkerOptions().position(center));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 16));
    }
}
