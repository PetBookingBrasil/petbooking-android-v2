package com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Models.Category;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingFragment;
import com.petbooking.UI.Widget.CircleTransformation;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;

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
    boolean expanded = false;
    Context context;
    List<Category> services;
    OnSelectCategoryListener onSelectCategoryListener;
    int positionSelected = -1;
    Category category;
    SchedulingFragment fragment;

    public CategoryAdapter(String title, Context context, List<Category> services) {
        super(new SectionParameters.Builder(R.layout.custom_pet_adapter)
                .headerResourceId(R.layout.header_scheduling)
                .build());
        this.title = title;
        this.context = context;
        this.services = services;
    }

    public void setFragment(SchedulingFragment fragment) {
        this.fragment = fragment;
    }

    public void setOnSelectCategoryListener(OnSelectCategoryListener onSelectCategoryListener) {
        this.onSelectCategoryListener = onSelectCategoryListener;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? 1 : 0;
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
        holder.services.setLayoutManager(new GridLayoutManager(context, 3));
        holder.services.setAdapter(new CustomServiceAdapter(context, services));
        holder.conteinerLayout.setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent));
        holder.btnAddPet.setVisibility(View.INVISIBLE);
        holder.circleLayout.setVisibility(View.GONE);

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
        viewHolder.image_header.setVisibility(View.VISIBLE);


        if (positionSelected >= 0 || category != null) {
            Category category;
            if (positionSelected >= 0) {
                category = services.get(positionSelected);
            } else {
                category = this.category;
            }
            viewHolder.headerEdit.setVisibility(View.VISIBLE);

            viewHolder.textCheckunicode.setVisibility(View.GONE);
            int color = AppUtils.getCategoryColor(context, category.categoryName);
            //GradientDrawable iconBackground = (GradientDrawable) viewHolder.image_header.getBackground();

            if(category.iconUrl !=null){
                Glide.with(context)
                        .load(category.iconUrl)
                        .error(category.icon)
                        .placeholder(category.icon)
                        .bitmapTransform(new CircleTransformation(context))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(viewHolder.image_header);
            }else {
                Glide.with(context)
                        .load(getCategoryUrl(category.categoryName))
                        .error(category.icon)
                        .placeholder(category.icon)
                        .bitmapTransform(new CircleTransformation(context))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(viewHolder.image_header);
            }
            //viewHolder.image_header.setImageDrawable(category.icon);

            //iconBackground.setColor(color);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            viewHolder.headerSection.setLayoutParams(params);
            viewHolder.headerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.expandedCategory();
                }
            });
        } else {
            viewHolder.headerEdit.setVisibility(View.GONE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            viewHolder.headerSection.setLayoutParams(params);
            viewHolder.image_header.setVisibility(View.GONE);
            viewHolder.textCheckunicode.setText("2");
            viewHolder.textCheckunicode.setVisibility(View.VISIBLE);
            viewHolder.textCheckunicode.setTextColor(ContextCompat.getColor(context, R.color.gray));
            viewHolder.circleImageView.setImageResource(R.color.schedule_background);
        }
    }

    public void setPositionSelected(int positionSelected) {
        this.positionSelected = positionSelected;
    }

    public int getPositionSelected(){
        return this.positionSelected;
    }

    private String getCategoryUrl(String categoryName){
        if(CommonUtils.icons !=null){
            return CommonUtils.icons.get(categoryName);
        }
        return "";
    }

    public void setExpanable(boolean expanded) {
        this.expanded = expanded;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_custom)
        RecyclerView services;
        @BindView(R.id.text_title_number)
        TextView titleNumber;
        @BindView(R.id.text_title)
        TextView textTitle;
        @BindView(R.id.containerLayout)
        RelativeLayout conteinerLayout;
        @BindView(R.id.btn_add_pet)
        Button btnAddPet;
        @BindView(R.id.circle_text)
        LinearLayout circleLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CustomServiceAdapter extends RecyclerView.Adapter<CustomServiceAdapter.ViewHolder> {
        Context context;
        List<Category> services;

        public CustomServiceAdapter(Context context, List<Category> services) {
            this.context = context;
            this.services = services;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_list_category, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Category category = services.get(position);
            int color = AppUtils.getCategoryColor(context, category.categoryName);
            //GradientDrawable iconBackground = (GradientDrawable) holder.layoutBackGround.getBackground();

            holder.categoryName.setText(category.categoryName);
            if(category.iconUrl !=null){
                Glide.with(context)
                        .load(category.iconUrl)
                        .error(category.icon)
                        .placeholder(category.icon)
                        .bitmapTransform(new CircleTransformation(context))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.categoryIcon);
            }else {
                Glide.with(context)
                        .load(getCategoryUrl(category.categoryName))
                        .error(category.icon)
                        .placeholder(category.icon)
                        .bitmapTransform(new CircleTransformation(context))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.categoryIcon);
            }
            //holder.categoryIcon.setImageDrawable(category.icon);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.categoryIcon.setClipToOutline(false);

            }

            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectCategoryListener.onSelect(position);
                    setPositionSelected(position);
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               // iconBackground.setTint(color);
                Log.i(getClass().getSimpleName(), "Qual a elevation " + holder.categoryIcon.getElevation());
                holder.categoryIcon.setElevation(50f);
                Log.i(getClass().getSimpleName(), "Qual a elevation 2 " + holder.categoryIcon.getElevation());
            } else {
             //   iconBackground.setColor(color);
            }


        }

        @Override
        public int getItemCount() {
            return services.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.category_icon)
            ImageView categoryIcon;
            @BindView(R.id.category_name)
            TextView categoryName;
            @BindView(R.id.layoutCategory)
            LinearLayout layoutBackGround;
            View v;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                v = itemView;
            }
        }
    }

    public interface OnSelectCategoryListener {
        void onSelect(int position);
    }
}
