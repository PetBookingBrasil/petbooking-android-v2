package com.petbooking.UI.Menu.Agenda;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Models.BusinessServices;
import com.petbooking.R;

import java.util.ArrayList;


/**
 * Created by Luciano José on 29/01/2017.
 */

public class AgendaServiceListAdapter extends RecyclerView.Adapter<AgendaServiceListAdapter.AgendaViewHolder> {

    private ArrayList<BusinessServices> mServicesList;
    private Context mContext;
    private OnServiceActionListener onServiceActionListener;
    private LinearLayoutManager mAdditionalLayoutManager;

    public AgendaServiceListAdapter(Context context, ArrayList<BusinessServices> servicesList, OnServiceActionListener onServiceActionListener) {
        this.mServicesList = servicesList;
        this.mContext = context;
        this.onServiceActionListener = onServiceActionListener;
    }

    public void updateList(ArrayList<BusinessServices> servicesList) {
        this.mServicesList = servicesList;
    }

    @Override
    public AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_agenda_service, parent, false);

        AgendaViewHolder holder = new AgendaViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(AgendaViewHolder holder, final int position) {
        BusinessServices service = mServicesList.get(position);

        AgendaAdditionalServiceListAdapter mAdapter;
        String price = mContext.getResources().getString(R.string.business_service_price, String.format("%.2f", service.price));

        holder.mTvBusinessName.setText(service.businessName);
        holder.mTvServiceName.setText(service.name);
        holder.mTvServiceTime.setText(service.startTime + " - " + service.endTime);
        holder.mTvProfessionalName.setText(service.professionalName);
        holder.mTvServicePrice.setText(price);

        if (service.additionalServices.size() != 0) {
            mAdapter = new AgendaAdditionalServiceListAdapter(mContext, service.additionalServices);

            mAdditionalLayoutManager = new LinearLayoutManager(mContext);
            mAdditionalLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            holder.mRvAdditionalServices.setHasFixedSize(true);
            holder.mRvAdditionalServices.setLayoutManager(mAdditionalLayoutManager);

            if (mAdapter != null) {
                holder.mRvAdditionalServices.setAdapter(mAdapter);
            }

            holder.mRvAdditionalServices.setVisibility(View.VISIBLE);
            holder.mTvAdditionalLabel.setVisibility(View.VISIBLE);
        } else {
            holder.mRvAdditionalServices.setVisibility(View.GONE);
            holder.mTvAdditionalLabel.setVisibility(View.GONE);
        }

        holder.mIBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onServiceActionListener.onAction(position, AppConstants.CANCEL_SERVICE);
            }
        });

        holder.mBtnReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onServiceActionListener.onAction(position, AppConstants.RESCHEDULE_SERVICE);
            }
        });

        Glide.with(mContext)
                .load(service.professionalAvatar.url)
                .centerCrop()
                .error(R.drawable.ic_menu_user)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mCivProfessionalPhoto);
    }

    @Override
    public int getItemCount() {
        return mServicesList.size();
    }

    public class AgendaViewHolder extends RecyclerView.ViewHolder {

        TextView mTvBusinessName;
        TextView mTvServiceName;
        TextView mTvServiceTime;
        TextView mTvServicePrice;
        TextView mTvProfessionalName;
        ImageView mCivProfessionalPhoto;
        TextView mTvAdditionalLabel;
        RecyclerView mRvAdditionalServices;
        ImageButton mIBtnCancel;
        Button mBtnReschedule;


        public AgendaViewHolder(View view) {
            super(view);

            mTvBusinessName = (TextView) view.findViewById(R.id.business_name);
            mTvServiceName = (TextView) view.findViewById(R.id.service_name);
            mTvServiceTime = (TextView) view.findViewById(R.id.service_time);
            mTvServicePrice = (TextView) view.findViewById(R.id.service_price);
            mTvProfessionalName = (TextView) view.findViewById(R.id.professional_name);
            mCivProfessionalPhoto = (ImageView) view.findViewById(R.id.professional_photo);
            mTvAdditionalLabel = (TextView) view.findViewById(R.id.additional_label);
            mRvAdditionalServices = (RecyclerView) view.findViewById(R.id.additional_services);
            mIBtnCancel = (ImageButton) view.findViewById(R.id.cancel_button);
            mBtnReschedule = (Button) view.findViewById(R.id.reschedule_button);
        }
    }

    public interface OnServiceActionListener {
        void onAction(int position, int action);
    }

}
