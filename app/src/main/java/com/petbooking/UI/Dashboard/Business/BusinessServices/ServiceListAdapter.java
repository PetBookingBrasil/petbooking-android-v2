package com.petbooking.UI.Dashboard.Business.BusinessServices;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.CartItem;
import com.petbooking.R;
import com.petbooking.UI.Widget.CircleTransformation;
import com.petbooking.Utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceViewHolder> {

    private SimpleDateFormat mDateFormat;
    private SimpleDateFormat mTimeFormat;
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
        this.mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.mTimeFormat = new SimpleDateFormat("HH:mm");
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

        LinearLayoutManager mAdditionalLayout;
        AdditionalServiceListAdapter mAdditionalAdapter;

        Calendar calendar = new GregorianCalendar();
        Calendar timeCalendar = Calendar.getInstance();
        boolean isServiceChecked = false;
        CartItem item = null;

        String price = mContext.getResources().getString(R.string.business_service_price, String.format("%.2f", service.price));
        final String totalPrice;
        String appointmentDate;

        holder.mTvServiceName.setText(service.name);
        holder.mTvServicePrice.setText(price);

        if (petId != null) {
            isServiceChecked = mAppointmentManager.isServiceSelected(service.id, petId);
            item = mAppointmentManager.getItem(service.id, petId);
        }

        holder.mCbService.setOnCheckedChangeListener(null);
        holder.mCbService.setChecked(isServiceChecked);

        if (isServiceChecked) {
            totalPrice = mContext.getResources().getString(R.string.business_service_price, String.format("%.2f", item.totalPrice));

            try {
                calendar.setTime(mDateFormat.parse(item.startDate));
                timeCalendar.setTime(mTimeFormat.parse(item.startTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            timeCalendar.add(Calendar.MINUTE, (item.service.duration / 60));
            appointmentDate = mContext.getResources().getString(R.string.appointment_date_info, String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),
                    CommonUtils.uppercaseFirstLetter(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())),
                    item.startTime, mTimeFormat.format(timeCalendar.getTime()));

            holder.mSeparatorLine.setVisibility(View.VISIBLE);
            holder.mTvAppointmentDate.setVisibility(View.VISIBLE);
            holder.mAppointmentInfo.setVisibility(View.VISIBLE);

            holder.mTvAppointmentDate.setText(appointmentDate);
            holder.mTvProfessionalName.setText(item.professional.name);
            holder.mTvTotalPrice.setText(totalPrice);
            Glide.with(mContext)
                    .load(item.professional.imageUrl)
                    .error(R.drawable.ic_placeholder_user)
                    .bitmapTransform(new CircleTransformation(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.mIvProfessionalPhoto);
        } else {
            holder.mTvAppointmentDate.setVisibility(View.GONE);
            holder.mAppointmentInfo.setVisibility(View.GONE);
            holder.mSeparatorLine.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(service.description)) {
            holder.mTvServiceDescription.setVisibility(View.GONE);
        } else {
            holder.mTvServiceDescription.setText(service.description);
            holder.mTvServiceDescription.setVisibility(View.VISIBLE);
        }

        if (service.additionalServices.size() != 0 && isServiceChecked) {
            mAdditionalLayout = new LinearLayoutManager(mContext);
            mAdditionalLayout.setOrientation(LinearLayoutManager.VERTICAL);

            final CartItem finalItem = item;
            mAdditionalAdapter = new AdditionalServiceListAdapter(mContext, service.additionalServices, new AdditionalServiceListAdapter.OnAdditionalSelect() {
                @Override
                public void onSelect(boolean selected, String additionalId) {
                    String newPrice;
                    BusinessServices additional = getAdditional(service.additionalServices, additionalId);

                    if (selected) {
                        addNewAdditional(finalItem.service.id, additional, finalItem.pet.id);
                        finalItem.totalPrice += additional.price;
                    } else {
                        removeAdditional(finalItem.service.id, additional, finalItem.pet.id);
                        finalItem.totalPrice -= additional.price;
                    }

                    newPrice = mContext.getResources().getString(R.string.business_service_price, String.format("%.2f", finalItem.totalPrice));
                    holder.mTvTotalPrice.setText(newPrice);
                }
            });
            mAdditionalAdapter.setPetId(petId);
            mAdditionalAdapter.setFromDetail(false);

            holder.mRvAdditionalServices.setHasFixedSize(true);
            holder.mRvAdditionalServices.setLayoutManager(mAdditionalLayout);
            holder.mRvAdditionalServices.setAdapter(mAdditionalAdapter);

            holder.mTvAdditionalLabel.setVisibility(View.VISIBLE);
            holder.mRvAdditionalServices.setVisibility(View.VISIBLE);
        } else {
            holder.mTvAdditionalLabel.setVisibility(View.GONE);
            holder.mRvAdditionalServices.setVisibility(View.GONE);
        }

        holder.mCbService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serviceListener.onSelect(position);
                } else {
                    serviceListener.onRemove(position);
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
        public RelativeLayout mAppointmentInfo;
        public CheckBox mCbService;
        public TextView mTvServiceName;
        public TextView mTvServicePrice;
        public TextView mTvServiceDescription;
        public TextView mTvAppointmentDate;
        public TextView mTvTotalPrice;
        public ImageView mIvProfessionalPhoto;
        public TextView mTvProfessionalName;
        public View mSeparatorLine;
        public RecyclerView mRvAdditionalServices;
        public TextView mTvAdditionalLabel;

        public ServiceViewHolder(View view) {
            super(view);

            mCbService = (CheckBox) view.findViewById(R.id.service_checkbox);
            mServiceItem = (LinearLayout) view.findViewById(R.id.item_layout);
            mSeparatorLine = view.findViewById(R.id.separator_line);
            mAppointmentInfo = (RelativeLayout) view.findViewById(R.id.appointment_info);
            mTvAppointmentDate = (TextView) view.findViewById(R.id.appointment_date);
            mTvTotalPrice = (TextView) view.findViewById(R.id.total_price);
            mTvServiceName = (TextView) view.findViewById(R.id.service_name);
            mTvServicePrice = (TextView) view.findViewById(R.id.service_price);
            mTvServiceDescription = (TextView) view.findViewById(R.id.service_description);
            mIvProfessionalPhoto = (ImageView) view.findViewById(R.id.professional_photo);
            mTvProfessionalName = (TextView) view.findViewById(R.id.professional_name);
            mTvAdditionalLabel = (TextView) view.findViewById(R.id.additional_label);
            mRvAdditionalServices = (RecyclerView) view.findViewById(R.id.additional_services);
        }
    }

    /**
     * Get Additional price
     *
     * @param services
     * @param additionalId
     * @return
     */
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

    public interface OnServiceListener {
        void onSelect(int position);

        void onRemove(int position);
    }

}
