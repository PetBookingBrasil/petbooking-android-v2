package com.petbooking.UI.Dashboard.Cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Models.BusinessServices;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class CartAdditionalServiceListAdapter extends RecyclerView.Adapter<CartAdditionalServiceListAdapter.AdditionalViewHolder> {

    private int parentPosition;
    private AppointmentManager mAppointmentManager;
    private CartListAdapter.OnCartChange onCartChange;
    private ArrayList<BusinessServices> mServiceList;
    private Context mContext;

    public CartAdditionalServiceListAdapter(Context context, ArrayList<BusinessServices> serviceList, CartListAdapter.OnCartChange onCartChange) {
        this.mServiceList = serviceList;
        this.mContext = context;
        this.mAppointmentManager = AppointmentManager.getInstance();
        this.onCartChange = onCartChange;
    }

    public void updateList(ArrayList<BusinessServices> serviceList) {
        this.mServiceList = serviceList;
    }

    @Override
    public AdditionalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_cart_additional_service, parent, false);

        AdditionalViewHolder holder = new AdditionalViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final AdditionalViewHolder holder, final int position) {
        final BusinessServices service = mServiceList.get(position);

        String price = mContext.getResources().getString(R.string.business_service_price, String.format("%.2f", service.price));

        holder.mTvAdditionalName.setText(service.name);
        holder.mTvAdditionalPrice.setText(price);

        holder.mIBtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppointmentManager.removeAdditionalByIndex(parentPosition, position);
                onCartChange.onChange();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mServiceList.size();
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public class AdditionalViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvAdditionalName;
        public TextView mTvAdditionalPrice;
        public ImageButton mIBtnRemove;

        public AdditionalViewHolder(View view) {
            super(view);

            mTvAdditionalName = (TextView) view.findViewById(R.id.additional_name);
            mTvAdditionalPrice = (TextView) view.findViewById(R.id.additional_price);
            mIBtnRemove = (ImageButton) view.findViewById(R.id.remove_button);
        }
    }

}
