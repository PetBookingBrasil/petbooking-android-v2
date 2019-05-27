package com.petbooking.UI.Dashboard.BusinessList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Models.Banner;
import com.petbooking.R;
import com.petbooking.UI.Widget.CircleTransformation;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerFragment extends Fragment {

    @BindView(R.id.imageBanner)
    ImageView imageBanner;

    private int position;
    private ArrayList<Banner> banners;
    CategoryListFragment fragment;

    public BannerFragment() {

    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setBanners(ArrayList<Banner> banners) {
        this.banners = banners;
    }

    public void setFragment(CategoryListFragment fragment) {
        this.fragment = fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.banner_layout, container, false);
        ButterKnife.bind(this, view);
        final Banner banner = banners.get(position);
        Glide.with(getContext())
                .load(banner.getImage())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageBanner);

        imageBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment!=null){
                    fragment.replaceFragment(banner.getId(), banner.getTitle(), null);
                }
            }
        });
        return view;
    }
}
