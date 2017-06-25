package com.petbooking.UI.Dashboard.BusinessMap;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.FavoriteResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.LocationManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.BusinessActivity;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessMap extends Fragment implements OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static Context mContext;
    private View fragmentView;
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
    private View mBusinessActiveLayout;
    private View mBusinessImportedLayout;
    private ImageView mIvBusinessPhoto;
    private ImageView mIvRatingStar;
    private ImageButton mBtnFavorite;
    private TextView mTvName;
    private TextView mTvStreet;
    private TextView mTvCity;
    private TextView mTvRate;
    private TextView mTvRatingCount;
    private TextView mTvDistance;

    GoogleMap.OnMarkerClickListener markerListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (marker.getSnippet().equals("CENTER")) {
                return false;
            }

            int index = Integer.parseInt(marker.getSnippet());
            selectedBusiness = mBusinessList.get(index);

            if (selectedBusiness.imported) {
                bindImportedBusiness(fragmentView);
                updateImportedBusinessInfo(selectedBusiness, index);
            } else {
                bindActiveBusiness(fragmentView);
                updateActiveBusinessInfo(selectedBusiness, index);
            }

            mGMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13));

            return true;
        }
    };

    GoogleMap.OnMapClickListener mapListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            mBusinessLayout.setVisibility(View.GONE);
        }
    };

    View.OnClickListener businessListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goToBusiness(selectedBusiness.id);
        }
    };


    LocationManager.LocationReadyCallback locationCallback = new LocationManager.LocationReadyCallback() {
        @Override
        public void onLocationReady(String locationCityState) {
            if (mLocationManager.getLastLatitude() != 0 && mLocationManager.getLastLongitude() != 0 && mGMap != null) {
                LatLng position = new LatLng(mLocationManager.getLastLatitude(), mLocationManager.getLastLongitude());
                mGMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12));
            }
        }
    };

    public BusinessMap() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_business_map, container, false);
        Bundle mapViewBundle = null;

        mContext = getContext();
        mBusinessService = new BusinessService();
        mLocationManager = LocationManager.getInstance();
        mLocationManager.setCallback(locationCallback);
        mLocationManager.requestLocation();
        mBusinessList = new ArrayList<>();
        userId = SessionManager.getInstance().getUserLogged().id;

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView = (MapView) fragmentView.findViewById(R.id.map);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
        listBusiness();

        return fragmentView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGMap = googleMap;
        mGMap.setOnMarkerClickListener(markerListener);
        mGMap.setOnMapClickListener(mapListener);

        if (mLocationManager.getLastLatitude() != 0 && mLocationManager.getLastLongitude() != 0) {
            LatLng position = new LatLng(mLocationManager.getLastLatitude(), mLocationManager.getLastLongitude());
            mGMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12));
        }
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
                int icon = business.imported ? R.drawable.ic_marker_imported : R.drawable.ic_marker;
                LatLng position = new LatLng(business.latitude, business.longitude);
                Marker marker = mGMap.addMarker(new MarkerOptions()
                        .title(business.name)
                        .snippet(String.valueOf(index))
                        .position(position)
                        .icon(BitmapDescriptorFactory.fromBitmap(AppUtils.getBitmap(getContext(), icon))));

            }
            index++;
        }
    }

    /**
     * Update active Business Info
     * when Click on Mark
     *
     * @param business
     */
    public void updateActiveBusinessInfo(final Business business, final int position) {

        String street = mContext.getResources().getString(R.string.business_street, business.street, business.streetNumber, business.city);
        String city = mContext.getResources().getString(R.string.business_city, business.city, business.state);
        String distance = mContext.getResources().getString(R.string.business_distance, String.format("%.2f", business.distance));
        String ratingCount = mContext.getResources().getQuantityString(R.plurals.business_rating_count, business.ratingCount, business.ratingCount);
        String average = String.format("%.1f", business.ratingAverage);

        if (business.favorited) {
            mBtnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            mBtnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }

        if (business.ratingCount == 0) {
            mTvRate.setVisibility(View.GONE);
            mTvRatingCount.setVisibility(View.GONE);
            mIvRatingStar.setVisibility(View.GONE);
        } else {
            mTvRate.setText(average);
            mTvRatingCount.setText(ratingCount);
            mIvRatingStar.setVisibility(View.GONE);
        }

        mTvName.setText(business.name);
        mTvStreet.setText(street);
        mTvCity.setText(city);
        mTvDistance.setText(distance);

        mBtnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (business.favorited) {
                    mBusinessService.deleteFavorite(business.favoritedId, new APICallback() {
                        @Override
                        public void onSuccess(Object response) {
                            mBusinessList.get(position).setFavorited(false);
                            mBusinessList.get(position).setFavoritedId("");
                            mBtnFavorite.setImageResource(R.drawable.ic_favorite_border);
                        }

                        @Override
                        public void onError(Object error) {

                        }
                    });
                } else {
                    mBusinessService.createFavorite(userId, business.id, new APICallback() {
                        @Override
                        public void onSuccess(Object response) {
                            FavoriteResp resp = (FavoriteResp) response;

                            mBusinessList.get(position).setFavorited(true);
                            mBusinessList.get(position).setFavoritedId(resp.data.id);
                            mBtnFavorite.setImageResource(R.drawable.ic_favorite_filled);
                        }

                        @Override
                        public void onError(Object error) {

                        }
                    });
                }
            }
        });

        Glide.with(mContext).load(business.image.url)
                .error(R.drawable.business_background)
                .placeholder(R.drawable.business_background)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .into(mIvBusinessPhoto);

        mBusinessLayout.setOnClickListener(businessListener);
        mBusinessLayout.setVisibility(View.VISIBLE);
        mBusinessActiveLayout.setVisibility(View.VISIBLE);
        mBusinessImportedLayout.setVisibility(View.GONE);
    }

    /**
     * Update imported Business Info
     * when Click on Mark
     *
     * @param business
     */
    public void updateImportedBusinessInfo(final Business business, final int position) {

        String street = mContext.getResources().getString(R.string.business_street, business.street, business.streetNumber, business.city);
        String distance = mContext.getResources().getString(R.string.business_distance, String.format("%.2f", business.distance));

        if (business.favorited) {
            mBtnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            mBtnFavorite.setImageResource(R.drawable.ic_favorite_border_black);
        }

        mTvName.setText(business.name);
        mTvStreet.setText(street);
        mTvDistance.setText(distance);

        mBtnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (business.favorited) {
                    mBusinessService.deleteFavorite(business.favoritedId, new APICallback() {
                        @Override
                        public void onSuccess(Object response) {
                            mBusinessList.get(position).setFavorited(false);
                            mBusinessList.get(position).setFavoritedId("");
                            mBtnFavorite.setImageResource(R.drawable.ic_favorite_border_black);
                        }

                        @Override
                        public void onError(Object error) {

                        }
                    });
                } else {
                    mBusinessService.createFavorite(userId, business.id, new APICallback() {
                        @Override
                        public void onSuccess(Object response) {
                            FavoriteResp resp = (FavoriteResp) response;

                            mBusinessList.get(position).setFavorited(true);
                            mBusinessList.get(position).setFavoritedId(resp.data.id);
                            mBtnFavorite.setImageResource(R.drawable.ic_favorite_filled);
                        }

                        @Override
                        public void onError(Object error) {

                        }
                    });
                }
            }
        });

        mBusinessLayout.setVisibility(View.VISIBLE);
        mBusinessActiveLayout.setVisibility(View.GONE);
        mBusinessImportedLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Bind Active Business
     * @param view
     */
    public void bindActiveBusiness(View view) {
        mBusinessLayout = view.findViewById(R.id.business_layout);
        mBusinessActiveLayout = view.findViewById(R.id.business_active);
        mBusinessImportedLayout = view.findViewById(R.id.business_imported);
        mIvBusinessPhoto = (ImageView) view.findViewById(R.id.business_photo);
        mIvRatingStar = (ImageView) view.findViewById(R.id.rating_star);
        mBtnFavorite = (ImageButton) view.findViewById(R.id.favorite_button);
        mTvName = (TextView) view.findViewById(R.id.business_name);
        mTvStreet = (TextView) view.findViewById(R.id.business_street);
        mTvCity = (TextView) view.findViewById(R.id.business_city);
        mTvRatingCount = (TextView) view.findViewById(R.id.ratings);
        mTvRate = (TextView) view.findViewById(R.id.business_rate);
        mTvDistance = (TextView) view.findViewById(R.id.business_distance);
    }

    /**
     * Bind Imported Business Elements
     * @param view
     */
    public void bindImportedBusiness(View view) {
        mBusinessLayout = view.findViewById(R.id.business_layout);
        mBusinessActiveLayout = view.findViewById(R.id.business_active);
        mBusinessImportedLayout = view.findViewById(R.id.business_imported);
        mBtnFavorite = (ImageButton) mBusinessImportedLayout.findViewById(R.id.favorite_button);
        mTvName = (TextView) mBusinessImportedLayout.findViewById(R.id.business_name);
        mTvStreet = (TextView) mBusinessImportedLayout.findViewById(R.id.business_street);
        mTvDistance = (TextView) mBusinessImportedLayout.findViewById(R.id.business_distance);
    }

    /**
     * Go to Business
     */
    public void goToBusiness(String businessId) {
        Intent businessIntent = new Intent(mContext, BusinessActivity.class);
        businessIntent.putExtra("businessId", businessId);
        mContext.startActivity(businessIntent);
    }

}
