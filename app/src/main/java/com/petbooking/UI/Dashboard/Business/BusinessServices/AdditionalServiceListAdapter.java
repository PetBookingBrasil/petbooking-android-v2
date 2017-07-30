package com.petbooking.UI.Dashboard.Business.BusinessServices;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.petbooking.Models.BusinessServices;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class AdditionalServiceListAdapter extends RecyclerView.Adapter<AdditionalServiceListAdapter.AdditionalViewHolder> {

    private ArrayList<BusinessServices> mServiceList;
    private Context mContext;

    public AdditionalServiceListAdapter(Context context, ArrayList<BusinessServices> reviewList) {
        this.mServiceList = reviewList;
        this.mContext = context;
    }

    public void updateList(ArrayList<BusinessServices> serviceList) {
        this.mServiceList = serviceList;
    }

    @Override
    public AdditionalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_additional, parent, false);

        AdditionalViewHolder holder = new AdditionalViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final AdditionalViewHolder holder, int position) {
        final BusinessServices service = mServiceList.get(position);

        String price = mContext.getResources().getString(R.string.business_service_price, String.format("%.2f", service.price));

        holder.mCbServiceCheck.setText(service.name);
        //holder.mTvAdditionalName.setText(service.name);
        holder.mTvAdditionalPrice.setText(price);
    }

    @Override
    public int getItemCount() {
        return mServiceList.size();
    }

    public class AdditionalViewHolder extends RecyclerView.ViewHolder {

        public CheckBox mCbServiceCheck;
        public TextView mTvAdditionalPrice;

        public AdditionalViewHolder(View view) {
            super(view);

            mCbServiceCheck = (CheckBox) view.findViewById(R.id.additional_checkbox);
            mTvAdditionalPrice = (TextView) view.findViewById(R.id.additional_price);
        }
    }

}
