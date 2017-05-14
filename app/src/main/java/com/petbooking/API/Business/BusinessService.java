package com.petbooking.API.Business;

import android.util.Log;

import com.petbooking.API.APIClient;
import com.petbooking.API.Business.BusinessInterface;
import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Utils.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Luciano José on 07/05/2017.
 */

public class BusinessService {


    private BusinessInterface mBusinessInterface;

    public BusinessService() {
        mBusinessInterface = APIClient.getClient().create(BusinessInterface.class);
    }

    public void listBusiness(String coords, int page, final APICallback callback) {
        Call<BusinessesResp> call = mBusinessInterface.listBusiness(coords, page);
        call.enqueue(new Callback<BusinessesResp>() {
            @Override
            public void onResponse(Call<BusinessesResp> call, Response<BusinessesResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<BusinessesResp> call, Throwable t) {

            }
        });
    }

    public void listHighlightBusiness(final APICallback callback) {
        Call<BusinessesResp> call = mBusinessInterface.listHighlightBusiness();
        call.enqueue(new Callback<BusinessesResp>() {
            @Override
            public void onResponse(Call<BusinessesResp> call, Response<BusinessesResp> response) {
                Log.d("AQUI", "AQUI");
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<BusinessesResp> call, Throwable t) {

            }
        });
    }


}
