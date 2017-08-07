package com.petbooking.UI.Dashboard.Business.ServiceDetail;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.petbooking.Models.Professional;
import com.petbooking.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class ProfessionalListAdapter extends RecyclerView.Adapter<ProfessionalListAdapter.ServiceViewHolder> {

    private ArrayList<Professional> mProfessionalList;
    private int selectedPosition = -1;
    private Context mContext;

    public ProfessionalListAdapter(Context context, ArrayList<Professional> professionalList) {
        this.mContext = context;
        this.mProfessionalList = professionalList;
    }

    public void updateList(ArrayList<Professional> professionalList) {
        this.mProfessionalList = professionalList;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_professional, parent, false);

        ServiceViewHolder holder = new ServiceViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ServiceViewHolder holder, final int position) {
        final Professional professional = mProfessionalList.get(position);

        int redColor = mContext.getResources().getColor(R.color.brand_primary);
        int textColor = mContext.getResources().getColor(R.color.text_color);

        holder.mTvProfessionalName.setText(professional.name);

        if (selectedPosition == position) {
            holder.mIvPhoto.setBorderColor(redColor);
            holder.mIvPhoto.setBorderWidth(4);

            holder.mTvProfessionalName.setTextColor(redColor);
            holder.mTvProfessionalName.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.mIvPhoto.setBorderWidth(0);

            holder.mTvProfessionalName.setTextColor(textColor);
            holder.mTvProfessionalName.setTypeface(Typeface.DEFAULT);
        }

        if (professional.avatar == null) {
            holder.mIvPhoto.setImageResource(R.drawable.ic_placeholder_user);
        } else {
            Glide.with(mContext)
                    .load(professional.avatar.url)
                    .error(R.drawable.ic_placeholder_user)
                    .into(holder.mIvPhoto);
        }

        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfessional(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProfessionalList.size();
    }

    /**
     * Select professional
     *
     * @param position
     */
    public void selectProfessional(int position) {
        notifyItemChanged(selectedPosition);
        selectedPosition = position;
        notifyItemChanged(selectedPosition);
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mItemLayout;
        public CircleImageView mIvPhoto;
        public TextView mTvProfessionalName;

        public ServiceViewHolder(View view) {
            super(view);

            mItemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
            mIvPhoto = (CircleImageView) view.findViewById(R.id.professional_photo);
            mTvProfessionalName = (TextView) view.findViewById(R.id.professional_name);
        }
    }

}
