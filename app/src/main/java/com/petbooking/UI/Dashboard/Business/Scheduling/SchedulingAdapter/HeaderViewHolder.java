package com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petbooking.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victorneves on 05/12/17.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.txt_selected_title)
    TextView headerTitle;
    @BindView(R.id.headerEdit)
    RelativeLayout headerEdit;
    @BindView(R.id.checkUnicode)
    TextView textCheckunicode;
    @BindView(R.id.img_header)
    ImageView image_header;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
