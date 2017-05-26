package com.petbooking.UI.Dashboard.Business.BusinessInformation;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.MapView;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.FavoriteResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
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
    private AlertDialog mLoadingDialog;

    /**
     * Business Info
     */
    private ImageView mIvBusinessPhoto;
    private ImageButton mIBtnFavorite;
    private TextView mTvName;
    private TextView mTvDescription;
    private TextView mTvPhone;
    private TextView mTvWebsite;
    private MapView mMapView;

    /**
     * Labels and Separators
     */
    private TextView mTvDescriptionLabel;
    private TextView mTvContactLabel;
    private TextView mTvWebsiteLabel;
    private TextView mTvSocialNetworkLabel;
    private View mSeparatorDescription;
    private View mSeparatorContact;
    private View mSeparatorWebsite;
    private View mSeparatorSocial;
    private View mSeparatorReview;
    private LinearLayout mContactLayout;
    private RelativeLayout mReviewLayout;

    /**
     * Social Icons
     */
    private ImageView mIvFacebook;
    private ImageView mIvTwitter;
    private ImageView mIvInstagram;
    private ImageView mIvGooglePlus;

    /**
     * Business Rating and Review
     */
    private ImageButton mBtnShowReviews;
    private StarsRating mRbBusiness;
    private TextView mTvRatingAverage;
    private TextView mTvRatingCount;

    /**
     * Review List
     */
    private ArrayList<Review> mReviewList;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRvReviews;
    private ReviewListAdapter mAdapter;


    View.OnClickListener favoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (business.favorited) {
                mBusinessService.deleteFavorite(business.favoritedId, new APICallback() {
                    @Override
                    public void onSuccess(Object response) {
                        business.setFavorited(false);
                        business.setFavoritedId("");
                        mIBtnFavorite.setImageResource(R.drawable.ic_favorite_border);
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });
            } else {
                mBusinessService.createFavorite(SessionManager.getInstance().getUserLogged().id,
                        business.id, new APICallback() {
                            @Override
                            public void onSuccess(Object response) {
                                FavoriteResp resp = (FavoriteResp) response;

                                business.setFavorited(true);
                                business.setFavoritedId(resp.data.id);
                                mIBtnFavorite.setImageResource(R.drawable.ic_favorite_filled);
                            }

                            @Override
                            public void onError(Object error) {

                            }
                        });
            }
        }
    };

    View.OnClickListener socialListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == mIvFacebook.getId()) {
                openPage(business.facebook);
            } else if (id == mIvTwitter.getId()) {
                openPage(business.twitter);
            } else if (id == mIvInstagram.getId()) {
                openPage(business.instagram);
            } else if (id == mIvGooglePlus.getId()) {
                openPage(business.googlePlus);
            } else if (id == mTvWebsite.getId()) {
                openPage(business.website);
            }

        }
    };

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

    View.OnClickListener reviewButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("SHOW", "REVIEWS");
        }
    };

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
        mIBtnFavorite = (ImageButton) view.findViewById(R.id.favorite_button);
        mTvName = (TextView) view.findViewById(R.id.business_name);
        mTvDescription = (TextView) view.findViewById(R.id.business_description);
        mTvPhone = (TextView) view.findViewById(R.id.business_phone);
        mTvWebsite = (TextView) view.findViewById(R.id.business_website);

        mTvDescriptionLabel = (TextView) view.findViewById(R.id.description_label);
        mTvContactLabel = (TextView) view.findViewById(R.id.contact_label);
        mTvWebsiteLabel = (TextView) view.findViewById(R.id.website_label);
        mTvSocialNetworkLabel = (TextView) view.findViewById(R.id.social_network_label);
        mSeparatorDescription = view.findViewById(R.id.separatorDescription);
        mSeparatorContact = view.findViewById(R.id.separatorContact);
        mSeparatorWebsite = view.findViewById(R.id.separatorWebsite);
        mSeparatorSocial = view.findViewById(R.id.separatorSocialNetwork);
        mSeparatorReview = view.findViewById(R.id.separatorReview);
        mContactLayout = (LinearLayout) view.findViewById(R.id.contactLayout);
        mReviewLayout = (RelativeLayout) view.findViewById(R.id.reviewLayout);

        mIvFacebook = (ImageView) view.findViewById(R.id.social_facebook);
        mIvTwitter = (ImageView) view.findViewById(R.id.social_twitter);
        mIvGooglePlus = (ImageView) view.findViewById(R.id.social_googleplus);
        mIvInstagram = (ImageView) view.findViewById(R.id.social_instagram);

        mBtnShowReviews = (ImageButton) view.findViewById(R.id.reviewButton);
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
        mRvReviews.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        if (mAdapter != null) {
            mRvReviews.setAdapter(mAdapter);
        }

        mBtnShowReviews.setOnClickListener(reviewButtonListener);

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
        showLoading();
        mBusinessService.getBusiness(id, SessionManager.getInstance().getUserLogged().id, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                business = (Business) response;
                updateBusinessInfo(business);
                hideLoading();
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
    public void updateBusinessInfo(final Business business) {
        boolean hasIcon = false;
        String ratingCount = mContext.getResources().getString(R.string.business_rating_count, business.ratingCount);
        String average = String.format("%.1f", business.ratingAverage);
        average = average.replace(",", ".");

        mTvName.setText(business.name);
        mTvDescription.setText(business.description);
        mTvPhone.setText(CommonUtils.formatPhone(business.phone));
        mTvWebsite.setText(business.website);
        mRbBusiness.setRating(business.ratingAverage);
        mTvRatingAverage.setText(average);
        mTvRatingCount.setText(ratingCount);
        hasIcon = checkSocialIcon(business.facebook, business.twitter, business.instagram, business.googlePlus);

        if (business.favorited) {
            mIBtnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            mIBtnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }

        if (TextUtils.isEmpty(business.description)) {
            mTvDescription.setVisibility(View.GONE);
            mTvDescriptionLabel.setVisibility(View.GONE);
            mSeparatorDescription.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(business.phone)) {
            mTvContactLabel.setVisibility(View.GONE);
            mContactLayout.setVisibility(View.GONE);
            mSeparatorContact.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(business.website) || TextUtils.equals("http://", business.website)) {
            mTvWebsiteLabel.setVisibility(View.GONE);
            mTvWebsite.setVisibility(View.GONE);
            mSeparatorWebsite.setVisibility(View.GONE);
        }

        if (business.ratingCount == 0) {
            mReviewLayout.setVisibility(View.GONE);
            mSeparatorReview.setVisibility(View.GONE);
        }

        if (!hasIcon) {
            mTvSocialNetworkLabel.setVisibility(View.GONE);
            mSeparatorSocial.setVisibility(View.GONE);
        }

        mIBtnFavorite.setOnClickListener(favoriteListener);
        mTvWebsite.setOnClickListener(socialListener);
        Glide.with(mContext).load(business.image.url).error(R.drawable.business_background).into(mIvBusinessPhoto);
    }

    /**
     * Check Social Network icons
     *
     * @param facebook
     * @param twitter
     * @param instagram
     * @param googlePlus
     * @return
     */
    public boolean checkSocialIcon(final String facebook, final String twitter, final String instagram, final String googlePlus) {
        boolean hasIcon = false;

        if (!TextUtils.isEmpty(facebook)) {
            mIvFacebook.setVisibility(View.VISIBLE);
            mIvFacebook.setOnClickListener(socialListener);
            hasIcon = true;
        }

        if (!TextUtils.isEmpty(twitter)) {
            mIvTwitter.setVisibility(View.VISIBLE);
            mIvTwitter.setOnClickListener(socialListener);
            hasIcon = true;
        }

        if (!TextUtils.isEmpty(instagram)) {
            mIvInstagram.setVisibility(View.VISIBLE);
            mIvInstagram.setOnClickListener(socialListener);
            hasIcon = true;
        }

        if (!TextUtils.isEmpty(googlePlus)) {
            mIvGooglePlus.setVisibility(View.VISIBLE);
            mIvGooglePlus.setOnClickListener(socialListener);
            hasIcon = true;
        }

        return hasIcon;
    }

    public void openPage(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void showLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new AlertDialog.Builder(mContext)
                .setView(View.inflate(mContext, R.layout.dialog_loading, null), 0, 0, 0, 0)
                .create();
        mLoadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mLoadingDialog.show();
    }

    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
