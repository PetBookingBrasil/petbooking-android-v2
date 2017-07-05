package com.petbooking.UI.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.petbooking.R;
import com.petbooking.UI.Login.LoginActivity;
import com.petbooking.UI.Widget.CircleIndicator;

public class PresentationActivity extends AppCompatActivity {

    private final int[] colors = {R.color.presentation_1, R.color.presentation_2, R.color.presentation_3};
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private Button mBtnNextTour;
    private Button mBtnSkip;

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.next) {
                int position = mViewPager.getCurrentItem();
                position++;
                if (position == 3) {
                    goToLogin();
                } else {
                    mBtnNextTour.setTextColor(getResources().getColor(colors[position]));
                    mViewPager.setCurrentItem(position);
                }
            } else {
                goToLogin();
            }
        }
    };

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mBtnNextTour.setTextColor(getResources().getColor(colors[position]));
            mCircleIndicator.setPageSelected(position);
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
        mCircleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);

        mViewPager = (ViewPager) findViewById(R.id.presentationPager);
        mViewPager.setAdapter(mPagerAdapter);

        mBtnNextTour = (Button) findViewById(R.id.next);
        mBtnSkip = (Button) findViewById(R.id.skip);

        mBtnNextTour.setOnClickListener(mListener);
        mViewPager.addOnPageChangeListener(pageChangeListener);

        mBtnNextTour.setTextColor(getResources().getColor(colors[0]));
        mCircleIndicator.setPageSelected(0);
        mBtnSkip.setOnClickListener(mListener);
    }

    /**
     * Go to Login Page
     */
    public void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
