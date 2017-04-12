package com.petbooking.Interfaces;

/**
 * Created by Luciano José on 08/01/2017.
 */

public interface Callback {

    void onSuccess(Object response);
    void onError(String message);

}
