package com.petbooking.UI.Menu.Search;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petbooking.Models.Category;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class FilterCategoryListAdapter extends RecyclerView.Adapter<FilterCategoryListAdapter.CategoryViewHolder> {

    private ArrayList<Category> mCategoryList;

    public FilterCategoryListAdapter(ArrayList<Category> mCategoryList) {
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

        holder.mIvCategoryIcon.setImageResource(category.icon);
        holder.mTvCategoryName.setText(category.name);

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
