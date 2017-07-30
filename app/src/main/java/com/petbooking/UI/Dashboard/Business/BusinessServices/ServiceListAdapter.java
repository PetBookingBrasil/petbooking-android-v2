package com.petbooking.UI.Dashboard.Business.BusinessServices;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.petbooking.Models.BusinessServices;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceViewHolder> {

    private ArrayList<BusinessServices> mServiceList;
    private Context mContext;

    private LinearLayoutManager mLayoutManager;

    public ServiceListAdapter(Context context, ArrayList<BusinessServices> serviceList) {
        this.mServiceList = serviceList;
        this.mContext = context;
    }

    public void updateList(ArrayList<BusinessServices> serviceList) {
        this.mServiceList = serviceList;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_service, parent, false);

        ServiceViewHolder holder = new ServiceViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ServiceViewHolder holder, int position) {
        final BusinessServices service = mServiceList.get(position);

        AdditionalServiceListAdapter mAdapter;
        String price = mContext.getResources().getString(R.string.business_service_price, String.format("%.2f", service.price));

        holder.mTvServiceName.setText(service.name);
        holder.mTvServicePrice.setText(price);

        if (TextUtils.isEmpty(service.description)) {
            holder.mTvServiceDescription.setVisibility(View.GONE);
        } else {
            holder.mTvServiceDescription.setText(service.description);
            holder.mTvServiceDescription.setVisibility(View.VISIBLE);
        }

        if (service.additionalServices.size() != 0) {
            mAdapter = new AdditionalServiceListAdapter(mContext, service.additionalServices);

            mLayoutManager = new LinearLayoutManager(mContext);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            holder.mRvAdditionalServices.setHasFixedSize(true);
            holder.mRvAdditionalServices.setLayoutManager(mLayoutManager);

            if (mAdapter != null) {
                holder.mRvAdditionalServices.setAdapter(mAdapter);
            }
        }

        holder.mServiceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (service.additionalServices.size() == 0) {
                    return;
                }

                if (holder.mTvAdditionalLabel.isShown()) {
                    holder.mTvAdditionalLabel.setVisibility(View.GONE);
                    holder.mRvAdditionalServices.setVisibility(View.GONE);
                    holder.mVAdditionalSeparator.setVisibility(View.GONE);
                } else {
                    holder.mTvAdditionalLabel.setVisibility(View.VISIBLE);
                    holder.mRvAdditionalServices.setVisibility(View.VISIBLE);
                    holder.mVAdditionalSeparator.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mServiceList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mServiceItem;
        public TextView mTvServiceName;
        public TextView mTvServicePrice;
        public TextView mTvServiceDescription;
        public TextView mTvAdditionalLabel;
        public RecyclerView mRvAdditionalServices;
        public View mVAdditionalSeparator;

        public ServiceViewHolder(View view) {
            super(view);

            mServiceItem = (LinearLayout) view.findViewById(R.id.item_layout);
            mTvServiceName = (TextView) view.findViewById(R.id.service_name);
            mTvServicePrice = (TextView) view.findViewById(R.id.service_price);
            mTvServiceDescription = (TextView) view.findViewById(R.id.service_description);
            mTvAdditionalLabel = (TextView) view.findViewById(R.id.additional_label);
            mRvAdditionalServices = (RecyclerView) view.findViewById(R.id.additional_services);
            mVAdditionalSeparator = view.findViewById(R.id.additional_separator);
        }
    }

}
