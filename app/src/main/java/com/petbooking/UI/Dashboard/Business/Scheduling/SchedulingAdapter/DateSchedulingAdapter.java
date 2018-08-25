package com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Models.CartItem;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Scheduling.model.AppointmentDateSlot;
import com.petbooking.UI.Dashboard.Business.Scheduling.widget.MyLinearLayout;
import com.petbooking.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Victor on 25/02/18.
 */

public class DateSchedulingAdapter extends StatelessSection {
    private AppointmentManager mAppointmentManager;
    private boolean expanded;
    List<AppointmentDateSlot> days;
    Context context;
    AppointmentDateSlot dateSlot;
    int selectedHour = 0;
    HourListAdapter hourListAdapter;
    OnDateSelected onDateSelected;

    RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            int action = e.getAction();
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    rv.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };


    public void setOnDateSelected(OnDateSelected onDateSelected) {
        this.onDateSelected = onDateSelected;
    }

    public DateSchedulingAdapter(Context context, List<AppointmentDateSlot> days) {
        super(new SectionParameters.Builder(R.layout.item_date_adapter)
                .headerResourceId(R.layout.header_scheduling)
                .build());
        this.context = context;
        this.days = days;
        this.dateSlot = new AppointmentDateSlot(new Date(), new ArrayList<String>());
        mAppointmentManager = AppointmentManager.getInstance();

    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void setDays(List<AppointmentDateSlot> days) {
        this.days = days;
    }

    public List<AppointmentDateSlot> getDays() {
        return days;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder item, int position) {
        ItemViewHolder holder = (ItemViewHolder) item;
        holder.date.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        holder.date.setAdapter(new DateListDayAdapter());
        MyLinearLayout managerDate = new MyLinearLayout(context, LinearLayoutManager.VERTICAL, false);
        holder.hours.setLayoutManager(managerDate);
        holder.hours.addOnItemTouchListener(mScrollTouchListener);
        hourListAdapter = new HourListAdapter(dateSlot.timer);
        holder.hours.setAdapter(hourListAdapter);
        String monthDate = CommonUtils.formatDate(CommonUtils.MONTH_DESCRIPTION_FORMAT, days.get(0).date).toUpperCase();
        String yearFormat = CommonUtils.formatDate(CommonUtils.YEAR_FORMAT, days.get(0).date).toUpperCase();
        holder.month.setText(monthDate + " " + yearFormat);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder item) {
        HeaderViewHolder holder = (HeaderViewHolder) item;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        holder.headerSection.setLayoutParams(params);
        holder.image_header.setVisibility(View.GONE);
        holder.textCheckunicode.setText("5");
        holder.textCheckunicode.setVisibility(View.VISIBLE);
        holder.textCheckunicode.setTextColor(ContextCompat.getColor(context, R.color.gray));
        holder.circleImageView.setImageResource(R.color.schedule_background);
        holder.headerEdit.setVisibility(View.GONE);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.month)
        TextView month;
        @BindView(R.id.recycler_date)
        RecyclerView date;
        @BindView(R.id.text_title)
        TextView textTitle;
        @BindView(R.id.recycler_hour)
        RecyclerView hours;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DateListDayAdapter extends RecyclerView.Adapter<DateListViewHolder> {
        int selectedPosition = -1;

        @Override
        public DateListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_list_date, parent, false);
            DateListViewHolder vh = new DateListViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(DateListViewHolder holder, final int position) {
            final AppointmentDateSlot date = days.get(position);

            if (selectedPosition == -1) {
                selectedPosition = 0;
                dateSlot = date;
                hourListAdapter.setTimers(date.timer,date.date);
                hourListAdapter.notifyDataSetChanged();
            }
            holder.textDay.setText(CommonUtils.formatDate(CommonUtils.DAY_FORMAT_DESCRIOTION, date.date).toUpperCase());
            holder.textDayNumber.setText(CommonUtils.formatDate(CommonUtils.DAY_FORMAT, date.date));
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = position;
                    hourListAdapter.setTimers(date.timer,date.date);
                    hourListAdapter.setSelectedDate(-1);
                    hourListAdapter.notifyDataSetChanged();
                    dateSlot = date;
                    notifyDataSetChanged();

                }
            });

            if (selectedPosition == position) {
                holder.layoutDate.setBackground(ContextCompat.getDrawable(context, R.drawable.background_date_red));
                holder.textDay.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.textDayNumber.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.dayPoynt.setVisibility(View.VISIBLE);
            } else {
                holder.layoutDate.setBackground(ContextCompat.getDrawable(context, R.drawable.background_date));
                holder.textDay.setTextColor(ContextCompat.getColor(context, R.color.text_color));
                holder.textDayNumber.setTextColor(ContextCompat.getColor(context, R.color.text_color));
                holder.dayPoynt.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return days.size();
        }
    }

    class HourListAdapter extends RecyclerView.Adapter<HourListViewHolder> {
        ArrayList<String> timers;
        int selectedDate = -1;

        public HourListAdapter(ArrayList<String> times) {
            this.timers = times;
        }

        @Override
        public HourListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_list_date_available, parent, false);
            return new HourListViewHolder(view);
        }

        public void setTimers(ArrayList<String> timers,Date date) {
            ArrayList<String> newTimers = new ArrayList<>();
            for (int i = 0; i < timers.size(); i++) {
                if (!hasDate(CommonUtils.formatDate(CommonUtils.DATEFORMATDEFAULT, date), timers.get(i))) {
                    newTimers.add(timers.get(i));
                }
            }
            this.timers = newTimers;
        }

        @Override
        public void onBindViewHolder(HourListViewHolder holder, final int position) {
            String timers = this.timers.get(position);
            holder.hourAvaliable.setText(timers);
            if (selectedDate == -1) {
                selectedDate = 0;
            }
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedDate = position;
                    onDateSelected.onSelectDate(dateSlot, selectedDate);
                    notifyDataSetChanged();
                }
            });


            if (selectedDate == position) {
                holder.hourAvaliable.setTextColor(ContextCompat.getColor(context, R.color.brand_primary));
                DateSchedulingAdapter.this.onDateSelected.onSelectDate(dateSlot, selectedDate);
                holder.itemHourLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.item_hour_background_brand));

            } else {
                holder.hourAvaliable.setTextColor(ContextCompat.getColor(context, R.color.text_color));
                holder.itemHourLayout.setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent));
            }
        }

        @Override
        public int getItemCount() {
            return timers.size();
        }

        public void setSelectedDate(int selectedDate) {
            this.selectedDate = selectedDate;
        }
    }

    class DateListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layoutDate)
        LinearLayout layoutDate;
        @BindView(R.id.textDay)
        TextView textDay;
        @BindView(R.id.textDayNumber)
        TextView textDayNumber;

        @BindView(R.id.textDayPoint)
        TextView dayPoynt;

        View v;

        public DateListViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    class HourListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.separator_line_top)
        View separatorTop;
        @BindView(R.id.date_avaliable)
        TextView dateAvaliable;
        @BindView(R.id.hour_avaliable)
        TextView hourAvaliable;
        @BindView(R.id.item_hour_layout)
        LinearLayout itemHourLayout;
        View v;

        public HourListViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            ButterKnife.bind(this, itemView);
        }

    }

    public interface OnDateSelected {
        void onSelectDate(AppointmentDateSlot appointmentDateSlot, int selectedPosition);
    }

    private boolean hasDate(String date, String timer) {
        ArrayList<CartItem> cartItems = mAppointmentManager.getCart();
        if (cartItems != null) {
            for (int i = 0; i < cartItems.size(); i++) {
                if (date.equals(cartItems.get(i).startDate) && timer.equals(cartItems.get(i).startTime)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
}
