package com.petbooking.UI.Menu.Favorites;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Models.Business;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.BusinessActivity;
import com.petbooking.UI.Widget.StarsRating;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.FavoriteViewHolder> {

    private ArrayList<Business> mBusinessList;
    private Context mContext;
    private RequestManager mGlide;

    public FavoritesListAdapter(Context context, ArrayList<Business> businessList, RequestManager glide) {
        this.mBusinessList = businessList;
        this.mContext = context;
        this.mGlide = glide;
    }

    public void updateList(ArrayList<Business> businessList) {
        this.mBusinessList = businessList;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_favorites, parent, false);

        FavoriteViewHolder holder = new FavoriteViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final FavoriteViewHolder holder, final int position) {

        final Business business = mBusinessList.get(position);
        String street = mContext.getResources().getString(R.string.business_street, business.street, business.streetNumber);
        String ratingCount = mContext.getResources().getString(R.string.business_rating_count, business.ratingCount);
        String average = String.format("%.1f", business.ratingAverage);
        average = average.replace(",", ".");

        holder.mTvName.setText(business.name);
        holder.mTvStreet.setText(street);
        holder.mTvRate.setText(average);
        holder.mRbBusiness.setRating(business.ratingAverage);
        holder.mTvRatingCount.setText(ratingCount);
        holder.mClBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBusiness(business.id);
            }
        });

        mGlide.load(business.avatar.url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .dontAnimate()
                .into(holder.mIvPhoto);
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

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mClBusiness;
        CircleImageView mIvPhoto;
        TextView mTvName;
        TextView mTvStreet;
        TextView mTvRate;
        TextView mTvRatingCount;
        StarsRating mRbBusiness;

        public FavoriteViewHolder(View view) {
            super(view);

            mIvPhoto = (CircleImageView) view.findViewById(R.id.business_photo);
            mClBusiness = (ConstraintLayout) view.findViewById(R.id.business_item);
            mTvName = (TextView) view.findViewById(R.id.business_name);
            mTvStreet = (TextView) view.findViewById(R.id.business_street);
            mTvRatingCount = (TextView) view.findViewById(R.id.business_rating_count);
            mTvRate = (TextView) view.findViewById(R.id.business_rating_average);
            mRbBusiness = (StarsRating) view.findViewById(R.id.business_rating_stars);
        }
    }

}
