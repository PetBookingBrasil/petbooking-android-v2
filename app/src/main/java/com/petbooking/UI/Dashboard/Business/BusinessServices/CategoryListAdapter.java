package com.petbooking.UI.Dashboard.Business.BusinessServices;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Category;
import com.petbooking.R;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    public OnItemClick itemClick;
    private ArrayList<Category> mCategoryList;
    private Context mContext;

    private LinearLayoutManager mLayoutManager;

    public CategoryListAdapter(Context context, ArrayList<Category> categoryList, OnItemClick adapterInterface) {
        this.mCategoryList = categoryList;
        this.mContext = context;
        itemClick = adapterInterface;
    }

    public void updateList(ArrayList<Category> categoryList) {
        this.mCategoryList = categoryList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_category, parent, false);

        CategoryViewHolder holder = new CategoryViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, final int position) {
        Category category = mCategoryList.get(position % mCategoryList.size());

        int color = AppUtils.getCategoryColor(mContext, category.categoryName);
        GradientDrawable iconBackground = (GradientDrawable) holder.mIvCategoryIcon.getBackground();

        holder.mIvCategoryIcon.setImageResource(category.icon);
        holder.mTvCategoryName.setText(category.categoryText);

        holder.mIvCategoryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onItemClick(position);
            }
        });

        iconBackground.setColor(color);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public ImageView mIvCategoryIcon;
        public TextView mTvCategoryName;

        public CategoryViewHolder(View view) {
            super(view);

            mIvCategoryIcon = (ImageView) view.findViewById(R.id.category_icon);
            mTvCategoryName = (TextView) view.findViewById(R.id.category_name);

        }
    }

    public interface OnItemClick {

        void onItemClick(int position);

    }

}
