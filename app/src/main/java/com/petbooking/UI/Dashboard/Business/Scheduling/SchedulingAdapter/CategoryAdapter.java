package com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petbooking.Models.Category;
import com.petbooking.R;
import com.petbooking.Utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by victorneves on 30/11/17.
 */

public class CategoryAdapter extends StatelessSection {
    String title;
    List<String> itens;
    boolean expaned = false;
    Context context;
    List<Category> services;
    OnSelectCategoryListener onSelectCategoryListener;

    public CategoryAdapter(String title, List<String> itens, Context context, List<Category> services) {
        super(new SectionParameters.Builder(R.layout.custom_pet_adapter)
                .headerResourceId(R.layout.header_scheduling)
                .build());
        this.title = title;
        this.itens = itens;
        this.context = context;
        this.services = services;
    }

    public void setOnSelectCategoryListener(OnSelectCategoryListener onSelectCategoryListener) {
        this.onSelectCategoryListener = onSelectCategoryListener;
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
    public void onBindItemViewHolder(RecyclerView.ViewHolder item, final int position) {
        ItemViewHolder holder = (ItemViewHolder) item;
        holder.titleNumber.setText("2");
        holder.textTitle.setText(R.string.bussines_category);
        holder.services.setLayoutManager(new GridLayoutManager(context,3));
        holder.services.setAdapter(new CustomServiceAdapter(context,services));

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setServices(List<Category> services) {
        this.services = services;
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

    public void setExpanable(boolean expaned) {
        this.expaned = expaned;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_custom)
        RecyclerView services;
        @BindView(R.id.text_title_number)
        TextView titleNumber;
        @BindView(R.id.text_title)
        TextView textTitle;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CustomServiceAdapter extends RecyclerView.Adapter<CustomServiceAdapter.ViewHolder>{
        Context context;
        List<Category> services;

        public CustomServiceAdapter(Context context, List<Category> services) {
            this.context = context;
            this.services = services;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_list_category,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Category category = services.get(position);
            int color = AppUtils.getCategoryColor(context, category.categoryName);
            GradientDrawable iconBackground = (GradientDrawable) holder.categoryIcon.getBackground();
            holder.categoryName.setText(category.categoryName);
            holder.categoryIcon.setImageDrawable(category.icon);

            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectCategoryListener.onSelect(position);
                }
            });

            iconBackground.setColor(color);
        }

        @Override
        public int getItemCount() {
            return services.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.category_icon)
            ImageView categoryIcon;
            @BindView(R.id.category_name)
            TextView categoryName;
            View v;
            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                v = itemView;
            }
        }
    }

    public interface OnSelectCategoryListener {
        void onSelect(int position);
    }
}
