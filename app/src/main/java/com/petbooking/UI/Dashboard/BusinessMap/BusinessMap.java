package com.petbooking.UI.Dashboard.BusinessMap;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.R;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessMap extends Fragment implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private BusinessService mBusinessService;
    private LocationManager mLocationManager;
    private String userId;
    private MapView mMapView;
    private GoogleMap mGMap = null;
    private ArrayList<Business> mBusinessList;
    private Business selectedBusiness;

    /**
     * Business Elements
     */
    private View mBusinessLayout;
    GoogleMap.OnMapClickListener mapListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            mBusinessLayout.setVisibility(View.GONE);
        }
    };
    private TextView mTvName;
    GoogleMap.OnMarkerClickListener markerListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            int index = Integer.parseInt(marker.getSnippet());
            selectedBusiness = mBusinessList.get(index);

            updateBusinessInfo(selectedBusiness);

            return true;
        }
    };

    public BusinessMap() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_map, container, false);
        Bundle mapViewBundle = null;

        mBusinessService = new BusinessService();
        mLocationManager = LocationManager.getInstance();
        mBusinessList = new ArrayList<>();
        userId = SessionManager.getInstance().getUserLogged().id;

        mBusinessLayout = view.findViewById(R.id.business_layout);
        mTvName = (TextView) view.findViewById(R.id.business_name);

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
        listBusiness();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGMap = googleMap;
        mGMap.setOnMarkerClickListener(markerListener);
        mGMap.setOnMapClickListener(mapListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     * List Business
     */
    public void listBusiness() {
        mBusinessService.listBusiness(mLocationManager.getLocationCoords(), userId, 1, 30, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mBusinessList = (ArrayList<Business>) response;
                createMarkers(mBusinessList);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    /**
     * Create Markes from Business list
     *
     * @param businessList
     */
    public void createMarkers(ArrayList<Business> businessList) {
        int index = 0;
        for (Business business : businessList) {
            if (business.latitude != null && business.longitude != null) {
                LatLng position = new LatLng(business.latitude, business.longitude);
                Marker marker = mGMap.addMarker(new MarkerOptions()
                        .title(business.name)
                        .snippet(String.valueOf(index))
                        .position(position)
                        .icon(BitmapDescriptorFactory.fromBitmap(AppUtils.getBitmap(getContext(), R.drawable.ic_marker))));

            }
            index++;
        }
    }

    /**
     * Update Business Info
     * when Click on Mark
     * @param business
     */
    public void updateBusinessInfo(Business business){
        mTvName.setText(business.name);
        mBusinessLayout.setVisibility(View.VISIBLE);
    }

}
