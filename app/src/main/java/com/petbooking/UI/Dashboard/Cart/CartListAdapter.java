package com.petbooking.UI.Dashboard.Cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Models.CartItem;
import com.petbooking.R;
import com.petbooking.Utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Luciano José on 18/08/2017.
 */


public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartViewHolder> {

    private SimpleDateFormat mDateFormat;
    private SimpleDateFormat mTimeFormat;
    private Context mContext;
    private ArrayList<CartItem> mCartList;

    public CartListAdapter(Context context, ArrayList<CartItem> mCartList) {
        this.mContext = context;
        this.mCartList = mCartList;
        this.mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.mTimeFormat = new SimpleDateFormat("HH:mm");
    }

    public void updateList(ArrayList<CartItem> cartList) {
        this.mCartList = cartList;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_cart, parent, false);

        CartViewHolder holder = new CartViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, final int position) {
        CartItem cartItem = mCartList.get(position);

        Calendar calendar = new GregorianCalendar();
        Calendar timeCalendar = Calendar.getInstance();
        String price = mContext.getResources().getString(R.string.business_service_price,
                String.format("%.2f", cartItem.service.price));
        String totalPrice = mContext.getResources().getString(R.string.business_service_price,
                String.format("%.2f", cartItem.totalPrice));
        String appointmentDate;

        try {
            calendar.setTime(mDateFormat.parse(cartItem.startDate));
            timeCalendar.setTime(mTimeFormat.parse(cartItem.startTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeCalendar.add(Calendar.MINUTE, (cartItem.service.duration / 60));
        appointmentDate = mContext.getResources().getString(R.string.appointment_date_info, String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),
                CommonUtils.uppercaseFirstLetter(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())),
                cartItem.startTime, mTimeFormat.format(timeCalendar.getTime()));

        holder.mTvPetName.setText(cartItem.pet.name);
        holder.mTvProfessionalName.setText(cartItem.professional.name);
        holder.mTvIndex.setText("#" + (position + 1));
        holder.mTvDateInfo.setText(appointmentDate);
        holder.mTvServiceName.setText(cartItem.service.name);
        holder.mTvServicePrice.setText(price);
        holder.mTvTotalPrice.setText(totalPrice);

        //TODO: Apresentar Adicionais
        if (cartItem.additionalServices.size() == 0) {
            holder.mAdditionalLayout.setVisibility(View.GONE);
        } else {
            holder.mAdditionalLayout.setVisibility(View.VISIBLE);
        }

        Glide.with(mContext)
                .load(cartItem.pet.avatar.url)
                .error(R.drawable.ic_placeholder_dog)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mIvPetPhoto);

        Glide.with(mContext)
                .load(cartItem.professional.imageUrl)
                .error(R.drawable.ic_placeholder_user)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mIvProfessionalPhoto);
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mIvPetPhoto;
        TextView mTvPetName;
        TextView mTvIndex;
        TextView mTvDateInfo;
        TextView mTvServiceName;
        TextView mTvServicePrice;
        RelativeLayout mAdditionalLayout;
        RecyclerView mRvAdditional;
        CircleImageView mIvProfessionalPhoto;
        TextView mTvProfessionalName;
        EditText mEdtNotes;
        Button mBtnEdit;
        ImageButton mBtnRemove;
        TextView mTvTotalPrice;

        public CartViewHolder(View view) {
            super(view);

            mIvPetPhoto = (CircleImageView) view.findViewById(R.id.pet_photo);
            mTvPetName = (TextView) view.findViewById(R.id.pet_name);
            mTvIndex = (TextView) view.findViewById(R.id.index_label);
            mTvDateInfo = (TextView) view.findViewById(R.id.date_info);
            mTvServiceName = (TextView) view.findViewById(R.id.service_name);
            mTvServicePrice = (TextView) view.findViewById(R.id.service_price);
            mAdditionalLayout = (RelativeLayout) view.findViewById(R.id.additional_layout);
            mRvAdditional = (RecyclerView) view.findViewById(R.id.additional_list);
            mIvProfessionalPhoto = (CircleImageView) view.findViewById(R.id.professional_photo);
            mTvProfessionalName = (TextView) view.findViewById(R.id.professional_name);
            mEdtNotes = (EditText) view.findViewById(R.id.service_notes);
            mBtnEdit = (Button) view.findViewById(R.id.edit_button);
            mBtnRemove = (ImageButton) view.findViewById(R.id.remove_button);
            mTvTotalPrice = (TextView) view.findViewById(R.id.total_price);
        }
    }
}
