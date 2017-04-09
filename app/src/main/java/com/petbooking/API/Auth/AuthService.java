package com.petbooking.API.Auth;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.petbooking.API.Rest;
import com.petbooking.Interfaces.Callback;
import com.petbooking.Managers.APIManager;
import com.petbooking.Models.ConsumerRqt;
import com.petbooking.Models.GenericResponse;

/**
 * Created by Luciano José on 09/04/2017.
 */

public class AuthService {

    private static AuthService mInstance;
    private Context mContext;
    private APIManager mApiManager;
    JsonObjectRequest mRequest;

    public static AuthService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AuthService(context);
        }
        return mInstance;
    }

    public AuthService(Context context) {
        this.mContext = context;
        mApiManager = APIManager.getInstance(context);
    }


    public void authConsumer(final Callback callback) {
        this.mRequest = new Rest(mContext, GenericResponse.class)
                .post(APIAuthConstants.CONSUMER_ENDPOINT, new ConsumerRqt())
                .setCallback(callback)
                .build();

        mApiManager.addToRequestQueue(this.mRequest);
    }

}
