package com.petbooking.UI.Menu.Pets;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.UI.Menu.Pets.ProfilePet.ProfilePetActivity;
import com.petbooking.UI.Widget.CircleTransformation;
import com.petbooking.Utils.APIUtils;

import java.util.ArrayList;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.PetViewHolder> {

    private ArrayList<Pet> mPetList;
    private Context mContext;
    private AdapterInterface mInterface;
    private Gson mGson;

    public PetListAdapter(Context context, ArrayList<Pet> petList) {
        this.mPetList = petList;
        this.mContext = context;
        this.mInterface = (AdapterInterface) context;
        this.mGson = new Gson();
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
        int petAvatar = pet.type.equals("dog") ? R.drawable.ic_placeholder_dog : R.drawable.ic_placeholder_cat;

        holder.mTvName.setText(pet.name);
        holder.mTvBreed.setText(pet.breedName);

        holder.mClItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile(mGson.toJson(pet));
            }
        });

        holder.mBtnRemovePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.onRemoveClick(pet.id);
            }
        });

        if (pet.avatar.url.contains(APIConstants.FALLBACK_TAG)) {
            holder.mIvPicture.setImageResource(petAvatar);
            return;
        }

        Glide.with(mContext)
                .load(APIUtils.getAssetEndpoint(pet.avatar.url))
                .error(petAvatar)
                .placeholder(petAvatar)
                .bitmapTransform(new CircleTransformation(mContext))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mIvPicture);

    }

    @Override
    public int getItemCount() {
        return mPetList.size();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mClItem;
        ImageView mIvPicture;
        TextView mTvName;
        TextView mTvBreed;
        ImageButton mBtnRemovePet;

        public PetViewHolder(View view) {
            super(view);

            mClItem = (ConstraintLayout) view.findViewById(R.id.layout);
            mIvPicture = (ImageView) view.findViewById(R.id.pet_picture);
            mTvName = (TextView) view.findViewById(R.id.pet_name);
            mTvBreed = (TextView) view.findViewById(R.id.pet_breed);
            mBtnRemovePet = (ImageButton) view.findViewById(R.id.remove_button);
        }
    }

    public interface AdapterInterface {
        void onRemoveClick(String id);
    }

    public void goToProfile(String pet) {
        Intent profileIntent = new Intent(mContext, ProfilePetActivity.class);
        profileIntent.putExtra("pet", pet);
        mContext.startActivity(profileIntent);
    }

}
