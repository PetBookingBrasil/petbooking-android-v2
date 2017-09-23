package com.petbooking.UI.Dashboard.Business.ServiceDetail;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Models.Professional;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Widget.CircleTransformation;
import com.petbooking.Utils.APIUtils;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class ProfessionalListAdapter extends RecyclerView.Adapter<ProfessionalListAdapter.ProfessionalViewHolder> {

    private ArrayList<Professional> mProfessionalList;
    private int selectedPosition = -1;
    private OnSelectProfessionaListener onSelectProfessionaListener;
    private Context mContext;

    public ProfessionalListAdapter(Context context, ArrayList<Professional> professionalList, OnSelectProfessionaListener onSelectProfessionaListener) {
        this.mContext = context;
        this.mProfessionalList = professionalList;
        this.onSelectProfessionaListener = onSelectProfessionaListener;
    }

    public void updateList(ArrayList<Professional> professionalList) {
        this.mProfessionalList = professionalList;
    }

    @Override
    public ProfessionalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_professional, parent, false);

        ProfessionalViewHolder holder = new ProfessionalViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ProfessionalViewHolder holder, final int position) {
        final Professional professional = mProfessionalList.get(position);

        int redColor = mContext.getResources().getColor(R.color.brand_primary);
        int textColor = mContext.getResources().getColor(R.color.text_color);
        int professionalAvatar;

        if (professional.gender == null || professional.gender.equals(User.GENDER_MALE)) {
            professionalAvatar = R.drawable.ic_placeholder_man;
        } else {
            professionalAvatar = R.drawable.ic_placeholder_woman;
        }

        holder.mTvProfessionalName.setText(professional.name);

        if (selectedPosition == position) {
            holder.mTvProfessionalName.setTextColor(redColor);
            holder.mTvProfessionalName.setTypeface(Typeface.DEFAULT_BOLD);
            holder.mIvPhoto.setBackground(mContext.getResources().getDrawable(R.drawable.imageview_border));
        } else {
            holder.mTvProfessionalName.setTextColor(textColor);
            holder.mTvProfessionalName.setTypeface(Typeface.DEFAULT);
            holder.mIvPhoto.setBackground(null);
        }

        if (professional.imageUrl == null || professional.imageUrl.contains(APIConstants.FALLBACK_TAG)) {
            holder.mIvPhoto.setImageResource(professionalAvatar);
        } else {
            Glide.with(mContext)
                    .load(APIUtils.getAssetEndpoint(professional.imageUrl))
                    .error(professionalAvatar)
                    .placeholder(professionalAvatar)
                    .bitmapTransform(new CircleTransformation(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
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
        onSelectProfessionaListener.onSelect(position);
    }

    public class ProfessionalViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mItemLayout;
        public ImageView mIvPhoto;
        public TextView mTvProfessionalName;

        public ProfessionalViewHolder(View view) {
            super(view);

            mItemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
            mIvPhoto = (ImageView) view.findViewById(R.id.professional_photo);
            mTvProfessionalName = (TextView) view.findViewById(R.id.professional_name);
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyItemChanged(selectedPosition);
    }

    public interface OnSelectProfessionaListener {
        void onSelect(int position);
    }

}
