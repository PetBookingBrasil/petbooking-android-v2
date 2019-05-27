package com.petbooking.API.Business;

import android.util.Log;

import com.petbooking.API.APIClient;
import com.petbooking.API.Appointment.Models.ServiceResp;
import com.petbooking.API.Business.Models.BannerResponse;
import com.petbooking.API.Business.Models.BusinessResp;
import com.petbooking.API.Business.Models.BusinessesResp;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.API.Business.Models.FavoriteResp;
import com.petbooking.API.Business.Models.FavoriteRqt;
import com.petbooking.API.Business.Models.ReviewResp;
import com.petbooking.API.Business.Models.ReviewableResp;
import com.petbooking.API.Review.ReviewRequest;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Business;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Category;
import com.petbooking.Models.Review;
import com.petbooking.Models.ReviewServices;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.Date;

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

    public void listPromos(String promoId, String userId, int page, int limit, final APICallback callback) {
        Call<BusinessesResp> call = mBusinessInterface.listPromos(promoId);
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

    public void listBussinesByCategory(String coords, String userId, int page, int limit, String categoryId, final APICallback callback) {
        Call<BusinessesResp> call = mBusinessInterface.listBusinessByCategory(coords, userId, page, limit, categoryId);
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


    public void getReviews(String userId, final APICallback callback) {
       String date = CommonUtils.formatDate(CommonUtils.DATEFORMATDEFAULT,new Date());
        Call<ReviewableResp> call = mBusinessInterface.getReviews(userId);
        call.enqueue(new Callback<ReviewableResp>() {
            @Override
            public void onResponse(Call<ReviewableResp> call, Response<ReviewableResp> response) {
                if (response.isSuccessful()) {
                    if (response.body().data.size() > 0){
                        callback.onError(null);
                    }
                    Log.i(getClass().getSimpleName(),"Qual o review success" + response.body().data.size());
                    ArrayList<ReviewServices> services = new ArrayList<ReviewServices>();
                    ReviewableResp serviceResp = response.body();
                    for (ReviewableResp.Item item : serviceResp.data) {
                        ReviewServices service = APIUtils.parseReviewServices(item,serviceResp);

                        services.add(service);
                    }

                    callback.onSuccess(services);
                } else {
                    callback.onError(APIUtils.handleError(response));
                }
            }

            @Override
            public void onFailure(Call<ReviewableResp> call, Throwable t) {

            }
        });
    }

    public void sendReviews(ReviewRequest request, String userId, final APICallback callback) {

        Call<Void> call = mBusinessInterface.sendReviews(userId,request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(APIUtils.handleError(null));
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {

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

    public void listBanners(final APICallback callback) {
        Call<BannerResponse> call = mBusinessInterface.listBanners();
        call.enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
                APIUtils.handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
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

    public void callForBussinesSlug(String bussinesSlug){
        Call<Void> call = mBusinessInterface.listBussinesSlug(bussinesSlug);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(getClass().getSimpleName(),"Call button ok");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
