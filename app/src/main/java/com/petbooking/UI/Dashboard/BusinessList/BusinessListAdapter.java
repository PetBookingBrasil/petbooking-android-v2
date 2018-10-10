package com.petbooking.UI.Dashboard.BusinessList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.FavoriteResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.Models.Category;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.BusinessActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class BusinessListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BUSINESS_ACTIVE = 0;
    private static final int BUSINESS_IMPORTED = 1;

    private BusinessService mBusinessService;
    private ArrayList<Business> mBusinessList;
    private Context mContext;
    private String userId;
    private RequestManager mGlide;
    private boolean isFavoriteList;
    private OnFavoriteAction onFavoriteAction;
    private Category category;

    public BusinessListAdapter(Context context, ArrayList<Business> businessList, RequestManager glide) {
        this.mBusinessList = businessList;
        this.mContext = context;
        this.mBusinessService = new BusinessService();
        this.userId = SessionManager.getInstance().getUserLogged().id;
        this.mGlide = glide;
    }

    public void updateList(ArrayList<Business> businessList) {
        this.mBusinessList = businessList;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        RecyclerView.ViewHolder holder = null;

        if (viewType == BUSINESS_ACTIVE) {
            view = inflater.inflate(R.layout.item_list_business, parent, false);
            holder = new BusinessViewHolder(view);
        } else if (viewType == BUSINESS_IMPORTED) {
            view = inflater.inflate(R.layout.item_list_business_imported, parent, false);
            holder = new BusinessImportedViewHolder(view);
        }


        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case BUSINESS_ACTIVE:
                BusinessViewHolder businessViewHolder = (BusinessViewHolder) holder;
                updateBusiness(businessViewHolder, position);
                break;
            case BUSINESS_IMPORTED:
                BusinessImportedViewHolder importedViewHolder = (BusinessImportedViewHolder) holder;
                updateBusinessImported(importedViewHolder, position);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mBusinessList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mBusinessList.get(position).imported) {
            return BUSINESS_IMPORTED;
        } else {
            return BUSINESS_ACTIVE;
        }
    }

    /**
     * Update Business Information
     *
     * @param holder
     * @param position
     */
    public void updateBusiness(final BusinessViewHolder holder, final int position) {

        final Business business = mBusinessList.get(position);

        String street = mContext.getResources().getString(R.string.business_street, business.street, business.streetNumber, business.neighborhood);
        String city = mContext.getResources().getString(R.string.business_city, business.city, business.state);
        String distance = mContext.getResources().getString(R.string.business_distance, String.format("%.2f", business.distance));
        String ratingCount = mContext.getResources().getQuantityString(R.plurals.business_rating_count, business.ratingCount, business.ratingCount);
        String average = String.format("%.1f", business.ratingAverage);

        if (business.favorited) {
            holder.mBtnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.mBtnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }

        if (business.ratingCount == 0) {
            holder.mTvRate.setVisibility(View.GONE);
            holder.mTvRatingCount.setVisibility(View.GONE);
            holder.mIvRatingStar.setVisibility(View.GONE);
        } else {
            holder.mTvRate.setVisibility(View.VISIBLE);
            holder.mTvRatingCount.setVisibility(View.VISIBLE);
            holder.mIvRatingStar.setVisibility(View.VISIBLE);
            holder.mTvRate.setText(average);
            holder.mTvRatingCount.setText(ratingCount);
        }

        holder.mTvName.setText(business.name);
        holder.mTvStreet.setText(street);
        holder.mTvCity.setText(city);
        holder.mTvDistance.setText(distance);

        holder.mClBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBusiness(business.id, business.name, business.distance);
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

                            if (isFavoriteList) {
                                mBusinessList.remove(position);
                                onFavoriteAction.onDelete(position);
                                notifyDataSetChanged();
                            }
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

        mGlide.load(business.image.url)
                .error(R.drawable.business_background)
                .placeholder(R.drawable.business_background)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .into(holder.mIvBusinessPhoto);
    }

    /**
     * Update Business Imported
     *
     * @param holder
     * @param position
     */
    public void updateBusinessImported(final BusinessImportedViewHolder holder, final int position) {

        final Business business = mBusinessList.get(position);

        String distance = mContext.getResources().getString(R.string.business_distance, String.format("%.2f", business.distance));
        String street = mContext.getResources().getString(R.string.business_street, business.street, business.streetNumber, business.neighborhood);

        holder.mTvName.setText(business.name);
        holder.mTvStreet.setText(street);
        holder.mTvDistance.setText(distance);

        if (business.favorited) {
            holder.mBtnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.mBtnFavorite.setImageResource(R.drawable.ic_favorite_border_black);
        }

        holder.mBtnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (business.favorited) {
                    mBusinessService.deleteFavorite(business.favoritedId, new APICallback() {
                        @Override
                        public void onSuccess(Object response) {
                            mBusinessList.get(position).setFavorited(false);
                            mBusinessList.get(position).setFavoritedId("");
                            holder.mBtnFavorite.setImageResource(R.drawable.ic_favorite_border_black);
                            if (isFavoriteList) {
                                mBusinessList.remove(position);
                                onFavoriteAction.onDelete(position);
                                notifyDataSetChanged();
                            }
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

        holder.mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBusinessService.callForBussinesSlug(business.slug);
                callToBusiness(business.phone);

            }
        });
    }

    /**
     * Go to Business
     */
    public void goToBusiness(String businessId, String businessName, float businessDistance) {
        Intent businessIntent = new Intent(mContext, BusinessActivity.class);
        businessIntent.putExtra("businessId", businessId);
        businessIntent.putExtra("businessName", businessName);
        businessIntent.putExtra("businessDistance", businessDistance);
        businessIntent.putExtra("category", category);
        mContext.startActivity(businessIntent);
    }

    /**
     * Call to imported business
     *
     * @param number
     */
    public void callToBusiness(String number) {
        Uri data = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(data);
        mContext.startActivity(intent);
    }

    public void setFavoriteList(boolean favoriteList) {
        isFavoriteList = favoriteList;
    }

    public void setOnFavoriteAction(OnFavoriteAction onFavoriteAction) {
        this.onFavoriteAction = onFavoriteAction;
    }

    public class BusinessImportedViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mClBusiness;
        ImageButton mBtnFavorite;
        Button mBtnCall;
        TextView mTvName;
        TextView mTvStreet;
        TextView mTvDistance;

        public BusinessImportedViewHolder(View view) {
            super(view);

            mClBusiness = (LinearLayout) view.findViewById(R.id.business_item);
            mBtnFavorite = (ImageButton) view.findViewById(R.id.favorite_button);
            mTvName = (TextView) view.findViewById(R.id.business_name);
            mTvStreet = (TextView) view.findViewById(R.id.business_street);
            mTvDistance = (TextView) view.findViewById(R.id.business_distance);
            mBtnCall = (Button) view.findViewById(R.id.call_button);
        }
    }

    public class BusinessViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mClBusiness;
        ImageView mIvBusinessPhoto;
        ImageView mIvRatingStar;
        ImageButton mBtnFavorite;
        TextView mTvName;
        TextView mTvStreet;
        TextView mTvCity;
        TextView mTvRate;
        TextView mTvRatingCount;
        TextView mTvDistance;

        public BusinessViewHolder(View view) {
            super(view);

            mClBusiness = (ConstraintLayout) view.findViewById(R.id.business_item);
            mIvBusinessPhoto = (ImageView) view.findViewById(R.id.business_photo);
            mIvRatingStar = (ImageView) view.findViewById(R.id.rating_star);
            mBtnFavorite = (ImageButton) view.findViewById(R.id.favorite_button);
            mTvName = (TextView) view.findViewById(R.id.business_name);
            mTvStreet = (TextView) view.findViewById(R.id.business_street);
            mTvCity = (TextView) view.findViewById(R.id.business_city);
            mTvRatingCount = (TextView) view.findViewById(R.id.ratings);
            mTvRate = (TextView) view.findViewById(R.id.business_rate);
            mTvDistance = (TextView) view.findViewById(R.id.business_distance);
        }
    }

    public interface OnFavoriteAction {
        void onDelete(int position);
    }
}
