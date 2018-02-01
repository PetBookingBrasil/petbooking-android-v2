package com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
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
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingFragment;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by victorneves on 05/12/17.
 */

public class ServiceAdapter extends StatelessSection {
    Context mContext;
    List<BusinessServices> services;
    String title;
    String header;
    boolean expanded = false;
    boolean checked = false;
    private String petId;
    private AppointmentManager mAppointmentManager;
    private AdditionalServiceListAdapter mAdditionalServiceListAdapter;
    SchedulingFragment fragment;
    OnSelectecService onSelectecService;
    boolean serviceAdd = false;
    int selectedPosition = -1;


    AdditionalServiceListAdapter.OnAdditionalSelect mAdditionalSelected = new AdditionalServiceListAdapter.OnAdditionalSelect() {
        @Override
        public void onSelect(boolean selected, String additionalId) {
            BusinessServices service = services.get(selectedPosition);
            if (selected) {
                fragment.addServiceAdditional(getAdditional(service.additionalServices,additionalId));
            }else{
                fragment.removeAdditional(getAdditional(service.additionalServices,additionalId));
            }

        }
    };


    public ServiceAdapter(Context mContext, List<BusinessServices> services, String title, OnSelectecService onSelectecService,SchedulingFragment fragment) {
        super(new SectionParameters.Builder(R.layout.item_list_service)
                .headerResourceId(R.layout.header_scheduling)
                .build());
        this.mContext = mContext;
        this.services = services;
        this.title = title;
        this.onSelectecService = onSelectecService;
        this.mAppointmentManager = AppointmentManager.getInstance();
        mAdditionalServiceListAdapter = new AdditionalServiceListAdapter(mContext, new ArrayList<BusinessServices>(),mAdditionalSelected);
        this.fragment = fragment;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHeader(String header) {
        this.header = header;
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
        return expanded ? services.size() : 0;
    }

    public void setexpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder item, final int position) {
        final BusinessServices service = services.get(position);
        final ServiceViewHolder holder = (ServiceViewHolder) item;
        if(position !=0){
            holder.headerTitle.setVisibility(View.GONE);
        }
        mAdditionalServiceListAdapter.setPetId(petId);
        mAdditionalServiceListAdapter.setFromDetail(false);
        mAdditionalServiceListAdapter.updateList(service.additionalServices);
        holder.adicionalServicesList.setLayoutManager(new LinearLayoutManager(mContext));
        holder.adicionalServicesList.setHasFixedSize(true);
        holder.adicionalServicesList.setAdapter(mAdditionalServiceListAdapter);

        final Drawable colorGreen = ContextCompat.getDrawable(mContext,R.drawable.circle_background_green);
        final Drawable colorGray = ContextCompat.getDrawable(mContext,R.drawable.circle_background);
        String price = mContext.getResources().getString(R.string.business_service_price, String.format("%.2f", service.price));
        holder.serviceName.setText(service.name);
        holder.servicePrice.setText(price);
        if (TextUtils.isEmpty(service.description)) {
            holder.serviceDescription.setVisibility(View.GONE);
        } else {
            holder.serviceDescription.setText(service.description);
        }

        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition != position && !checked){
                    onSelectecService.onSelect(petId, service, true);
                    holder.circleImageView.setBackground(colorGreen);
                    holder.checkUnicode.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                    setSelectedPosition(position);
                    mAdditionalServiceListAdapter.updateList(service.additionalServices);
                    holder.adicionalServicesList.setVisibility(View.VISIBLE);
                    mAdditionalServiceListAdapter.notifyDataSetChanged();
                    if(service.additionalServices.size() >0)
                    holder.aditionalLabel.setVisibility(View.VISIBLE);
                    serviceAdd = true;

                }else if(!checked){
                    onSelectecService.onSelect(petId, null, false);
                    holder.circleImageView.setBackground(colorGray);
                    holder.checkUnicode.setTextColor(ContextCompat.getColor(mContext,R.color.light_gray));
                    setSelectedPosition(-1);
                    holder.adicionalServicesList.setVisibility(View.GONE);
                    holder.aditionalLabel.setVisibility(View.GONE);
                    serviceAdd = false;
                }
            }
        });

        if(checked){
            holder.circleImageView.setBackground(colorGreen);
            holder.checkUnicode.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            setSelectedPosition(position);
            mAdditionalServiceListAdapter.updateList(service.additionalServices);
            holder.adicionalServicesList.setVisibility(View.VISIBLE);
            mAdditionalServiceListAdapter.notifyDataSetChanged();
            if(service.additionalServices.size() >0)
            holder.aditionalLabel.setVisibility(View.VISIBLE);
        }else{
            holder.circleImageView.setBackground(colorGray);
            holder.checkUnicode.setTextColor(ContextCompat.getColor(mContext,R.color.light_gray));
            setSelectedPosition(-1);
            holder.adicionalServicesList.setVisibility(View.GONE);
            holder.aditionalLabel.setVisibility(View.GONE);
        }

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.circleImageView.performClick();
            }
        });
        checked = false;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        mAdditionalServiceListAdapter.clearServices();
    }

    public void setServiceAdd(boolean serviceAdd) {
        this.serviceAdd = serviceAdd;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
        viewHolder.headerTitle.setText(title);
        viewHolder.image_header.setVisibility(View.GONE);
        viewHolder.textCheckunicode.setVisibility(View.VISIBLE);

        if(serviceAdd){
            viewHolder.textCheckunicode.setText(R.string.check);
            viewHolder.textCheckunicode.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            viewHolder.circleImageView.setImageResource(R.drawable.circle_background_green);
            if(fragment.getCount() > 0) {
                viewHolder.layoutAdditionas.setVisibility(View.VISIBLE);
                viewHolder.quantAdditional.setText(MessageFormat.format("+{0}", fragment.getCount()));
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            viewHolder.headerSection.setLayoutParams(params);
            if(!expanded) {
                viewHolder.headerEdit.setVisibility(View.VISIBLE);
                viewHolder.headerEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment.expandedService();
                    }
                });
            }
        }else{
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            viewHolder.headerSection.setLayoutParams(params);
            viewHolder.textCheckunicode.setText("3");
            viewHolder.textCheckunicode.setTextColor(ContextCompat.getColor(mContext,R.color.gray));
            viewHolder.circleImageView.setImageResource(R.color.schedule_background);
            viewHolder.layoutAdditionas.setVisibility(View.GONE);
            viewHolder.headerEdit.setVisibility(View.GONE);
        }

    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.service_checkbox)
        RelativeLayout circleImageView;
        @BindView(R.id.checkUnicode)
        TextView checkUnicode;
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
        @BindView(R.id.additional_label)
        TextView aditionalLabel;
        @BindView(R.id.layout_header_title)
        LinearLayout headerTitle;
        View v;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            v = itemView;
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


}
