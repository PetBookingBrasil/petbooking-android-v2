package com.petbooking.UI.Menu.Search;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petbooking.Models.Category;
import com.petbooking.R;
import com.petbooking.Utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class FilterCategoryListAdapter extends RecyclerView.Adapter<FilterCategoryListAdapter.CategoryViewHolder> {

    private Context mContext;
    private ArrayList<Category> mCategoryList;

    public FilterCategoryListAdapter(Context context, ArrayList<Category> mCategoryList) {
        this.mContext = context;
        this.mCategoryList = mCategoryList;
    }

    public void updateList(ArrayList<Category> categoryList) {
        this.mCategoryList = categoryList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_category_filter, parent, false);

        CategoryViewHolder holder = new CategoryViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, final int position) {
        Category category = mCategoryList.get(position);

        int color = AppUtils.getCategoryColor(mContext, category.categoryName);
        GradientDrawable iconBackground = (GradientDrawable) holder.mIvCategoryIcon.getBackground();

        holder.mIvCategoryIcon.setImageResource(category.icon);
        holder.mTvCategoryName.setText(category.categoryText);

        iconBackground.setColor(color);
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
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

}
