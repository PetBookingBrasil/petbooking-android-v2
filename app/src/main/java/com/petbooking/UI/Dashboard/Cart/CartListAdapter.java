package com.petbooking.UI.Dashboard.Cart;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Models.CartItem;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Widget.CircleTransformation;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Luciano José on 18/08/2017.
 */


public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartViewHolder> {

    private SimpleDateFormat mDateFormat;
    private SimpleDateFormat mTimeFormat;
    private AppointmentManager mAppointmentManager;
    private OnCartChange onCartChange;
    private Context mContext;
    private ArrayList<CartItem> mCartList;

    public CartListAdapter(Context context, ArrayList<CartItem> mCartList, OnCartChange onCartChange) {
        this.mContext = context;
        this.mCartList = mCartList;
        this.mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.mTimeFormat = new SimpleDateFormat("HH:mm");
        this.mAppointmentManager = AppointmentManager.getInstance();
        this.onCartChange = onCartChange;
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
        final CartItem cartItem = mCartList.get(position);

        CartAdditionalServiceListAdapter mAdditionalAdapter;
        LinearLayoutManager mAdditionalLayout;
        Calendar calendar = new GregorianCalendar();
        Calendar timeCalendar = Calendar.getInstance();
        String price = mContext.getResources().getString(R.string.business_service_price,
                String.format("%.2f", cartItem.service.price));
        String totalPrice = mContext.getResources().getString(R.string.business_service_price,
                String.format("%.2f", cartItem.totalPrice));
        String appointmentDate;
        int professionalAvatar;
        int petAvatar;

        if (cartItem.professional.gender == null || cartItem.professional.gender.equals(User.GENDER_MALE)) {
            professionalAvatar = R.drawable.ic_placeholder_man;
        } else {
            professionalAvatar = R.drawable.ic_placeholder_woman;
        }

        if (cartItem.pet.type != null && cartItem.pet.type.equals("dog")) {
            petAvatar = R.drawable.ic_placeholder_dog;
        } else {
            petAvatar = R.drawable.ic_placeholder_cat;
        }

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

        if (cartItem.additionalServices.size() == 0) {
            holder.mAdditionalLayout.setVisibility(View.GONE);
        } else {
            holder.mAdditionalLayout.setVisibility(View.VISIBLE);

            mAdditionalAdapter = new CartAdditionalServiceListAdapter(mContext, cartItem.additionalServices, onCartChange);
            mAdditionalAdapter.setParentPosition(position);

            mAdditionalLayout = new LinearLayoutManager(mContext);
            mAdditionalLayout.setOrientation(LinearLayoutManager.VERTICAL);

            holder.mRvAdditional.setLayoutManager(mAdditionalLayout);
            holder.mRvAdditional.setAdapter(mAdditionalAdapter);
        }

        holder.mBtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppointmentManager.removeItemByIndex(position);
                onCartChange.onChange();
            }
        });

        holder.mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartChange.onEditItem(cartItem, position);
            }
        });

        holder.mEdtNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cartItem.notes = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Glide.with(mContext)
                .load(APIUtils.getAssetEndpoint(cartItem.pet.avatar.url))
                .error(petAvatar)
                .placeholder(petAvatar)
                .bitmapTransform(new CircleTransformation(mContext))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mIvPetPhoto);

        if (cartItem.professional.imageUrl == null || cartItem.professional.imageUrl.contains(APIConstants.FALLBACK_TAG)) {
            holder.mIvProfessionalPhoto.setImageResource(professionalAvatar);
        } else {
            Glide.with(mContext)
                    .load(APIUtils.getAssetEndpoint(cartItem.professional.imageUrl))
                    .error(professionalAvatar)
                    .placeholder(professionalAvatar)
                    .bitmapTransform(new CircleTransformation(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.mIvProfessionalPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        public ImageView mIvPetPhoto;
        public TextView mTvPetName;
        public TextView mTvIndex;
        public TextView mTvDateInfo;
        public TextView mTvServiceName;
        public TextView mTvServicePrice;
        public RelativeLayout mAdditionalLayout;
        public RecyclerView mRvAdditional;
        public ImageView mIvProfessionalPhoto;
        public TextView mTvProfessionalName;
        public EditText mEdtNotes;
        public Button mBtnEdit;
        public ImageButton mBtnRemove;
        public TextView mTvTotalPrice;

        public CartViewHolder(View view) {
            super(view);

            mIvPetPhoto = (ImageView) view.findViewById(R.id.pet_photo);
            mTvPetName = (TextView) view.findViewById(R.id.pet_name);
            mTvIndex = (TextView) view.findViewById(R.id.index_label);
            mTvDateInfo = (TextView) view.findViewById(R.id.date_info);
            mTvServiceName = (TextView) view.findViewById(R.id.service_name);
            mTvServicePrice = (TextView) view.findViewById(R.id.service_price);
            mAdditionalLayout = (RelativeLayout) view.findViewById(R.id.additional_layout);
            mRvAdditional = (RecyclerView) view.findViewById(R.id.additional_list);
            mIvProfessionalPhoto = (ImageView) view.findViewById(R.id.professional_photo);
            mTvProfessionalName = (TextView) view.findViewById(R.id.professional_name);
            mEdtNotes = (EditText) view.findViewById(R.id.service_notes);
            mBtnEdit = (Button) view.findViewById(R.id.edit_button);
            mBtnRemove = (ImageButton) view.findViewById(R.id.remove_button);
            mTvTotalPrice = (TextView) view.findViewById(R.id.total_price);
        }
    }

    public interface OnCartChange {
        void onChange();

        void onEditItem(CartItem cartItem,int position);
    }
}
