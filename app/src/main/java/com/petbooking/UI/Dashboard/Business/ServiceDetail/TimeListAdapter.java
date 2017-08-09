package com.petbooking.UI.Dashboard.Business.ServiceDetail;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
public class TimeListAdapter extends RecyclerView.Adapter<TimeListAdapter.TimeViewHolder> {

    private int selectedPosition = -1;
    private ArrayList<String> mTimeList;
    private OnSelectTimeListener timeListener;
    private Context mContext;

    public TimeListAdapter(Context context, ArrayList<String> timeList, OnSelectTimeListener timeListener) {
        this.mContext = context;
        this.mTimeList = timeList;
        this.timeListener = timeListener;
    }

    public void updateList(ArrayList<String> timeList) {
        this.mTimeList = timeList;
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_appointment_time, parent, false);

        TimeViewHolder holder = new TimeViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final TimeViewHolder holder, final int position) {
        final String time = mTimeList.get(position);

        int redColor = mContext.getResources().getColor(R.color.brand_primary);
        int whiteColor = mContext.getResources().getColor(R.color.white);

        GradientDrawable background = (GradientDrawable) holder.mTvTime.getBackground();

        if (position == selectedPosition) {
            holder.mTvTime.setTextColor(whiteColor);
            holder.mTvTime.setTypeface(Typeface.DEFAULT_BOLD);
            background.setColor(redColor);
            background.setStroke(2, redColor);
        } else {
            holder.mTvTime.setTextColor(mContext.getResources().getColor(R.color.text_color));
            holder.mTvTime.setTypeface(Typeface.DEFAULT);
            background.setStroke(2, Color.GRAY);
            background.setColor(whiteColor);
        }

        holder.mTvTime.setText(time);
        holder.mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPosition);
                selectedPosition = position;
                notifyItemChanged(selectedPosition);
                timeListener.onSelect(position);
            }
        });
    }

    /**
     * Reset Time
     */
    public void resetTime() {
        selectedPosition = -1;
        notifyItemChanged(selectedPosition);
        timeListener.onSelect(selectedPosition);
    }

    @Override
    public int getItemCount() {
        return mTimeList.size();
    }


    public class TimeViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvTime;

        public TimeViewHolder(View view) {
            super(view);

            mTvTime = (TextView) view.findViewById(R.id.time);
        }
    }

    public interface OnSelectTimeListener {
        void onSelect(int position);
    }
}
