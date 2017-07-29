package com.petbooking.API.Business;

import com.petbooking.API.APIClient;
import com.petbooking.API.Business.Models.BusinessResp;
import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.API.Business.Models.FavoriteResp;
import com.petbooking.API.Business.Models.FavoriteRqt;
import com.petbooking.API.Business.Models.ReviewResp;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Business;
import com.petbooking.Models.Category;
import com.petbooking.Models.Review;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;

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

    public void listBusiness(String coords, String userId, int page, int limit, final APICallback callback) {
        Call<BusinessesResp> call = mBusinessInterface.listBusiness(coords, userId, page, limit);
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

    public void searchBusiness(String coords, String userId, int page, int limit, String categoryId, String text, final APICallback callback) {
        Call<BusinessesResp> call = mBusinessInterface.searchBusiness(coords, userId, page, limit, categoryId, text);
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


    public void listBusinessReviews(String businessId, int page, final APICallback callback) {
        Call<ReviewResp> call = mBusinessInterface.listBusinessReviews(businessId, page);
        call.enqueue(new Callback<ReviewResp>() {
            @Override
            public void onResponse(Call<ReviewResp> call, Response<ReviewResp> response) {
                if (response.isSuccessful()) {
                    ArrayList<Review> reviewList = new ArrayList<Review>();
                    ReviewResp responseList = response.body();
                    for (ReviewResp.Item item : responseList.data) {
                        reviewList.add(APIUtils.parseReview(item.id, item.attributes));
                    }

                    callback.onSuccess(reviewList);
                } else {
                    callback.onError(APIUtils.handleError(response));
                }
            }

            @Override
            public void onFailure(Call<ReviewResp> call, Throwable t) {

            }
        });
    }


    public void getBusiness(String businessId, String userId, final APICallback callback) {
        Call<BusinessResp> call = mBusinessInterface.getBusiness(businessId, userId);
        call.enqueue(new Callback<BusinessResp>() {
            @Override
            public void onResponse(Call<BusinessResp> call, Response<BusinessResp> response) {
                if (response.isSuccessful()) {
                    BusinessResp responseItem = response.body();
                    Business business = APIUtils.parseBusiness(responseItem.data.id, responseItem.data.attributes);

                    callback.onSuccess(business);
                } else {
                    callback.onError(APIUtils.handleError(response));
                }
            }

            @Override
            public void onFailure(Call<BusinessResp> call, Throwable t) {

            }
        });
    }

    public void listCategories(final APICallback callback) {
        Call<CategoryResp> call = mBusinessInterface.listCategories();
        call.enqueue(new Callback<CategoryResp>() {
            @Override
            public void onResponse(Call<CategoryResp> call, Response<CategoryResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<CategoryResp> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void listBusinessCategories(String businessId, final APICallback callback) {
        Call<CategoryResp> call = mBusinessInterface.listBusinessCategories(businessId);
        call.enqueue(new Callback<CategoryResp>() {
            @Override
            public void onResponse(Call<CategoryResp> call, Response<CategoryResp> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<CategoryResp> call, Throwable t) {
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
