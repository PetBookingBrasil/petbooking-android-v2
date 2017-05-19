package com.petbooking.API.Business;

import com.petbooking.API.APIClient;
import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.API.Business.Models.FavoriteResp;
import com.petbooking.API.Business.Models.FavoriteRqt;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Business;
import com.petbooking.Utils.APIUtils;

import java.util.ArrayList;

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
                if (response.isSuccessful()) {
                    BusinessesResp responseList = response.body();
                    ArrayList<Business> businessList = new ArrayList<>();
                    for (BusinessesResp.Item item : responseList.data) {
                        businessList.add(APIUtils.parseBusiness(item.id, item.attributes));
                    }

                    callback.onSuccess(businessList);
                } else {
                    callback.onError(APIUtils.handleError(response));
                }
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
                if (response.isSuccessful()) {
                    BusinessesResp responseList = response.body();
                    ArrayList<Business> businessList = new ArrayList<>();
                    for (BusinessesResp.Item item : responseList.data) {
                        businessList.add(APIUtils.parseBusiness(item.id, item.attributes));
                    }

                    callback.onSuccess(businessList);
                } else {
                    callback.onError(APIUtils.handleError(response));
                }
            }

            @Override
            public void onFailure(Call<BusinessesResp> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void createFavorite(String userId, String businessId, final APICallback callback) {
        FavoriteRqt favoriteRqt = new FavoriteRqt(APIConstants.DATA_FAVORITES_TYPE_BUSINESS, businessId);
        Call<FavoriteResp> call = mBusinessInterface.createFavorite(userId, favoriteRqt);
        call.enqueue(new Callback<FavoriteResp>() {
            @Override
            public void onResponse(Call<FavoriteResp> call, Response<FavoriteResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<FavoriteResp> call, Throwable t) {

            }
        });
    }

    public void deleteFavorite(String favoriteId, final APICallback callback) {
        Call<FavoriteResp> call = mBusinessInterface.deleteFavorite(favoriteId);
        call.enqueue(new Callback<FavoriteResp>() {
            @Override
            public void onResponse(Call<FavoriteResp> call, Response<FavoriteResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<FavoriteResp> call, Throwable t) {

            }
        });
    }
}
