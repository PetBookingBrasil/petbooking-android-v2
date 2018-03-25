package com.petbooking.UI.Dashboard.Business.BusinessServices;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.petbooking.API.Appointment.Models.ServiceResp;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Models.BusinessServices;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class AdditionalServiceListAdapter extends RecyclerView.Adapter<AdditionalServiceListAdapter.AdditionalViewHolder> {

    private boolean isFromDetail;
    private String petId;
    private AppointmentManager mAppointmentManager;
    private OnAdditionalSelect onAdditionalSelect;
    private ArrayList<BusinessServices> mServiceList;
    private ArrayList<BusinessServices> servicesInsert;
    private Context mContext;
    int positionSelected = -1;

    public AdditionalServiceListAdapter(Context context, ArrayList<BusinessServices> reviewList, OnAdditionalSelect onAdditionalSelect) {
        this.mServiceList = reviewList;
        this.mContext = context;
        this.onAdditionalSelect = onAdditionalSelect;
        servicesInsert = new ArrayList<>();
        this.mAppointmentManager = AppointmentManager.getInstance();
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
    public void onBindViewHolder(final AdditionalViewHolder holder, final int position) {
        final Drawable colorGreen = ContextCompat.getDrawable(mContext, R.drawable.circle_background_green);
        final Drawable colorGray = ContextCompat.getDrawable(mContext, R.drawable.circle_background);
        final BusinessServices service = mServiceList.get(position);

        String price = mContext.getResources().getString(R.string.business_service_price, String.format("%.2f", service.price));
        boolean isAdditionalSelected = false;

        if (petId != null) {
            isAdditionalSelected = existsAdditional(service, servicesInsert);
            setPositionSelected(position);
        }

        if (mServiceList.size() <= 1) {
            holder.view.setVisibility(View.INVISIBLE);
        }

        holder.mServiceName.setText(service.name);
        holder.mTvAdditionalPrice.setText(price);

        final boolean finalIsAdditionalSelected = isAdditionalSelected;
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mCbServiceCheck.performClick();
            }
        });
        holder.mCbServiceCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!existsAdditional(service, servicesInsert)) {
                    onAdditionalSelect.onSelect(true, service.id);
                    setPositionSelected(position);
                    servicesInsert.add(service);
                    holder.mCbServiceCheck.setBackground(colorGreen);
                    holder.check.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                } else {
                    setPositionSelected(-1);
                    holder.mCbServiceCheck.setBackground(colorGray);
                    servicesInsert.remove(service);
                    holder.check.setTextColor(ContextCompat.getColor(mContext, R.color.light_gray));
                    onAdditionalSelect.onSelect(false, service.id);
                }
            }
        });

        if (isAdditionalSelected) {
            holder.mCbServiceCheck.setBackground(colorGreen);
            holder.check.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            holder.mCbServiceCheck.setBackground(colorGray);
            holder.check.setTextColor(ContextCompat.getColor(mContext, R.color.light_gray));
        }

    }

    public boolean existsAdditional(BusinessServices additional, ArrayList<BusinessServices> additionals) {
        for (BusinessServices services : additionals) {
            if (services.id.equals(additional.id)) {
                return true;
            }
        }
        return false;
    }

    public void clearServices() {
        this.servicesInsert.clear();
    }

    public void setPositionSelected(int positionSelected) {
        this.positionSelected = positionSelected;
    }

    @Override
    public int getItemCount() {
        if (mServiceList != null)
            return mServiceList.size();
        else
            return 0;
    }

    public class AdditionalViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout mCbServiceCheck;
        public TextView mTvAdditionalPrice;
        public TextView mServiceName;
        public TextView check;
        public View view;
        public View v;

        public AdditionalViewHolder(View view) {
            super(view);
            v = view;
            mCbServiceCheck = (RelativeLayout) view.findViewById(R.id.additional_checkbox);
            mTvAdditionalPrice = (TextView) view.findViewById(R.id.additional_price);
            mServiceName = (TextView) view.findViewById(R.id.service_additional_name);
            check = (TextView) view.findViewById(R.id.text_check);
            this.view = view.findViewById(R.id.separator_bottom);
        }
    }

    public void setFromDetail(boolean fromDetail) {
        isFromDetail = fromDetail;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public interface OnAdditionalSelect {
        void onSelect(boolean selected, String additionalId);
    }

}
