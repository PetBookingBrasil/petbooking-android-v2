package com.petbooking.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petbooking.R;

/**
 * Created by Luciano José on 06/04/2017.
 */

public class MenuItem extends LinearLayout {

    private ImageView mIvItemIcon;
    private TextView mTvItemText;


    public MenuItem(Context context, AttributeSet attrs) {
        super(context);
        initView(context, attrs);
    }

    public void initView(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.nav_drawer_item, this);

        mIvItemIcon = (ImageView) findViewById(R.id.icon);
        mTvItemText = (TextView) findViewById(R.id.text);

        if (attrs != null) {
            TypedArray prop = context.obtainStyledAttributes(attrs, R.styleable.MenuItem, 0, 0);

            String text = "";
            Drawable icon = null;

            try {
                text = prop.getString(R.styleable.MenuItem_item_text);
                icon = prop.getDrawable(R.styleable.MenuItem_item_icon);
            } catch (Exception e) {
                Log.e("ITEM", "There was an error loading attributes.");
            } finally {
                prop.recycle();
            }

            setText(text);
            setIcon(icon);
        }
    }

    private void setText(String text) {
        mTvItemText.setText(text);
    }

    private void setIcon(Drawable icon) {
        mIvItemIcon.setImageDrawable(icon);
    }
}
