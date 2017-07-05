package com.petbooking.UI.Widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.petbooking.R;

/**
 * Created by Luciano José on 23/06/2017.
 */

public class CircleIndicator extends LinearLayout {

    private ImageView btnOne;
    private ImageView btnTwo;
    private ImageView btnThree;

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        View view = View.inflate(context, R.layout.widget_circle_indicator, this);

        btnOne = (ImageView) view.findViewById(R.id.btn_one);
        btnTwo = (ImageView) view.findViewById(R.id.btn_two);
        btnThree = (ImageView) view.findViewById(R.id.btn_three);
    }

    public void setPageSelected(int position){
        switch (position){
            case 0:
                btnOne.setImageResource(R.drawable.ic_indicator_active);
                btnTwo.setImageResource(R.drawable.ic_indicator_inactive);
                btnThree.setImageResource(R.drawable.ic_indicator_inactive);
                break;
            case 1:
                btnOne.setImageResource(R.drawable.ic_indicator_inactive);
                btnTwo.setImageResource(R.drawable.ic_indicator_active);
                btnThree.setImageResource(R.drawable.ic_indicator_inactive);
                break;
            case 2:
                btnOne.setImageResource(R.drawable.ic_indicator_inactive);
                btnTwo.setImageResource(R.drawable.ic_indicator_inactive);
                btnThree.setImageResource(R.drawable.ic_indicator_active);
                break;
        }
    }
}
