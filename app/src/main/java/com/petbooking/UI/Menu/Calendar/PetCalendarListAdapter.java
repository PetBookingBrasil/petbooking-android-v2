package com.petbooking.UI.Menu.Calendar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.Utils.CommonUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class PetCalendarListAdapter extends RecyclerView.Adapter<PetCalendarListAdapter.PetViewHolder> {

    private Context mContext;
    private ArrayList<Pet> mPetList;

    public PetCalendarListAdapter(Context context, ArrayList<Pet> petList) {
        this.mPetList = petList;
        this.mContext = context;
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
    public void onBindViewHolder(final PetViewHolder holder, int position) {

        Pet pet = mPetList.get(position);
        int deviceWidth = CommonUtils.getDeviceWidth(mContext);
        int width = deviceWidth - (deviceWidth / 100 * 50);
        ViewGroup.LayoutParams params = holder.mItemLayout.getLayoutParams();

        params.width = width;
        holder.mItemLayout.setLayoutParams(params);
        holder.mTvName.setText(pet.name);

        Glide.with(mContext)
                .load(pet.avatar.thumb.url)
                .centerCrop()
                .error(R.drawable.ic_menu_user)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mIvPhoto);
    }

    @Override
    public int getItemCount() {
        return mPetList.size();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mItemLayout;
        public CircleImageView mIvPhoto;
        public TextView mTvName;

        public PetViewHolder(View view) {
            super(view);

            mItemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
            mIvPhoto = (CircleImageView) view.findViewById(R.id.pet_photo);
            mTvName = (TextView) view.findViewById(R.id.pet_name);
        }
    }


}
