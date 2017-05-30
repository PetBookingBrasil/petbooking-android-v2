package com.petbooking.UI.Widget.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
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


public class DaysListAdapter extends RecyclerView.Adapter<DaysListAdapter.DaysViewHolder> {

    private Context mContext;
    private ArrayList<String> mDaysList;

    /**
     * Hours List
     */
    private ArrayList<String> mHoursList;
    private HoursListAdapter mHourAdapter;
    private LinearLayoutManager mLayoutManager;

    public DaysListAdapter(Context context, ArrayList<String> daysList) {
        this.mDaysList = daysList;
        this.mContext = context;
        this.mHoursList = daysList;
    }

    public void updateList(ArrayList<String> daysList) {
        this.mDaysList = daysList;
    }

    @Override
    public DaysViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_days, parent, false);

        DaysViewHolder holder = new DaysViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final DaysViewHolder holder, int position) {

        GradientDrawable dayBackground = (GradientDrawable) holder.mTvDay.getBackground();

        mHourAdapter = new HoursListAdapter(mContext, mHoursList);

        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        holder.mRvHours.setHasFixedSize(true);
        holder.mRvHours.setLayoutManager(mLayoutManager);

        if (mHourAdapter != null) {
            holder.mRvHours.setAdapter(mHourAdapter);
        }

        if (position == 5) {
            dayBackground.setColor(Color.WHITE);
            holder.mTvDay.setTextColor(mContext.getResources().getColor(R.color.brand_primary));
        } else {
            dayBackground.setColor(mContext.getResources().getColor(R.color.brand_primary));
            holder.mTvDay.setTextColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return mDaysList.size();
    }

    public class DaysViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvDayName;
        public TextView mTvDay;
        public RecyclerView mRvHours;

        public DaysViewHolder(View view) {
            super(view);

            mTvDayName = (TextView) view.findViewById(R.id.day_name);
            mTvDay = (TextView) view.findViewById(R.id.day);
            mRvHours = (RecyclerView) view.findViewById(R.id.hours_list);
        }
    }

}
