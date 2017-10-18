package com.petbooking.UI.Dashboard.Business.ServiceDetail;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petbooking.API.Appointment.Models.ProfessionalResp;
import com.petbooking.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Luciano José on 23/05/2017.
 */
public class DaysListAdapter extends RecyclerView.Adapter<DaysListAdapter.DayViewHolder> {

    private int selectedPosition = -1;
    private OnSelectDayListener onSelectDayListener;
    private ArrayList<ProfessionalResp.Slot> mDayList;
    private Context mContext;

    public DaysListAdapter(Context context, ArrayList<ProfessionalResp.Slot> dayList, OnSelectDayListener onSelectDayListener) {
        this.mContext = context;
        this.mDayList = dayList;
        this.onSelectDayListener = onSelectDayListener;
    }

    public void updateList(ArrayList<ProfessionalResp.Slot> dayList) {
        this.mDayList = dayList;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_appointment_day, parent, false);

        DayViewHolder holder = new DayViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final DayViewHolder holder, final int position) {
        final ProfessionalResp.Slot slot = mDayList.get(position);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(slot.date);

        holder.mTvDate.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        holder.mTvDayName.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));

        if (selectedPosition == position) {
            holder.mTvDate.setBackground(mContext.getResources().getDrawable(R.drawable.selected_day_background));
            holder.mTvDate.setTypeface(Typeface.DEFAULT_BOLD);
            holder.mTvDayName.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.mTvDate.setTypeface(Typeface.DEFAULT);
            holder.mTvDate.setBackground(null);
            holder.mTvDayName.setTypeface(Typeface.DEFAULT);
        }

        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDay(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDayList.size();
    }

    /**
     * Select Day
     *
     * @param position
     */
    public void selectDay(int position) {
        notifyItemChanged(selectedPosition);
        selectedPosition = position;
        notifyItemChanged(selectedPosition);
        onSelectDayListener.onSelect(selectedPosition);
    }

    /**
     * Reset Selected day
     */
    public void resetDay(){
        this.selectedPosition = -1;
        notifyItemChanged(selectedPosition);
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mItemLayout;
        public TextView mTvDayName;
        public TextView mTvDate;

        public DayViewHolder(View view) {
            super(view);

            mItemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
            mTvDayName = (TextView) view.findViewById(R.id.day_name);
            mTvDate = (TextView) view.findViewById(R.id.day);
        }
    }

    public interface OnSelectDayListener {
        void onSelect(int position);
    }
}
