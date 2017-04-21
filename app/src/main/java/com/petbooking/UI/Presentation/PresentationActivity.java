package com.petbooking.UI.Presentation;

import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.petbooking.Constants.AppConstants;
import com.petbooking.R;

public class PresentationActivity extends AppCompatActivity {

    private ConstraintLayout mPresentationLayout;
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            position++;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPresentationLayout = (ConstraintLayout) findViewById(R.id.presentationLayout);
        mViewPager = (ViewPager) findViewById(R.id.presentationPager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(pageChangeListener);
    }

}
