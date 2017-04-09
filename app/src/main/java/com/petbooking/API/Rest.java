package com.petbooking.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Interfaces.Callback;
import com.petbooking.Models.GenericArrayList;
import com.petbooking.Models.GenericResponse;
import com.petbooking.Utils.APIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Luciano José on 09/04/2017.
 */

public class Rest {

    private Context mContext;
    private Gson mResponseManager;
    //private SessionManager mSessionManager;
    private Class responseClassType;

    private JSONObject objectResponse;
    private JSONArray arrayResponse;

    private int method;
    private String url;
    private String token;
    private Callback callback;

    private Map<String, String> headers;
    private JsonObjectRequest mRequest;
    private JSONObject body;

    public Rest(Context context, Class responseType) {
        this.mContext = context;

        /*if (mSessionManager == null) {
            mSessionManager = SessionManager.getInstance(mContext);
        }*/

        if (mResponseManager == null) {
            mResponseManager = new Gson();
        }

        this.responseClassType = responseType;
    }

    public Rest get(String url) {
        this.method = com.android.volley.Request.Method.GET;
        this.url = url;
        this.body = null;
        return this;
    }

    public Rest one(String url, String id) {
        this.method = com.android.volley.Request.Method.GET;
        this.url = url.replace(APIConstants.PATH_PARAM, id);
        this.body = null;
        return this;
    }

    public Rest post(String url, Object body) {
        this.method = com.android.volley.Request.Method.POST;
        this.url = url;
        this.body = APIUtils.getBody(body);
        return this;
    }

    public Rest put(String url, String id) {
        this.method = com.android.volley.Request.Method.PUT;
        this.url = url.replace(APIConstants.PATH_PARAM, id);
        this.body = null;
        return this;
    }

    public Rest put(String url, String id, Object body) {
        this.method = com.android.volley.Request.Method.PUT;
        this.url = url.replace(APIConstants.PATH_PARAM, id);
        this.body = APIUtils.getBody(body);
        return this;
    }

    public Rest setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public JsonObjectRequest build() {
        mRequest = new JsonObjectRequest(this.method,
                this.url,
                this.body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = null;
                        Object object = null;

                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("ERROR");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // token = mSessionManager.getToken();
                // Log.d("TOKEN", token);
                headers = APIUtils.getHeaders("", "");
                return headers;
            }
        };

        return mRequest;
    }

    public Object parseResponse(JSONObject response) {
        Object object;

        if (response.has("token") || responseClassType.equals(GenericResponse.class)) {
            object = mResponseManager.fromJson(response.toString(), responseClassType);
        } else {
            objectResponse = response.optJSONObject("data");

            if (objectResponse != null) {
                object = mResponseManager.fromJson(objectResponse.toString(), responseClassType);
            } else {
                arrayResponse = response.optJSONArray("data");
                object = mResponseManager.fromJson(arrayResponse.toString(), new GenericArrayList<>(responseClassType));
            }
        }
        return object;
    }

}
