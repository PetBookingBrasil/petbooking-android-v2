package com.petbooking.UI.Dashboard.Business.BusinessInformation;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.Business.Models.FavoriteResp;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.Business;
import com.petbooking.Models.Review;
import com.petbooking.R;
import com.petbooking.UI.Widget.StarsRating;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessInformationFragment extends Fragment implements OnMapReadyCallback {

    private Context mContext;
    private BusinessService mBusinessService;
    private String businessId;
    private Business mBusiness;
    private AlertDialog mLoadingDialog;
    private ScrollView mSvInfo;
    private boolean isShown = false;

    /**
     * Business Info
     */
    private ImageView mIvBusinessPhoto;
    private ImageButton mIBtnFavorite;
    private TextView mTvName;
    private TextView mTvDescription;
    private TextView mTvPhone;
    private TextView mTvWebsite;
    private TextView mTvStreet;
    private TextView mTvDistance;

    /**
     * Map
     */
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private GoogleMap mGMap = null;
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
    private View mSeparatorLocation;
    private LinearLayout mContactLayout;
    private LinearLayout mLocationLayout;
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
            if (mBusiness.favorited) {
                mBusinessService.deleteFavorite(mBusiness.favoritedId, new APICallback() {
                    @Override
                    public void onSuccess(Object response) {
                        mBusiness.setFavorited(false);
                        mBusiness.setFavoritedId("");
                        mIBtnFavorite.setImageResource(R.drawable.ic_favorite_border);
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });
            } else {
                mBusinessService.createFavorite(SessionManager.getInstance().getUserLogged().id,
                        mBusiness.id, new APICallback() {
                            @Override
                            public void onSuccess(Object response) {
                                FavoriteResp resp = (FavoriteResp) response;

                                mBusiness.setFavorited(true);
                                mBusiness.setFavoritedId(resp.data.id);
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
                openPage(mBusiness.facebook);
            } else if (id == mIvTwitter.getId()) {
                openPage(mBusiness.twitter);
            } else if (id == mIvInstagram.getId()) {
                openPage(mBusiness.instagram);
            } else if (id == mIvGooglePlus.getId()) {
                openPage(mBusiness.googlePlus);
            } else if (id == mTvWebsite.getId()) {
                openPage(mBusiness.website);
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
            if (isShown) {
                mRvReviews.setVisibility(View.GONE);
                mBtnShowReviews.setImageResource(R.drawable.ic_arrow_down);
            } else {
                mRvReviews.setVisibility(View.VISIBLE);
                mBtnShowReviews.setImageResource(R.drawable.ic_arrow_up);
                mSvInfo.post(new Runnable() {

                    @Override
                    public void run() {
                        mSvInfo.smoothScrollTo(mSvInfo.getScrollX(), mRvReviews.getTop());
                    }
                });
            }

            isShown = !isShown;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBusinessService = new BusinessService();
        this.mContext = getContext();
        this.businessId = getArguments().getString("businessId", "0");
        getBusinessInfo(businessId);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_information, container, false);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mSvInfo = (ScrollView) view.findViewById(R.id.infoView);
        mIvBusinessPhoto = (ImageView) view.findViewById(R.id.business_photo);
        mIBtnFavorite = (ImageButton) view.findViewById(R.id.favorite_button);
        mTvName = (TextView) view.findViewById(R.id.business_name);
        mTvDescription = (TextView) view.findViewById(R.id.business_description);
        mTvPhone = (TextView) view.findViewById(R.id.business_phone);
        mTvWebsite = (TextView) view.findViewById(R.id.business_website);
        mTvStreet = (TextView) view.findViewById(R.id.business_street);
        mTvDistance = (TextView) view.findViewById(R.id.business_distance);
        mMapView = (MapView) view.findViewById(R.id.business_location);

        mTvDescriptionLabel = (TextView) view.findViewById(R.id.description_label);
        mTvContactLabel = (TextView) view.findViewById(R.id.contact_label);
        mTvWebsiteLabel = (TextView) view.findViewById(R.id.website_label);
        mTvSocialNetworkLabel = (TextView) view.findViewById(R.id.social_network_label);
        mSeparatorDescription = view.findViewById(R.id.separatorDescription);
        mSeparatorContact = view.findViewById(R.id.separatorContact);
        mSeparatorWebsite = view.findViewById(R.id.separatorWebsite);
        mSeparatorSocial = view.findViewById(R.id.separatorSocialNetwork);
        mSeparatorReview = view.findViewById(R.id.separatorReview);
        mSeparatorLocation = view.findViewById(R.id.separatorLocation);
        mContactLayout = (LinearLayout) view.findViewById(R.id.contactLayout);
        mReviewLayout = (RelativeLayout) view.findViewById(R.id.reviewLayout);
        mLocationLayout = (LinearLayout) view.findViewById(R.id.locationLayout);

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
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

        getBusinessReviews(businessId);

        return view;
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
                mBusiness = (Business) response;
                updateBusinessInfo(mBusiness);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    public void getBusinessReviews(String id) {
        mBusinessService.listBusinessReviews(id, 1, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mReviewList = (ArrayList<Review>) response;
                mAdapter.updateList(mReviewList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    /**
     * Update Business Info
     *
     * @param mBusiness
     */
    public void updateBusinessInfo(final Business mBusiness) {
        boolean hasIcon = false;
        int categoryColor = AppUtils.getCategoryColor(mContext, mBusiness.businesstype);
        GradientDrawable mDistanceBackground = (GradientDrawable) mTvDistance.getBackground();
        String street = mContext.getResources().getString(R.string.business_street, mBusiness.street, mBusiness.streetNumber);
        String distance = mContext.getResources().getString(R.string.business_distance, String.format("%.2f", mBusiness.distance));
        String ratingCount = mContext.getResources().getString(R.string.business_rating_count, mBusiness.ratingCount);
        String average = String.format("%.1f", mBusiness.ratingAverage);
        average = average.replace(",", ".");

        mTvName.setText(mBusiness.name);
        mTvDescription.setText(mBusiness.description);
        mTvPhone.setText(CommonUtils.formatPhone(mBusiness.phone));
        mTvWebsite.setText(mBusiness.website);
        mRbBusiness.setRating(mBusiness.ratingAverage);
        mTvStreet.setText(street);
        mTvDistance.setText(distance);
        mTvRatingAverage.setText(average);
        mTvRatingCount.setText(ratingCount);
        mDistanceBackground.setColor(categoryColor);
        hasIcon = checkSocialIcon(mBusiness.facebook, mBusiness.twitter, mBusiness.instagram, mBusiness.googlePlus);

        if (mBusiness.favorited) {
            mIBtnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            mIBtnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }

        if (TextUtils.isEmpty(mBusiness.description)) {
            mTvDescription.setVisibility(View.GONE);
            mTvDescriptionLabel.setVisibility(View.GONE);
            mSeparatorDescription.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(mBusiness.phone)) {
            mTvContactLabel.setVisibility(View.GONE);
            mContactLayout.setVisibility(View.GONE);
            mSeparatorContact.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(mBusiness.website) || TextUtils.equals("http://", mBusiness.website)) {
            mTvWebsiteLabel.setVisibility(View.GONE);
            mTvWebsite.setVisibility(View.GONE);
            mSeparatorWebsite.setVisibility(View.GONE);
        }

        if (mBusiness.ratingCount == 0) {
            mReviewLayout.setVisibility(View.GONE);
            mSeparatorReview.setVisibility(View.GONE);
        }

        if (!hasIcon) {
            mTvSocialNetworkLabel.setVisibility(View.GONE);
            mSeparatorSocial.setVisibility(View.GONE);
        }

        if (mBusiness.latitude != null && mBusiness.longitude != null) {
            updateLocation(mBusiness.latitude, mBusiness.longitude);
        } else {
            mLocationLayout.setVisibility(View.GONE);
            mSeparatorLocation.setVisibility(View.GONE);
        }

        mIBtnFavorite.setOnClickListener(favoriteListener);
        mTvWebsite.setOnClickListener(socialListener);
        Glide.with(mContext).load(mBusiness.image.url).error(R.drawable.business_background).into(mIvBusinessPhoto);
        hideLoading();
    }


    /**
     * Update Business Location
     */
    public void updateLocation(Double latitude, Double longitude) {
        LatLng center = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(center)
                .icon(BitmapDescriptorFactory.fromBitmap(AppUtils.getBitmap(getContext(), R.drawable.ic_marker)));
        mGMap.addMarker(markerOptions);
        mGMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 16));
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

    /**
     * Open Webpages
     *
     * @param url
     */
    public void openPage(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    /**
     * Show Request Loading
     */
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

    /**
     * Hide Request Loading
     */
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGMap = googleMap;
        mGMap.getUiSettings().setAllGesturesEnabled(false);
    }
}
