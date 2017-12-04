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

import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingFragment;

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
    List<String> itens;
    SchedulingFragment fragment;
    boolean expaned = true;
    Context context;
    List<Pet> pets;

    public PetAdapter(String title, List<String> itens, SchedulingFragment fragment,List<Pet> pet,Context context) {
        super(new SectionParameters.Builder(R.layout.custom_pet_adapter)
                .headerResourceId(R.layout.header_scheduling)
                .build());
        this.title = title;
        this.itens = itens;
        this.fragment = fragment;
        this.pets = pet;
        this.context = context;
    }

    @Override
    public int getContentItemsTotal() {
        return expaned ? 1 : 0;
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

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_selected_title)
        TextView headerTitle;

        public HeaderViewHolder(View itemView) {
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
            final Pet text = pets.get(position);
            holder.petName.setText(text.name);
            holder.imgPet.setImageResource(R.drawable.ic_pet);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PetAdapter.this.expaned = false;
                    setTitle(text.name);
                    fragment.notifyChanged(position);

                }
            });
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



