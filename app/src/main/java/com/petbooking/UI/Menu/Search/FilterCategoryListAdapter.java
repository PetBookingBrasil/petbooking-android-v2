package com.petbooking.UI.Menu.Search;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private OnSelectCategoryListener onSelectCategoryListener;
    private ArrayList<Category> mCategoryList;
    private int selectedPosition = -1;


    public FilterCategoryListAdapter(Context context, ArrayList<Category> mCategoryList) {
        this.mContext = context;
        this.mCategoryList = mCategoryList;
    }

    public void updateList(ArrayList<Category> categoryList) {
        selectedPosition = -1;
        this.mCategoryList = categoryList;
    }

    public void setOnSelectCategoryListener(OnSelectCategoryListener onSelectCategoryListener) {
        this.onSelectCategoryListener = onSelectCategoryListener;
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

        holder.mIvCategoryIcon.setImageDrawable(category.icon);
        holder.mTvCategoryName.setText(category.categoryText);

        if (position == selectedPosition) {
            holder.mTvCategoryName.setTextColor(color);
            holder.mTvCategoryName.setTypeface(Typeface.DEFAULT_BOLD);
            holder.mIvCategoryIcon.setAlpha((float) 1);
        } else {
            holder.mTvCategoryName.setTextColor(mContext.getResources().getColor(R.color.text_color));
            holder.mTvCategoryName.setTypeface(Typeface.DEFAULT);
            holder.mIvCategoryIcon.setAlpha((float) 0.4);
        }

        holder.mCategoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPosition);

                if (position == selectedPosition) {
                    selectedPosition = -1;
                } else {
                    selectedPosition = position;
                }

                notifyItemChanged(selectedPosition);
                onSelectCategoryListener.onSelect(position);
            }
        });

        iconBackground.setColor(color);
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mCategoryItem;
        public ImageView mIvCategoryIcon;
        public TextView mTvCategoryName;

        public CategoryViewHolder(View view) {
            super(view);

            mCategoryItem = (LinearLayout) view.findViewById(R.id.category_item);
            mIvCategoryIcon = (ImageView) view.findViewById(R.id.category_icon);
            mTvCategoryName = (TextView) view.findViewById(R.id.category_name);
        }
    }

    public interface OnSelectCategoryListener {
        void onSelect(int position);
    }

}
