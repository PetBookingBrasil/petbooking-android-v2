package com.petbooking.UI.Dashboard.BusinessList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.FavoriteResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.BusinessActivity;
import com.petbooking.UI.Widget.StarsRating;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.BusinessViewHolder> {

    private BusinessService mBusinessService;
    private ArrayList<Business> mBusinessList;
    private Context mContext;
    private String userId;

    public BusinessListAdapter(Context context, ArrayList<Business> businessList) {
        this.mBusinessList = businessList;
        this.mContext = context;
        this.mBusinessService = new BusinessService();
        this.userId = SessionManager.getInstance().getUserLogged().id;
    }

    public void updateList(ArrayList<Business> businessList) {
        this.mBusinessList = businessList;
    }

    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_business, parent, false);

        BusinessViewHolder holder = new BusinessViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final BusinessViewHolder holder, final int position) {

        final Business business = mBusinessList.get(position);

        int categoryColor = AppUtils.getCategoryColor(mContext, business.businesstype);
        GradientDrawable mDistanceBackground = (GradientDrawable) holder.mTvDistance.getBackground();
        String street = mContext.getResources().getString(R.string.business_street, business.street, business.streetNumber);
        String distance = mContext.getResources().getString(R.string.business_distance, String.format("%.2f", business.distance));
        String ratingCount = mContext.getResources().getString(R.string.business_rating_count, business.ratingCount);
        String average = String.format("%.1f", business.ratingAverage);
        average = average.replace(",", ".");

        if (business.favorited) {
            holder.mBtnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.mBtnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }

        if (business.ratingCount == 0) {
            holder.mTvRate.setVisibility(View.GONE);
            holder.mTvRatingCount.setVisibility(View.GONE);
            holder.mRbBusiness.setVisibility(View.GONE);
        } else {
            holder.mTvRate.setText(average);
            holder.mRbBusiness.setRating(business.ratingAverage);
            holder.mTvRatingCount.setText(ratingCount);
        }

        holder.mTvName.setText(business.name);
        holder.mTvStreet.setText(street);
        holder.mTvDistance.setText(distance);

        mDistanceBackground.setColor(categoryColor);

        holder.mClBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBusiness(business.id);
            }
        });

        holder.mBtnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (business.favorited) {
                    mBusinessService.deleteFavorite(business.favoritedId, new APICallback() {
                        @Override
                        public void onSuccess(Object response) {
                            mBusinessList.get(position).setFavorited(false);
                            mBusinessList.get(position).setFavoritedId("");
                            holder.mBtnFavorite.setImageResource(R.drawable.ic_favorite_border);
                        }

                        @Override
                        public void onError(Object error) {

                        }
                    });
                } else {
                    mBusinessService.createFavorite(userId, business.id, new APICallback() {
                        @Override
                        public void onSuccess(Object response) {
                            FavoriteResp resp = (FavoriteResp) response;

                            mBusinessList.get(position).setFavorited(true);
                            mBusinessList.get(position).setFavoritedId(resp.data.id);
                            holder.mBtnFavorite.setImageResource(R.drawable.ic_favorite_filled);
                        }

                        @Override
                        public void onError(Object error) {

                        }
                    });
                }
            }
        });

        Glide.with(mContext)
                .load(business.image.url)
                .error(R.drawable.business_background)
                .placeholder(R.drawable.business_background)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .into(holder.mIvBusinessPhoto);
    }

    @Override
    public int getItemCount() {
        return mBusinessList.size();
    }

    /**
     * Go to Business
     */
    public void goToBusiness(String businessId) {
        Intent businessIntent = new Intent(mContext, BusinessActivity.class);
        businessIntent.putExtra("businessId", businessId);
        mContext.startActivity(businessIntent);
    }

    public class BusinessViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mClBusiness;
        ImageView mIvBusinessPhoto;
        ImageButton mBtnFavorite;
        TextView mTvName;
        TextView mTvStreet;
        TextView mTvRate;
        TextView mTvRatingCount;
        TextView mTvDistance;
        StarsRating mRbBusiness;

        public BusinessViewHolder(View view) {
            super(view);

            mClBusiness = (ConstraintLayout) view.findViewById(R.id.business_item);
            mIvBusinessPhoto = (ImageView) view.findViewById(R.id.business_photo);
            mBtnFavorite = (ImageButton) view.findViewById(R.id.favorite_button);
            mTvName = (TextView) view.findViewById(R.id.business_name);
            mTvStreet = (TextView) view.findViewById(R.id.business_street);
            mTvRatingCount = (TextView) view.findViewById(R.id.ratings);
            mTvRate = (TextView) view.findViewById(R.id.business_rate);
            mTvDistance = (TextView) view.findViewById(R.id.business_distance);
            mRbBusiness = (StarsRating) view.findViewById(R.id.average_rate);
        }
    }

}
