package com.petbooking.UI.Dashboard.Business.BusinessInformation;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.Models.Review;
import com.petbooking.R;
import com.petbooking.UI.Widget.StarsRating;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {

    private ArrayList<Review> mReviewList;
    private Context mContext;

    public ReviewListAdapter(Context context, ArrayList<Review> reviewList) {
        this.mReviewList = reviewList;
        this.mContext = context;
    }

    public void updateList(ArrayList<Review> reviewList) {
        this.mReviewList = reviewList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_review, parent, false);

        ReviewViewHolder holder = new ReviewViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

        final Review review = mReviewList.get(position);

        holder.mTvName.setText(review.userName);
        holder.mTvComment.setText(review.comment);
        holder.mRbBusiness.setRating(review.rating);

        if (TextUtils.isEmpty(review.comment)) {
            holder.mTvComment.setVisibility(View.GONE);
        }

        Glide.with(mContext)
                .load(review.userAvatar.url)
                .error(R.drawable.ic_placeholder_user)
                .into(holder.mIvUserPhoto);
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvUserPhoto;
        TextView mTvName;
        TextView mTvComment;
        StarsRating mRbBusiness;

        public ReviewViewHolder(View view) {
            super(view);

            mIvUserPhoto = (ImageView) view.findViewById(R.id.user_image);
            mTvName = (TextView) view.findViewById(R.id.user_name);
            mTvComment = (TextView) view.findViewById(R.id.comment);
            mRbBusiness = (StarsRating) view.findViewById(R.id.rating);
        }
    }

}
