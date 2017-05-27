package com.petbooking.UI.Dashboard.Business.BusinessServices;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petbooking.Models.BusinessServices;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServicewViewHolder> {

    private ArrayList<BusinessServices> mServiceList;
    private Context mContext;

    public ServiceListAdapter(Context context, ArrayList<BusinessServices> reviewList) {
        this.mServiceList = reviewList;
        this.mContext = context;
    }

    public void updateList(ArrayList<BusinessServices> serviceList) {
        this.mServiceList = serviceList;
    }

    @Override
    public ServicewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_service, parent, false);

        ServicewViewHolder holder = new ServicewViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ServicewViewHolder holder, int position) {

        final BusinessServices service = mServiceList.get(position);

    }

    @Override
    public int getItemCount() {
        return mServiceList.size();
    }

    public class ServicewViewHolder extends RecyclerView.ViewHolder {

        public ServicewViewHolder(View view) {
            super(view);

        }
    }

}
