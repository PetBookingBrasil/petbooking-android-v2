package com.petbooking.Managers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Luciano José on 09/04/2017.
 */

public class APIManager {

    private static APIManager mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private APIManager(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized APIManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new APIManager(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
