package com.petbooking.UI.Widget.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petbooking.R;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class HoursListAdapter extends RecyclerView.Adapter<HoursListAdapter.HourViewHolder> {

    private Context mContext;
    private ArrayList<String> mHoursList;
    private RecyclerView mRvHours;

    public HoursListAdapter(Context context, ArrayList<String> hoursList) {
        this.mContext = context;
        this.mHoursList = hoursList;
    }

    public void updateList(ArrayList<String> hoursList) {
        this.mHoursList = hoursList;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_hours, parent, false);

        HourViewHolder holder = new HourViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final HourViewHolder holder, int position) {


        final GradientDrawable dayBackground = (GradientDrawable) holder.mTvHour.getBackground();

        dayBackground.setColor(Color.TRANSPARENT);
        holder.mTvHour.setText(mHoursList.get(position));

        holder.mTvHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayBackground.setColor(mContext.getResources().getColor(R.color.brand_primary));
                holder.mTvHour.setTextColor(Color.WHITE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHoursList.size();
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvHour;

        public HourViewHolder(View view) {
            super(view);

            mTvHour = (TextView) view.findViewById(R.id.hour);
        }
    }

}
