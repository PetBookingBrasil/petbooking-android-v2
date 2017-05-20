package com.petbooking.Events;

/**
 * Created by Luciano José on 19/05/2017.
 */

public class LocationChangedEvt {

    public String cityState;

    public LocationChangedEvt(String cityState) {
        this.cityState = cityState;
    }
}
