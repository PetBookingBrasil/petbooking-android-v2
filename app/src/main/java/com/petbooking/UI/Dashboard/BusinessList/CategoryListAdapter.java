package com.petbooking.UI.Dashboard.BusinessList;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petbooking.Models.Category;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter.CategoryAdapter;
import com.petbooking.UI.Dashboard.Content.ContentTabsAdapter;
import com.petbooking.Utils.AppUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by joice on 15/01/18.
 */

public class CategoryListAdapter extends  RecyclerView.Adapter<CategoryListAdapter.ViewHolder>{
    Context context;
    ArrayList<Category> categories;
    CategoryListFragment fragment;


    public CategoryListAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    public void setFragment(CategoryListFragment fragment) {
        this.fragment = fragment;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_item_list_category,parent,false);
        CategoryListAdapter.ViewHolder holder = new CategoryListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryListAdapter.ViewHolder holder, final int position) {
        final Category category = categories.get(position);
        int color = AppUtils.getCategoryColor(context, category.categoryName);
        GradientDrawable iconBackground = (GradientDrawable) holder.categoryIcon.getBackground();

        holder.categoryName.setText(category.categoryName);
        holder.categoryIcon.setImageDrawable(category.icon);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.categoryIcon.setClipToOutline(false);

        }

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (fragment!=null){
                        fragment.replaceFragment(category.id, category.categoryName);
                    }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iconBackground.setTint(color);
            Log.i(getClass().getSimpleName(), "Qual a elevation " + holder.categoryIcon.getElevation());
            holder.categoryIcon.setElevation(50f);
            Log.i(getClass().getSimpleName(), "Qual a elevation 2 " + holder.categoryIcon.getElevation());
        }else{
            iconBackground.setColor(color);
        }


    }

    @Override
    public int getItemCount() {
        return categories.size();
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
