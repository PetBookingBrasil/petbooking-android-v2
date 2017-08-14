package com.petbooking.UI.Dashboard.Business.BusinessServices;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Models.BusinessServices;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceViewHolder> {

    private String petId;
    private AppointmentManager mAppointmentManager;
    private ArrayList<BusinessServices> mServiceList;
    private OnServiceListener serviceListener;
    private Context mContext;

    public ServiceListAdapter(Context context, ArrayList<BusinessServices> serviceList, OnServiceListener serviceListener) {
        this.mServiceList = serviceList;
        this.mContext = context;
        this.serviceListener = serviceListener;
        this.mAppointmentManager = AppointmentManager.getInstance();
    }

    public void updateList(ArrayList<BusinessServices> serviceList) {
        this.mServiceList = serviceList;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_service, parent, false);

        ServiceViewHolder holder = new ServiceViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ServiceViewHolder holder, final int position) {
        final BusinessServices service = mServiceList.get(position);

        String price = mContext.getResources().getString(R.string.business_service_price, String.format("%.2f", service.price));
        boolean isServiceChecked = false;

        holder.mTvServiceName.setText(service.name);
        holder.mTvServicePrice.setText(price);

        if (petId != null) {
            isServiceChecked = mAppointmentManager.isServiceSelected(service.id, petId);
        }

        holder.mCbService.setOnCheckedChangeListener(null);
        holder.mCbService.setChecked(isServiceChecked);

        if (TextUtils.isEmpty(service.description)) {
            holder.mTvServiceDescription.setVisibility(View.GONE);
        } else {
            holder.mTvServiceDescription.setText(service.description);
            holder.mTvServiceDescription.setVisibility(View.VISIBLE);
        }

        holder.mCbService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serviceListener.onSelect(position);
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
        public CheckBox mCbService;
        public TextView mTvServiceName;
        public TextView mTvServicePrice;
        public TextView mTvServiceDescription;

        public ServiceViewHolder(View view) {
            super(view);

            mCbService = (CheckBox) view.findViewById(R.id.service_checkbox);
            mServiceItem = (LinearLayout) view.findViewById(R.id.item_layout);
            mTvServiceName = (TextView) view.findViewById(R.id.service_name);
            mTvServicePrice = (TextView) view.findViewById(R.id.service_price);
            mTvServiceDescription = (TextView) view.findViewById(R.id.service_description);
        }
    }

    public interface OnServiceListener {
        void onSelect(int position);
    }

}
