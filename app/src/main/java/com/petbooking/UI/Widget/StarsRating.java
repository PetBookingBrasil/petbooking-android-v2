package com.petbooking.UI.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.petbooking.R;

/**
 * Created by Luciano José on 20/05/2017.
 */

public class StarsRating extends LinearLayout {

    private ImageView mIvStar1;
    private ImageView mIvStar2;
    private ImageView mIvStar3;
    private ImageView mIvStar4;
    private ImageView mIvStar5;

    public StarsRating(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public StarsRating(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.widget_star_rating, this);

        mIvStar1 = (ImageView) view.findViewById(R.id.star1);
        mIvStar2 = (ImageView) view.findViewById(R.id.star2);
        mIvStar3 = (ImageView) view.findViewById(R.id.star3);
        mIvStar4 = (ImageView) view.findViewById(R.id.star4);
        mIvStar5 = (ImageView) view.findViewById(R.id.star5);
    }

    public void setRating(float average) {
        mIvStar1.setImageResource(getStarDrawable(1, average));
        mIvStar2.setImageResource(getStarDrawable(2, average));
        mIvStar3.setImageResource(getStarDrawable(3, average));
        mIvStar4.setImageResource(getStarDrawable(4, average));
        mIvStar5.setImageResource(getStarDrawable(5, average));
    }

    private int getStarDrawable(int position, float average) {
        boolean full = average >= position;
        boolean half = average > position - 1;
        return full ? R.drawable.ic_star_rating_on :
                half ? R.drawable.ic_star_rating_half : R.drawable.ic_star_rating_off;
    }

}