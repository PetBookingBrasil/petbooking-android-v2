package com.petbooking.UI.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.petbooking.R;

/**
 * Created by Luciano José on 05/06/2017.
 */

public class StyledSwitch extends RelativeLayout {

    private TextView mTvTitle;
    private SwitchButton mSwitch;

    public StyledSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.widget_styled_switch, this);

        mTvTitle = (TextView) view.findViewById(R.id.switch_title);
        mSwitch = (SwitchButton) view.findViewById(R.id.custom_switch);

    }

    public void setTitle(String title){
        mTvTitle.setText(title);
    }

}
