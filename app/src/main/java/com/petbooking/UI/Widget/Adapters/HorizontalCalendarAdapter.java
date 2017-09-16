package com.petbooking.UI.Widget.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petbooking.Models.CalendarItem;
import com.petbooking.R;
import com.petbooking.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Luciano José on 23/05/2017.
 */


public class HorizontalCalendarAdapter extends RecyclerView.Adapter<HorizontalCalendarAdapter.HorizontalCalendarHolder> {

    private Context mContext;
    private Date today;
    private ArrayList<CalendarItem> mDaysList;

    public HorizontalCalendarAdapter(Context context, ArrayList<CalendarItem> daysList) {
        this.mDaysList = daysList;
        this.mContext = context;
        this.today = new Date();
    }

    public void updateList(ArrayList<CalendarItem> daysList) {
        this.mDaysList = daysList;
    }

    @Override
    public HorizontalCalendarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_calendar, parent, false);

        HorizontalCalendarHolder holder = new HorizontalCalendarHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final HorizontalCalendarHolder holder, int position) {

        CalendarItem date = mDaysList.get(position);

        holder.mTvDay.setText(CommonUtils.formatDay(date.day));
        holder.mTvDayName.setText(CommonUtils.formatDayName(date.dayName));
        holder.mTvMonth.setText(date.monthName + ", " + date.year);

        if (date.date.before(today)) {
            holder.mTvDay.setTextColor(Color.WHITE);
            holder.mTvDay.setBackground(mContext.getResources().getDrawable(R.drawable.past_day_background));
        } else if (date.date.after(today)) {
            holder.mTvDay.setTextColor(mContext.getResources().getColor(R.color.brand_primary));
            holder.mTvDay.setBackground(mContext.getResources().getDrawable(R.drawable.future_day_background));
        }
    }

    @Override
    public int getItemCount() {
        return mDaysList.size();
    }

    public class HorizontalCalendarHolder extends RecyclerView.ViewHolder {

        public TextView mTvDay;
        public TextView mTvDayName;
        public TextView mTvMonth;

        public HorizontalCalendarHolder(View view) {
            super(view);

            mTvDay = (TextView) view.findViewById(R.id.day_number);
            mTvDayName = (TextView) view.findViewById(R.id.day_name);
            mTvMonth = (TextView) view.findViewById(R.id.month);
        }
    }

}
