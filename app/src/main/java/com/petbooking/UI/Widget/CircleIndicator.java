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
}
