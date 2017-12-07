package com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingFragment;
import com.petbooking.UI.Widget.CircleTransformation;
import com.petbooking.Utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by victorneves on 30/11/17.
 */

public class PetAdapter extends StatelessSection {
    String title;
    SchedulingFragment fragment;
    boolean expanded = true;
    Context context;
    List<Pet> pets;

    public PetAdapter(String title, SchedulingFragment fragment,List<Pet> pet,Context context) {
        super(new SectionParameters.Builder(R.layout.custom_pet_adapter)
                .headerResourceId(R.layout.header_scheduling)
                .build());
        this.title = title;
        this.fragment = fragment;
        this.pets = pet;
        this.context = context;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? 1 : 0;
    }

    public void addPets(ArrayList<Pet> pets){
        this.pets = pets;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder item, final int position) {
        ItemViewHolder holder = (ItemViewHolder) item;
        holder.pets.setLayoutManager(new GridLayoutManager(context,3));
        holder.pets.setAdapter(new CustomPetAdapter(context,pets));

    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
        viewHolder.headerTitle.setText(title);
        viewHolder.headerEdit.setVisibility(View.VISIBLE);
        viewHolder.image_header.setVisibility(View.VISIBLE);
        viewHolder.headerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.editPet();
            }
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
       @BindView(R.id.recycler_custom)
       RecyclerView pets;
       @BindView(R.id.text_title_number)
       TextView titleNumber;
       @BindView(R.id.text_title)
       TextView textTitle;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class CustomPetAdapter extends RecyclerView.Adapter<CustomPetAdapter.ViewHolder>{
        Context context;
        List<Pet> pets;

        public CustomPetAdapter(Context context, List<Pet> pets) {
            this.context = context;
            this.pets = pets;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_list_pet_calendar,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Pet pet = pets.get(position);
            holder.petName.setText(pet.name);
            int petAvatar;

            if (pet.type != null && pet.type.equals("dog")) {
                petAvatar = R.drawable.ic_placeholder_dog;
            } else {
                petAvatar = R.drawable.ic_placeholder_cat;
            }

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PetAdapter.this.expanded = false;
                    setTitle(pet.name);
                    fragment.notifyChanged(position);

                }
            });
            Glide.with(context)
                    .load(APIUtils.getAssetEndpoint(pet.avatar.url))
                    .error(petAvatar)
                    .placeholder(petAvatar)
                    .bitmapTransform(new CircleTransformation(context))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.imgPet);
        }

        @Override
        public int getItemCount() {
            return pets.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.pet_photo)
            ImageView imgPet;
            @BindView(R.id.pet_name)
            TextView petName;
            View view;
            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                view = itemView;

            }
        }

    }

}



