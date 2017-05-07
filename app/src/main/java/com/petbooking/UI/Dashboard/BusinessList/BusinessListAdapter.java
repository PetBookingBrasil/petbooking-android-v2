package com.petbooking.UI.Dashboard.BusinessList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.petbooking.Models.Business;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * Created by Luciano José on 29/01/2017.
 */

public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.BusinessViewHolder> {

    private ArrayList<Business> mBusinessList;
    private Context mContext;

    public BusinessListAdapter(Context context, ArrayList<Business> businessList) {
        this.mBusinessList = businessList;
        this.mContext = context;
    }

    public void updateList(ArrayList<Business> businessList) {
        this.mBusinessList = businessList;
    }

    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_business, parent, false);

        BusinessViewHolder holder = new BusinessViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder holder, int position) {

        final Business business = mBusinessList.get(position);

        holder.mTvRate.setText(business.id);
        holder.mTvName.setText(business.name);
    }

    @Override
    public int getItemCount() {
        return mBusinessList.size();
    }

    public class BusinessViewHolder extends RecyclerView.ViewHolder {

        TextView mTvRate;
        TextView mTvName;

        public BusinessViewHolder(View view) {
            super(view);

            mTvRate = (TextView) view.findViewById(R.id.business_rate);
            mTvName = (TextView) view.findViewById(R.id.business_name);
        }
    }

}
