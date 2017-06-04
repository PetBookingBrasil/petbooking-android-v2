package com.petbooking.UI.Menu.Favorites;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petbooking.Models.Business;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.FavoriteViewHolder> {

    private ArrayList<Business> mBusinessList;
    private Context mContext;

    public FavoritesListAdapter(Context context, ArrayList<Business> businessList) {
        this.mBusinessList = businessList;
        this.mContext = context;
    }

    public void updateList(ArrayList<Business> businessList) {
        this.mBusinessList = businessList;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_favorites, parent, false);

        FavoriteViewHolder holder = new FavoriteViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final FavoriteViewHolder holder, final int position) {

        final Business business = mBusinessList.get(position);

        holder.mTvName.setText(business.name);

    }

    @Override
    public int getItemCount() {
        return mBusinessList.size();
    }


    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mClBusiness;
        TextView mTvName;

        public FavoriteViewHolder(View view) {
            super(view);

            mClBusiness = (LinearLayout) view.findViewById(R.id.business_item);
            mTvName = (TextView) view.findViewById(R.id.business_name);
        }
    }

}
