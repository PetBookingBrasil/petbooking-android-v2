package com.petbooking.UI.Dashboard.BusinessList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Models.Business;
import com.petbooking.R;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.BusinessViewHolder> {

    private ArrayList<Business> mBusinessList;
    private Context mContext;

    public BusinessListAdapter(Context context, ArrayList<Business> businessList) {
        this.mBusinessList = businessList;
        this.mContext = context;
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
    public void onBindViewHolder(BusinessViewHolder holder, int position) {

        final Business business = mBusinessList.get(position);

        int categoryColor = AppUtils.getCategoryColor(mContext, business.businesstype);
        Drawable categoryIcon = AppUtils.getBusinessIcon(mContext, business.businesstype);
        GradientDrawable mDistanceBackground = (GradientDrawable) holder.mTvDistance.getBackground();
        String street = mContext.getResources().getString(R.string.business_street, business.street, business.streetNumber);
        String address = mContext.getResources().getString(R.string.business_address, business.neighborhood, business.city,
                business.state, CommonUtils.formatZipcode(business.zipcode));
        String ratingCount = mContext.getResources().getString(R.string.business_rating_count, business.ratingCount);
        String distance = mContext.getResources().getString(R.string.business_distance, String.format("%.2f", business.distance));

        holder.mTvRate.setText(String.valueOf(business.ratingAverage));
        holder.mRbBusiness.setRating(business.ratingAverage);
        holder.mTvRatingCount.setText(ratingCount);
        holder.mTvName.setText(business.name);
        holder.mTvStreet.setText(street);
        holder.mTvAddress.setText(address);
        holder.mTvDistance.setText(distance);

        holder.mIvCategoryIcon.setImageDrawable(categoryIcon);
        mDistanceBackground.setColor(categoryColor);

        Glide.with(mContext)
                .load("http://www.hospitalamigobicho.com.br/images/slide/slide-petshop.jpg")
                .error(R.drawable.business_background)
                .placeholder(R.drawable.business_background)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mIvBusinessPhoto);
    }

    @Override
    public int getItemCount() {
        return mBusinessList.size();
    }

    public class BusinessViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mClBusiness;
        ImageView mIvBusinessPhoto;
        ImageView mIvCategoryIcon;
        TextView mTvName;
        TextView mTvStreet;
        TextView mTvAddress;
        TextView mTvRate;
        TextView mTvRatingCount;
        TextView mTvDistance;
        RatingBar mRbBusiness;

        public BusinessViewHolder(View view) {
            super(view);

            mClBusiness = (ConstraintLayout) view.findViewById(R.id.business_item);
            mIvBusinessPhoto = (ImageView) view.findViewById(R.id.business_photo);
            mIvCategoryIcon = (ImageView) view.findViewById(R.id.category_icon);
            mTvName = (TextView) view.findViewById(R.id.business_name);
            mTvStreet = (TextView) view.findViewById(R.id.business_street);
            mTvAddress = (TextView) view.findViewById(R.id.business_address);
            mTvRatingCount = (TextView) view.findViewById(R.id.ratings);
            mTvRate = (TextView) view.findViewById(R.id.business_rate);
            mTvDistance = (TextView) view.findViewById(R.id.business_distance);
            mRbBusiness = (RatingBar) view.findViewById(R.id.average_rate);
        }
    }

}
