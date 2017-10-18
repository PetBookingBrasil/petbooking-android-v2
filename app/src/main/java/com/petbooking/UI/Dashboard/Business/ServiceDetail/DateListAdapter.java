package com.petbooking.UI.Dashboard.Business.ServiceDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petbooking.Models.AppointmentDate;
import com.petbooking.R;

import java.util.ArrayList;

/**
 * Created by Luciano José on 23/05/2017.
 */
public class DateListAdapter extends RecyclerView.Adapter<DateListAdapter.DateViewHolder> {

    private ArrayList<AppointmentDate> mAppointmentDateList;
    private Context mContext;

    public DateListAdapter(Context context, ArrayList<AppointmentDate> appointmentDateList) {
        this.mContext = context;
        this.mAppointmentDateList = appointmentDateList;
    }

    public void updateList(ArrayList<AppointmentDate> appointmentDateList) {
        this.mAppointmentDateList = appointmentDateList;
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_appointment_date, parent, false);

        DateViewHolder holder = new DateViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final DateViewHolder holder, final int position) {
        final AppointmentDate appointmentDate = mAppointmentDateList.get(position);

        holder.mTvDate.setText(appointmentDate.monthName + " " + appointmentDate.year);
    }

    @Override
    public int getItemCount() {
        return mAppointmentDateList.size();
    }


    public class DateViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvDate;

        public DateViewHolder(View view) {
            super(view);

            mTvDate = (TextView) view.findViewById(R.id.date);
        }
    }
}
