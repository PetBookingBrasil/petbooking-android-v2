package com.petbooking.UI.Menu.Pets;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.UI.Menu.Pets.ProfilePetActivity.ProfilePetActivity;
import com.petbooking.UI.Menu.Profile.ProfileActivity;
import com.petbooking.Utils.APIUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.PetViewHolder> {

    private ArrayList<Pet> mPetList;
    private Context mContext;
    private AdapterInterface mInterface;

    public PetListAdapter(Context context, ArrayList<Pet> petList) {
        this.mPetList = petList;
        this.mContext = context;
        this.mInterface = (AdapterInterface) context;
    }

    public void updateList(ArrayList<Pet> petList) {
        this.mPetList = petList;
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_pets, parent, false);

        PetViewHolder holder = new PetViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(PetViewHolder holder, int position) {

        final Pet pet = mPetList.get(position);

        holder.mTvName.setText(pet.name);
        holder.mTvBreed.setText(pet.breedName);

        holder.mClItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile();
            }
        });

        holder.mBtnRemovePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.onRemoveClick(pet.id);
            }
        });

        Glide.with(mContext)
                .load(APIUtils.getAssetEndpoint(pet.avatar.url))
                .error(R.drawable.ic_menu_user)
                .placeholder(R.drawable.ic_menu_user)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .dontAnimate()
                .into(holder.mCvPicture);

    }

    @Override
    public int getItemCount() {
        return mPetList.size();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mClItem;
        CircleImageView mCvPicture;
        TextView mTvName;
        TextView mTvBreed;
        ImageButton mBtnRemovePet;

        public PetViewHolder(View view) {
            super(view);

            mClItem = (ConstraintLayout) view.findViewById(R.id.layout);
            mCvPicture = (CircleImageView) view.findViewById(R.id.pet_picture);
            mTvName = (TextView) view.findViewById(R.id.pet_name);
            mTvBreed = (TextView) view.findViewById(R.id.pet_breed);
            mBtnRemovePet = (ImageButton) view.findViewById(R.id.remove_button);
        }
    }

    public interface AdapterInterface {
        void onRemoveClick(String id);
    }

    public void goToProfile() {
        Intent profileIntent = new Intent(mContext, ProfilePetActivity.class);
        mContext.startActivity(profileIntent);
    }

}
