package com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petbooking.API.Business.BusinessService;
import com.petbooking.API.User.Models.ScheduleResp;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.CartItem;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.BusinessServices.AdditionalServiceListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by victorneves on 05/12/17.
 */

public class ServiceAdapter extends StatelessSection {
    Context mContext;
    List<BusinessServices> services;
    String title;
    boolean expaned = false;
    boolean checked = false;
    private String petId;
    private AppointmentManager mAppointmentManager;
    private AdditionalServiceListAdapter mAdditionalServiceListAdapter;
    OnSelectecService onSelectecService;

    public ServiceAdapter(Context mContext, List<BusinessServices> services, String title, OnSelectecService onSelectecService) {
        super(new SectionParameters.Builder(R.layout.item_list_service)
                .headerResourceId(R.layout.header_scheduling)
                .build());
        this.mContext = mContext;
        this.services = services;
        this.title = title;
        this.onSelectecService = onSelectecService;
        this.mAppointmentManager = AppointmentManager.getInstance();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setServices(List<BusinessServices> services, boolean checked) {
        this.services = services;
        this.checked = checked;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    @Override
    public int getContentItemsTotal() {
        return expaned ? services.size() : 0;
    }

    public void setExpaned(boolean expaned) {
        this.expaned = expaned;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder item, int position) {
        final BusinessServices service = services.get(position);
        final ServiceViewHolder holder = (ServiceViewHolder) item;
        CartItem cartItem = null;
        String price = mContext.getResources().getString(R.string.business_service_price, String.format("%.2f", service.price));
        holder.serviceName.setText(service.name);
        holder.servicePrice.setText(price);
        if (TextUtils.isEmpty(service.description)) {
            holder.serviceDescription.setVisibility(View.GONE);
        } else {
            holder.serviceDescription.setText(service.description);
        }

        final CartItem finalCartItem = cartItem;
        holder.adicionalServicesList.setVisibility(View.VISIBLE);

        holder.serviceCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !checked) {
                    configureAdditional(holder, finalCartItem, service);
                    onSelectecService.onSelect(petId, service, true);
                } else if (!checked) {
                    onSelectecService.onSelect(petId, null, false);
                    holder.adicionalServicesList.setVisibility(View.INVISIBLE);
                }
            }
        });
        holder.serviceCheckbox.setChecked(checked);
        checked = false;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
        viewHolder.headerTitle.setText(title);
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.service_checkbox)
        CheckBox serviceCheckbox;
        @BindView(R.id.item_layout)
        LinearLayout itemLayout;
        @BindView(R.id.separator_line)
        View separetorLine;
        @BindView(R.id.service_description)
        TextView serviceDescription;
        @BindView(R.id.service_name)
        TextView serviceName;
        @BindView(R.id.service_price)
        TextView servicePrice;
        @BindView(R.id.additional_services)
        RecyclerView adicionalServicesList;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public BusinessServices getAdditional(ArrayList<BusinessServices> services, String additionalId) {
        for (BusinessServices service : services) {
            if (service.id.equals(additionalId)) {
                return service;
            }
        }

        return null;
    }

    public void addNewAdditional(String serviceId, BusinessServices additional, String petId) {
        mAppointmentManager.addNewAdditional(serviceId, additional, petId);
    }

    public void removeAdditional(String serviceId, BusinessServices additional, String petId) {
        mAppointmentManager.removeAdditional(serviceId, additional, petId);
    }

    public interface OnSelectecService {
        void onSelect(String petId, BusinessServices serviceId, boolean add);
    }

    private void configureAdditional(ServiceViewHolder holder, CartItem cartItem, final BusinessServices service) {
        LinearLayoutManager additionalServiceLayout = new LinearLayoutManager(mContext);
        additionalServiceLayout.setOrientation(LinearLayoutManager.VERTICAL);
        final CartItem finalItem = cartItem;
        mAdditionalServiceListAdapter = new AdditionalServiceListAdapter(mContext, service.additionalServices, new AdditionalServiceListAdapter.OnAdditionalSelect() {
            @Override
            public void onSelect(boolean selected, String additionalId) {
                BusinessServices additional = getAdditional(service.additionalServices, additionalId);
                if (selected) {
                    //addNewAdditional(finalItem.service.id, additional, finalItem.pet.id);
                    //finalItem.totalPrice += additional.price;
                } else {
                    //removeAdditional(finalItem.service.id, additional, finalItem.pet.id);
                    //finalItem.totalPrice -= additional.price;
                }
            }
        });
        mAdditionalServiceListAdapter.setPetId(petId);
        mAdditionalServiceListAdapter.setFromDetail(false);
        holder.adicionalServicesList.setLayoutManager(additionalServiceLayout);
        holder.adicionalServicesList.setHasFixedSize(true);
        holder.adicionalServicesList.setAdapter(mAdditionalServiceListAdapter);
        holder.adicionalServicesList.setVisibility(View.VISIBLE);
    }
}
