package com.petbooking.UI.Widget;

import android.content.Context;
import android.content.res.TypedArray;
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

    private RelativeLayout mView;
    private TextView mTvTitle;
    private SwitchButton mSwitch;

    OnClickListener viewListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isChecked = mSwitch.isChecked();
            mSwitch.setChecked(!isChecked);
        }
    };

    public StyledSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.widget_styled_switch, this);

        mView = (RelativeLayout) view.findViewById(R.id.switchView);
        mTvTitle = (TextView) view.findViewById(R.id.switch_title);

        mSwitch = (SwitchButton) view.findViewById(R.id.custom_switch);

        if (attrs != null) {
            TypedArray prop = context.getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.StyledSwitch, 0, 0);

            String title = "";

            try {
                title = prop.getString(R.styleable.StyledSwitch_title);
            } catch (Exception e) {
                Log.e("SWITCH", "There was an error loading attributes.");
            } finally {
                prop.recycle();
            }


            setTitle(title);
            mView.setOnClickListener(viewListener);
        }
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setChecked(boolean checked) {
        mSwitch.setChecked(checked);
    }

    public boolean isChecked() {
        return mSwitch.isChecked();
    }

    public SwitchButton getmSwitch() {
        return mSwitch;
    }
}
