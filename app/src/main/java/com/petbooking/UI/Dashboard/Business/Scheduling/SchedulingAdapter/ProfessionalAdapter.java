package com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.petbooking.Models.Professional;
import com.petbooking.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by victorneves on 05/12/17.
 */

public class ProfessionalAdapter extends StatelessSection {
    List<Professional> professionals;
    Context context;
    boolean expaned = false;

    public ProfessionalAdapter(SectionParameters sectionParameters) {
        super(new SectionParameters.Builder(R.layout.custom_professional_adapter)
                .headerResourceId(R.layout.header_scheduling)
                .build());
    }

    @Override
    public int getContentItemsTotal() {
        return expaned ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recycler_professional)
        RecyclerView professionalList;
        @BindView(R.id.recycler_dates)
        RecyclerView datesAvaliableList;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
