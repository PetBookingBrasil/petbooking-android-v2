package com.petbooking.firebase;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Victor on 22/07/18.
 */

public class FirebaseService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseService";



    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }
}
