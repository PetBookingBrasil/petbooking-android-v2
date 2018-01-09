package com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petbooking.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by victorneves on 05/12/17.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.txt_selected_title)
    TextView headerTitle;
    @BindView(R.id.circleImage)
    CircleImageView circleImageView;
    @BindView(R.id.headerEdit)
    RelativeLayout headerEdit;
    @BindView(R.id.checkUnicode)
    TextView textCheckunicode;
    @BindView(R.id.img_header)
    ImageView image_header;
    @BindView(R.id.layout_additionals)
    LinearLayout layoutAdditionas;
    @BindView(R.id.additional_quant)
    TextView quantAdditional;
    @BindView(R.id.header_section)
    LinearLayout headerSection;
    @BindView(R.id.custom_header)
    CardView cardHeader;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
