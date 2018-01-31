package com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Managers.AppointmentManager;
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
    int selectedPosition = -1;
    boolean initial = true;
    boolean imageLoaded = false;
    RequestManager mGlide;

    public PetAdapter(String title, SchedulingFragment fragment, List<Pet> pet, Context context) {
        super(new SectionParameters.Builder(R.layout.custom_pet_adapter)
                .headerResourceId(R.layout.header_scheduling)
                .build());
        this.title = title;
        this.fragment = fragment;
        this.pets = pet;
        this.context = context;
        mGlide = Glide.with(context);
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? 1 : 0;
    }

    public void addPets(ArrayList<Pet> pets) {
        this.pets = pets;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder item, final int position) {
        ItemViewHolder holder = (ItemViewHolder) item;
        holder.titleNumber.setVisibility(View.GONE);
        holder.circleLayout.setVisibility(View.GONE);
        holder.pets.setLayoutManager(new GridLayoutManager(context, 3));
        holder.pets.setAdapter(new CustomPetAdapter(context, pets));

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
        viewHolder.headerTitle.setText(title);
        viewHolder.headerEdit.setVisibility(View.VISIBLE);
        viewHolder.image_header.setVisibility(View.VISIBLE);
        viewHolder.headerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.clearFields();
                imageLoaded = false;
            }
        });
        if (selectedPosition >= 0) {
            Pet pet = pets.get(selectedPosition);
            int petAvatar;
            if (pet.type != null && pet.type.equals("dog")) {
                petAvatar = R.drawable.ic_placeholder_dog;
            } else {
                petAvatar = R.drawable.ic_placeholder_cat;
            }
            if(!imageLoaded) {
                imageLoaded = true;
                        mGlide.load(APIUtils.getAssetEndpoint(pet.avatar.url))
                        .error(petAvatar)
                        .placeholder(petAvatar)
                        .bitmapTransform(new CircleTransformation(context))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(viewHolder.image_header);
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            viewHolder.headerSection.setLayoutParams(params);

        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            viewHolder.headerSection.setLayoutParams(params);
            viewHolder.textCheckunicode.setText("1");
            viewHolder.textCheckunicode.setVisibility(View.VISIBLE);
            viewHolder.textCheckunicode.setTextColor(ContextCompat.getColor(context, R.color.gray));
            viewHolder.circleImageView.setImageResource(R.color.schedule_background);
        }
        viewHolder.headerSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.headerEdit.performClick();
            }
        });
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_custom)
        RecyclerView pets;
        @BindView(R.id.text_title_number)
        TextView titleNumber;
        @BindView(R.id.text_title)
        TextView textTitle;
        @BindView(R.id.circle_text)
        LinearLayout circleLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CustomPetAdapter extends RecyclerView.Adapter<CustomPetAdapter.ViewHolder> {
        Context context;
        List<Pet> pets;
        AppointmentManager mAppointmentManager;


        public CustomPetAdapter(Context context, List<Pet> pets) {
            this.context = context;
            this.pets = pets;
            this.mAppointmentManager = AppointmentManager.getInstance();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_list_pet_calendar, parent, false);
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
                    setSelectedPosition(position);

                }
            });

                    mGlide.load(APIUtils.getAssetEndpoint(pet.avatar.url))
                    .error(petAvatar)
                    .placeholder(petAvatar)
                    .bitmapTransform(new CircleTransformation(context))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.imgPet);

            if (initial && position == pets.size()) {
                PetAdapter.this.expanded = false;
                setTitle(pet.name);
                fragment.notifyChanged(position);
                setSelectedPosition(position);
            }
            if (mAppointmentManager.getCountCartPetId(pet.id) > 0) {
                holder.totalServices.setText(String.valueOf(mAppointmentManager.getCountCartPetId(pet.id)));
                holder.totalServices.setVisibility(View.VISIBLE);
            } else {
                holder.totalServices.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public int getItemCount() {
            return pets.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.pet_photo)
            ImageView imgPet;
            @BindView(R.id.total_appointments)
            TextView totalServices;
            @BindView(R.id.pet_name)
            TextView petName;
            View view;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                view = itemView;

            }
        }

    }

}



