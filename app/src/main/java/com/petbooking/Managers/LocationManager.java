package com.petbooking.Managers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.petbooking.Models.User;
import com.petbooking.Models.UserAddress;
import com.petbooking.R;
import com.petbooking.Utils.CommonUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

/**
 * Created by Luciano José on 13/04/2017.
 */

public class LocationManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static LocationManager instance;

    private Context mContext;
    private double mLastLocationX;
    private double mLastLocationY;
    private GoogleApiClient mGoogleApiClient;
    private WeakReference<LocationReadyCallback> mLocationReadyCallback;

    private LocationManager() {
    }

    public static LocationManager getInstance() {
        if (instance == null) {
            instance = new LocationManager();
        }
        return instance;
    }

    public void initialize(Context context) {
        this.mContext = context;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        int locationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        if (locationPermission == PackageManager.PERMISSION_GRANTED) {
            setLastLocation(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
            requestLocation();
        }
    }

    public void requestLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        int locationPolicy = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        if (locationPolicy == PackageManager.PERMISSION_GRANTED && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        setLastLocation(getCurrentLocationWithLocationService());
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            return;
        }

        setLastLocation(location);
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        if (mLocationReadyCallback != null && mLocationReadyCallback.get() != null) {
            mLocationReadyCallback.get().onLocationReady(getLocationCityState());
        }
    }

    private void setLastLocation(Location lastLocation) {
        if (lastLocation == null) {
            return;
        }
        mLastLocationX = lastLocation.getLatitude();
        mLastLocationY = lastLocation.getLongitude();
    }

    public String getLocationCoords() {
        return String.format(Locale.getDefault(), "%f;%f", mLastLocationX, mLastLocationY).replace(",", ".").replace(";", ",");
    }

    public void getLocationCityState(LocationReadyCallback locationReadyCallback) {
        mLocationReadyCallback = new WeakReference<LocationReadyCallback>(locationReadyCallback);
        int locationPolicy = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        if (locationPolicy == PackageManager.PERMISSION_GRANTED && mGoogleApiClient.isConnected()) {
            requestLocation();
        } else {
            onLocationChanged(getCurrentLocationWithLocationService());
        }
    }

    public String getLocationCityState() {
        if (mLastLocationX == 0) {
            return mContext.getString(R.string.prompt_loading);
        }
        String cityState = mContext.getString(R.string.error_no_location);
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(mLastLocationX, mLastLocationY, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                cityState = address.getLocality() + " - " + address.getAdminArea();
                Log.d("ADDRESS", new Gson().toJson(address));
            }
        } catch (IOException e) {
            return cityState;
        }
        return cityState;
    }

    public UserAddress getAddress() {
        UserAddress userAddress = null;
        if (mLastLocationX == 0) {
            return null;
        }
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(mLastLocationX, mLastLocationY, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                userAddress = CommonUtils.parseAddress(address);
            }
        } catch (IOException e) {
            return userAddress;
        }

        return userAddress;
    }

    private Location getCurrentLocationWithLocationService() {
        android.location.LocationManager locationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        int locCoarsePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
        int locFinePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        if (locCoarsePermission == PackageManager.PERMISSION_GRANTED || locFinePermission == PackageManager.PERMISSION_GRANTED) {
            return locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
        }
        return null;
    }

    public interface LocationReadyCallback {

        void onLocationReady(String locationCityState);

    }

}