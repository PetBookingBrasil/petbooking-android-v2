package com.petbooking.Utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.petbooking.API.Appointment.Models.CartRqt;
import com.petbooking.API.Appointment.Models.ServiceResp;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.BusinessesRspAttributes;
import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.API.Business.Models.ReviewResp;
import com.petbooking.API.Business.Models.ReviewableResp;
import com.petbooking.API.Generic.APIError;
import com.petbooking.API.Generic.ErrorResp;
import com.petbooking.API.User.Models.ScheduleResp;
import com.petbooking.BuildConfig;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Business;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.CartItem;
import com.petbooking.Models.Category;
import com.petbooking.Models.Review;
import com.petbooking.Models.ReviewServices;
import com.petbooking.Models.User;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Luciano José on 09/04/2017.
 */

public class APIUtils {

    /**
     * Create User from
     * AuthUserResponse
     *
     * @param resp
     * @return
     */
    public static User parseUser(AuthUserResp resp) {
        AuthUserResp.Attributes attr = resp.data.attributes;
        User user = new User(resp.data.id, attr.authToken, attr.name, attr.birthday, attr.phone, attr.phoneActivated,
                attr.phoneCodeCreatedAt, attr.email, attr.futureEventsCount, attr.acceptsSms, attr.zipcode,
                attr.street, attr.neighborhood, attr.streetNumber, attr.complement, attr.city, attr.state, attr.nickname,
                attr.gender, attr.cpf, attr.searchRange, attr.acceptsEmail, attr.acceptsNotifications,
                attr.acceptsTerms, attr.validForScheduling, attr.avatar);

        return user;
    }

    /**
     * Create a business from
     * BusinessResponse
     *
     * @param id
     * @param attr
     * @return
     */
    public static Business parseBusiness(String id, BusinessesRspAttributes attr) {
        Double latitude = null;
        Double longitude = null;
        String favoritedId = null;

        if (attr.location != null && attr.location.size() == 2) {
            latitude = Double.parseDouble(attr.location.get(0));
            longitude = Double.parseDouble(attr.location.get(1));
        }

        if (attr.favorited && attr.userFavorite != null) {
            favoritedId = attr.userFavorite.id;
        }

        Business business = new Business(id, attr.name, attr.description, attr.city, attr.state,
                attr.street, attr.neighborhood, attr.streetNumber, attr.zipcode,
                attr.ratingAverage, attr.ratingCount, attr.distance, attr.businesstype,
                latitude, longitude, attr.website, attr.phone, attr.facebook, attr.instagram,
                attr.twitter, attr.googlePlus, attr.snapchat, attr.coverImage, attr.avatar, attr.userFavorite, attr.favorited,
                attr.imported, favoritedId, attr.slug);

        return business;
    }

    /**
     * Create Review from
     * ReviewResp
     *
     * @param id
     * @param attr
     * @return
     */
    public static Review parseReview(String id, ReviewResp.Attributes attr) {
        Review review = new Review(id, attr.userName, attr.comment, attr.rating, attr.avatar);

        return review;
    }

    /**
     * Parse a business service
     *
     * @param event
     * @return
     */
    public static BusinessServices parseService(ScheduleResp.Event event) {
        BusinessServices businessServices = new BusinessServices(event.id, event.service.name, event.startTime,
                event.endTime, event.duration, event.service.description, event.service.price, event.businessName,
                event.professionalName, event.professionalAvatar);

        if (event.service.additionalServices.size() != 0) {
            businessServices.setAdditionalServices(event.service.additionalServices);
        }

        return businessServices;
    }

    /**
     * Parse a Business Services
     *
     * @param service
     * @return
     */
    public static BusinessServices parseBusinessService(ServiceResp.Item service) {
        BusinessServices businessService = new BusinessServices(service.id,
                service.attributes.name,
                service.attributes.duration,
                service.attributes.description,
                service.attributes.priceRange.service_price, service.attributes.priceRange.max_service_price
                , service.attributes.priceRange.min_service_price);
        return businessService;
    }

    public static ReviewServices parseReviewServices(ReviewableResp.Item service, ReviewableResp reviewableResp) {
        String bussinesName = "";
        String professionalAvatar = "";
        String categoryName = "";
        for (int i = 0; i < reviewableResp.included.size(); i++) {
            if (reviewableResp.included.get(i).type.equals("employments")) {
                professionalAvatar = reviewableResp.included.get(i).attributes.avatar.url;
                Log.i("Teste", "Qual o professional avatar " + professionalAvatar);
                continue;
            }
            if (reviewableResp.included.get(i).type.equals("service_categories")) {
                categoryName = reviewableResp.included.get(i).attributes.name;
            }
            if (reviewableResp.included.get(i).type.equals("businesses")) {
                bussinesName = reviewableResp.included.get(i).attributes.name;
                Log.i("Teste", "Qual o bussines name " + bussinesName);
                continue;
            }
        }
        ReviewServices reviewServices = new ReviewServices(service.attributes.pet_id, categoryName,
                bussinesName, service.attributes.professional_name, professionalAvatar, service.attributes.date, service.id);
        return reviewServices;
    }


    /**
     * Parse category
     *
     * @param context
     * @param item
     * @return
     */
    public static Category parseCategory(Context context, CategoryResp.Item item) {
        Category category;
        if (item.attributes.coverImage != null) {
            category = new Category(item.id, AppUtils.getCategoryText(item.attributes.name),
                    item.attributes.name, AppUtils.getBusinessIcon(context, item.attributes.name),
                    item.attributes.coverImage.listing.url);
        } else if(item.attributes.categoryIcon !=null){
            category = new Category(item.id, AppUtils.getCategoryText(item.attributes.name),
                    item.attributes.name, AppUtils.getBusinessIcon(context, item.attributes.name),
                    item.attributes.categoryIcon.icon.url);
        }else{
            category = new Category(item.id, AppUtils.getCategoryText(item.attributes.name),
                    item.attributes.name, AppUtils.getBusinessIcon(context, item.attributes.name));
        }

        return category;
    }

    public static ArrayList<CartRqt.Item> getCartReqItems(ArrayList<CartItem> cartItens) {
        ArrayList<CartRqt.Item> items = new ArrayList<>();
        CartRqt.Item item = null;

        for (CartItem cartItem : cartItens) {
            ArrayList<String> additionals = new ArrayList<>();

            if (cartItem.additionalServices.size() != 0) {
                for (BusinessServices additional : cartItem.additionalServices) {
                    additionals.add(additional.id);
                }
            }

            item = new CartRqt.Item(cartItem.startDate, cartItem.startTime, cartItem.businessId,
                    cartItem.service.id, cartItem.professional.id, cartItem.pet.id, cartItem.notes,
                    false, additionals);
            items.add(item);
        }

        return items;
    }

    /**
     * Handle API Response
     *
     * @param response
     * @param callback
     */
    public static void handleResponse(Response response, APICallback callback) {
        if (response.isSuccessful()) {
            callback.onSuccess(response.body());
        } else {
            callback.onError(handleError(response));
        }
    }

    /**
     * Handle Error status
     *
     * @param response
     * @return
     */
    public static Object handleError(Response response) {
        ResponseBody errorBody = response.errorBody();
        String error = null;
        try {
            error = errorBody != null ? new String(errorBody.bytes(), Charset.defaultCharset()) : response.message();
        } catch (IOException e) {
            e.printStackTrace();
        }
        APIError apiError = getFirstError(new Gson().fromJson(error, ErrorResp.class));
        return apiError;
    }

    /**
     * Get Error message
     *
     * @param errorRsp
     */
    public static APIError getFirstError(ErrorResp errorRsp) {
        if (errorRsp.errors != null && errorRsp.errors.size() > 0) {
            ErrorResp.Error firstError = errorRsp.errors.get(0);
            APIError error = new APIError(firstError.title, firstError.detail.toString(), firstError.code, firstError.status);
            return error;
        }

        return null;
    }

    /**
     * Get Asset Endpoint
     *
     * @param assetPath
     * @return
     */
    public static String getAssetEndpoint(String assetPath) {
        String endpoint = assetPath + "?" + new Date().getTime();
        return endpoint;
    }
}
