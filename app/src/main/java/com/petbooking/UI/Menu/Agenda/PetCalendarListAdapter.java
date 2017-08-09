package com.petbooking.UI.Menu.Agenda;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class PetCalendarListAdapter extends RecyclerView.Adapter<PetCalendarListAdapter.PetViewHolder> {

    private Context mContext;
    private AppointmentManager mAppointmentManager;
    private OnSelectPetListener onSelectPetListener;
    private ArrayList<Pet> mPetList;
    private int selectedPosition = -1;

    public PetCalendarListAdapter(Context context, ArrayList<Pet> petList, OnSelectPetListener onSelectPetListener) {
        this.mPetList = petList;
        this.mContext = context;
        this.mAppointmentManager = AppointmentManager.getInstance();
        this.onSelectPetListener = onSelectPetListener;
    }

    public void updateList(ArrayList<Pet> petList) {
        this.mPetList = petList;
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_pet_calendar, parent, false);

        PetViewHolder holder = new PetViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final PetViewHolder holder, final int position) {

        Pet pet = mPetList.get(position);
        int redColor = mContext.getResources().getColor(R.color.brand_primary);
        int textColor = mContext.getResources().getColor(R.color.text_color);
        int totalAppointments = mAppointmentManager.getTotalAppointments(pet.id);

        if (totalAppointments == 0) {
            holder.mTvTotalAppointments.setVisibility(View.GONE);
        } else {
            holder.mTvTotalAppointments.setVisibility(View.VISIBLE);
            holder.mTvTotalAppointments.setText(String.valueOf(totalAppointments));
        }

        if (selectedPosition == position) {
            holder.mIvPhoto.setBorderColor(redColor);
            holder.mIvPhoto.setBorderWidth(4);
            holder.mIvPhoto.setColorFilter(mContext.getResources().getColor(R.color.picture_filter_color));

            holder.mTvName.setTextColor(redColor);
            holder.mTvName.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.mIvPhoto.setBorderWidth(0);
            holder.mIvPhoto.setColorFilter(null);

            holder.mTvName.setTextColor(textColor);
            holder.mTvName.setTypeface(Typeface.DEFAULT);
        }

        holder.mTvName.setText(pet.name);
        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPet(position);
            }
        });

        Glide.with(mContext)
                .load(pet.avatar.url)
                .centerCrop()
                .error(R.drawable.ic_menu_user)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mIvPhoto);
    }

    /**
     * Select pet
     *
     * @param position
     */
    public void selectPet(int position) {
        notifyItemChanged(selectedPosition);
        selectedPosition = position;
        notifyItemChanged(selectedPosition);
        onSelectPetListener.onSelect(position);
    }

    @Override
    public int getItemCount() {
        return mPetList.size();
    }

    /**
     * Reset Selected Position
     */
    public void resetSelectedPosition() {
        selectedPosition = -1;
        onSelectPetListener.onSelect(selectedPosition);
        notifyDataSetChanged();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mItemLayout;
        public CircleImageView mIvPhoto;
        public TextView mTvName;
        public TextView mTvTotalAppointments;

        public PetViewHolder(View view) {
            super(view);

            mItemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
            mIvPhoto = (CircleImageView) view.findViewById(R.id.pet_photo);
            mTvName = (TextView) view.findViewById(R.id.pet_name);
            mTvTotalAppointments = (TextView) view.findViewById(R.id.total_appointments);
        }
    }


    public interface OnSelectPetListener {
        void onSelect(int position);
    }
}
