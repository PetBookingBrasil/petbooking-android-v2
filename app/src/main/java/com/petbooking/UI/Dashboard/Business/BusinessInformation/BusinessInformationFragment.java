package com.petbooking.UI.Dashboard.Business.BusinessInformation;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.MapView;
import com.google.gson.Gson;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Models.Business;
import com.petbooking.Models.Review;
import com.petbooking.R;
import com.petbooking.UI.Widget.StarsRating;
import com.petbooking.Utils.CommonUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessInformationFragment extends Fragment {

    private Context mContext;
    private BusinessService mBusinessService;
    private String businessId;
    private Business business;

    /**
     * Business Info
     */
    private ImageView mIvBusinessPhoto;
    private TextView mTvName;
    private TextView mTvDescription;
    private TextView mTvPhone;
    private TextView mTvEmail;
    private TextView mTvWebsite;
    private MapView mMapView;

    /**
     * Social Icons
     */
    private ImageView mIvFacebook;
    private ImageView mIvTwitter;
    private ImageView mIvInstagram;
    private ImageView mIvGooglePlus;

    /**
     * Business Rating
     */
    private ArrayList<Review> mReviewList;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRvReviews;
    private ReviewListAdapter mAdapter;
    private StarsRating mRbBusiness;
    private TextView mTvRatingAverage;
    private TextView mTvRatingCount;

    public BusinessInformationFragment() {
        // Required empty public constructor
    }

    public static BusinessInformationFragment newInstance(String id) {
        BusinessInformationFragment fragment = new BusinessInformationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("businessId", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBusinessService = new BusinessService();
        this.mContext = getContext();
        this.businessId = getArguments().getString("businessId", "0");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_information, container, false);

        mIvBusinessPhoto = (ImageView) view.findViewById(R.id.business_photo);
        mTvName = (TextView) view.findViewById(R.id.business_name);
        mTvDescription = (TextView) view.findViewById(R.id.business_description);
        mTvPhone = (TextView) view.findViewById(R.id.business_phone);
        mTvEmail = (TextView) view.findViewById(R.id.business_email);
        mTvWebsite = (TextView) view.findViewById(R.id.business_website);

        mIvFacebook = (ImageView) view.findViewById(R.id.social_facebook);
        mIvTwitter = (ImageView) view.findViewById(R.id.social_twitter);
        mIvGooglePlus = (ImageView) view.findViewById(R.id.social_googleplus);
        mIvInstagram = (ImageView) view.findViewById(R.id.social_instagram);

        mTvRatingAverage = (TextView) view.findViewById(R.id.business_rating_average);
        mTvRatingCount = (TextView) view.findViewById(R.id.business_rating_count);
        mRbBusiness = (StarsRating) view.findViewById(R.id.business_rating_stars);


        mReviewList = new ArrayList<>();
        mRvReviews = (RecyclerView) view.findViewById(R.id.reviewsList);
        mAdapter = new ReviewListAdapter(mContext, mReviewList);

        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRvReviews.setHasFixedSize(true);
        mRvReviews.setLayoutManager(mLayoutManager);

        if (mAdapter != null) {
            mRvReviews.setAdapter(mAdapter);
        }

        initReviews();
        getBusinessInfo(businessId);

        return view;
    }

    private void initReviews() {
        for (int i = 0; i < 10; i++) {
            mReviewList.add(new Review());
        }
        mAdapter.updateList(mReviewList);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Get Business Information
     *
     * @param id
     */
    public void getBusinessInfo(String id) {
        mBusinessService.getBusiness(id, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                business = (Business) response;
                updateBusinessInfo(business);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    /**
     * Update Business Info
     *
     * @param business
     */
    public void updateBusinessInfo(Business business) {
        String phone = mContext.getResources().getString(R.string.business_phone, CommonUtils.formatPhone(business.phone));
        String email = mContext.getResources().getString(R.string.business_email, business.website);
        String ratingCount = mContext.getResources().getString(R.string.business_rating_count, business.ratingCount);
        String average = String.format("%.1f", business.ratingAverage);
        average = average.replace(",", ".");

        mTvName.setText(business.name);
        mTvDescription.setText(business.description);
        mTvPhone.setText(phone);
        mTvWebsite.setText(business.website);
        mRbBusiness.setRating(business.ratingAverage);
        mTvRatingAverage.setText(average);
        mTvRatingCount.setText(ratingCount);

        checkSocialIcon(business.facebook, business.twitter, business.instagram, business.googlePlus);

        Glide.with(mContext).load(business.image.url).error(R.drawable.business_background).into(mIvBusinessPhoto);
    }

    /**
     * Check Social Network
     *
     * @param facebook
     * @param twitter
     * @param instagram
     * @param googlePlus
     */
    public void checkSocialIcon(String facebook, String twitter, String instagram, String googlePlus) {
        if (!TextUtils.isEmpty(facebook)) {
            mIvFacebook.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(twitter)) {
            mIvTwitter.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(instagram)) {
            mIvInstagram.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(googlePlus)) {
            mIvGooglePlus.setVisibility(View.VISIBLE);
        }
    }
}
