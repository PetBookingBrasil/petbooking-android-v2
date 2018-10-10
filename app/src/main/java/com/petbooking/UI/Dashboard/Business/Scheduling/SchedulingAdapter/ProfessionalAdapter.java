package com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingAdapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petbooking.API.Appointment.Models.ProfessionalResp;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Models.AppointmentDate;
import com.petbooking.Models.Professional;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Scheduling.SchedulingFragment;
import com.petbooking.UI.Dashboard.Business.Scheduling.model.AppointmentDateChild;
import com.petbooking.UI.Dashboard.Business.Scheduling.model.AppointmentDateSlot;
import com.petbooking.UI.Dashboard.Business.Scheduling.widget.MyLinearLayout;
import com.petbooking.UI.Dashboard.Business.ServiceDetail.DateListAdapter;
import com.petbooking.UI.Widget.CircleTransformation;
import com.petbooking.Utils.APIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by victorneves on 05/12/17.
 */

public class ProfessionalAdapter extends StatelessSection {
    List<Professional> professionals;
    Context context;
    boolean expanded = false;
    String title;
    DateListDayAdapter dateListDayAdapter;
    AppointmentDate appointmentDate = null;
    Professional professional = null;
    RecyclerView datesRecycler;
    OnProfessionalSelected onProfessionalSelected;
    SchedulingFragment fragment;
    private List<AppointmentDateChild> days;
    List<AppointmentDateSlot> appointmentDateSlots;

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

    public ProfessionalAdapter(Context context, List<Professional> professionals, String title) {
        super(new SectionParameters.Builder(R.layout.custom_professional_adapter)
                .headerResourceId(R.layout.header_scheduling)
                .build());
        this.professionals = professionals;
        this.context = context;
        this.title = title;
    }

    public void setProfessionals(List<Professional> professionals) {
        this.professionals = professionals;
    }

    public void setFragment(SchedulingFragment fragment) {
        this.fragment = fragment;
    }

    public void setexpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void setOnProfessionalSelected(OnProfessionalSelected onProfessionalSelected) {
        this.onProfessionalSelected = onProfessionalSelected;
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
        holder.professionalList.setLayoutManager(new GridLayoutManager(context, 3));
        //SnapHelper snapHelper = new PagerSnapHelper();
        //holder.professionalList.setOnFlingListener(null);
        //snapHelper.attachToRecyclerView(holder.professionalList);
        holder.professionalList.setClipToPadding(false);
        holder.professionalList.setAdapter(new ProfessionalListAdapter(context, professionals));

        List<AppointmentDateChild> childs = new ArrayList<>();
        dateListDayAdapter = new DateListDayAdapter(context, childs);
        MyLinearLayout managerDate = new MyLinearLayout(context, LinearLayoutManager.VERTICAL, false);
        holder.datesAvaliableList.setLayoutManager(managerDate);
        holder.datesAvaliableList.addOnItemTouchListener(mScrollTouchListener);
        holder.datesAvaliableList.setAdapter(dateListDayAdapter);
        SnapHelper snapHelperLinear = new LinearSnapHelper();
        holder.datesAvaliableList.setOnFlingListener(null);
        snapHelperLinear.attachToRecyclerView(holder.datesAvaliableList);
        holder.datesAvaliableList.setClipToPadding(false);
    }

    public void setDays(List<AppointmentDateSlot> days) {
        this.appointmentDateSlots = days;
    }

    public List<AppointmentDateChild> getDays() {
        return days;
    }

    public List<AppointmentDateSlot> getAppointmentDateSlots() {
        return appointmentDateSlots;
    }

    public void checkProfessional(){
        int i = 0;
        if(professionals.size() > 0){
            for(Professional professional: professionals){
                if(professional.id.equals(this.professional.id)){
                    updateProfissionals(i, true);
                    break;
                }
                i++;
            }
        }
    }

    public void updateProfissionals(final int position, final boolean edit) {
        new AsyncTask<Void, Void, Void>() {
            List<AppointmentDateChild> childs = new ArrayList<>();
            List<AppointmentDateSlot> slots = new ArrayList<>();

            @Override
            protected Void doInBackground(Void... voids) {

                for (int j = 0; j < professionals.get(position).availableDates.size(); j++) {
                    String monthName = "";

                    for (int i = 0; i < professionals.get(position).availableDates.get(j).days.size(); i++) {
                        monthName = professionals.get(position).availableDates.get(j).monthName;
                        Date date = professionals.get(position).availableDates.get(j).days.get(i).date;
                        ArrayList<String> times = professionals.get(position).availableDates.get(j).days.get(i).times;
                        AppointmentDateSlot slot = new AppointmentDateSlot(date,times);

                        slots.add(slot);
                        for (int x = 0; x < professionals.get(position).availableDates.get(j).days.get(i).times.size(); x++) {
                            String time = professionals.get(position).availableDates.get(j).days.get(i).times.get(x);
                            AppointmentDateChild dateChild =
                                    new AppointmentDateChild(j, monthName,
                                            professionals.get(position).availableDates.get(j).days.get(i).date, time);
                            childs.add(dateChild);
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.i(getClass().getSimpleName(), "Qual o professional " + position);
                //dateListDayAdapter.selectedPosition(0);
                //dateListDayAdapter.setDays(childs);
                //dateListDayAdapter.notifyDataSetChanged();
                setDays(slots);
                if(edit) {
                    onProfessionalSelected.selectedProfessional(professionals.get(position), null, null, position,true);
                }else{
                    onProfessionalSelected.selectedProfessional(professionals.get(position), null, null, position,false);
                }

            }
        }.execute();


    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder item) {
        HeaderViewHolder holder = (HeaderViewHolder) item;
        holder.headerTitle.setText(title);
        holder.headerEdit.setVisibility(View.VISIBLE);
        holder.image_header.setVisibility(View.VISIBLE);
        if (professional != null && !expanded) {
            int professionalAvatar;
            if (professional.gender == null || professional.gender.equals(User.GENDER_MALE)) {
                professionalAvatar = R.drawable.ic_placeholder_man;
            } else {
                professionalAvatar = R.drawable.ic_placeholder_woman;
            }

            if (professional.imageUrl == null || professional.imageUrl.contains(APIConstants.FALLBACK_TAG)) {
                holder.image_header.setImageResource(professionalAvatar);
            } else {
                Glide.with(context)
                        .load(APIUtils.getAssetEndpoint(professional.imageUrl))
                        .error(professionalAvatar)
                        .placeholder(professionalAvatar)
                        .bitmapTransform(new CircleTransformation(context))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.image_header);
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.headerSection.setLayoutParams(params);
            holder.image_header.setVisibility(View.VISIBLE);
            holder.headerEdit.setVisibility(View.VISIBLE);
            holder.headerEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.expandedProfessional();
                }
            });
        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            holder.headerSection.setLayoutParams(params);
            holder.image_header.setVisibility(View.GONE);
            holder.textCheckunicode.setText("4");
            holder.textCheckunicode.setVisibility(View.GONE);
            holder.textCheckunicode.setTextColor(ContextCompat.getColor(context, R.color.gray));
            holder.circleImageView.setImageResource(R.color.schedule_background);
            holder.headerEdit.setVisibility(View.GONE);
        }

    }

    public void setTitle(String title) {
        this.title = title;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_professional)
        RecyclerView professionalList;
        @BindView(R.id.recycler_dates)
        RecyclerView datesAvaliableList;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            datesRecycler = datesAvaliableList;
        }
    }

    class ProfessionalListAdapter extends RecyclerView.Adapter<ProfessionalListAdapter.ProfessionalViewHolder> {
        Context context;
        List<Professional> professionals;
        int selectedPosition = -1;
        boolean selected = true;

        public ProfessionalListAdapter(Context context, List<Professional> professionals) {
            this.context = context;
            this.professionals = professionals;
        }

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        @Override
        public ProfessionalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_list_professional, parent, false);
            ProfessionalViewHolder viewHolder = new ProfessionalViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ProfessionalViewHolder holder, final int position) {
            Professional professional = professionals.get(position);
            int professionalAvatar;
            holder.professionalName.setText(professional.name);

            if (professional.gender == null || professional.gender.equals(User.GENDER_MALE)) {
                professionalAvatar = R.drawable.ic_placeholder_man;
            } else {
                professionalAvatar = R.drawable.ic_placeholder_woman;
            }

            if (professional.imageUrl == null || professional.imageUrl.contains(APIConstants.FALLBACK_TAG)) {
                holder.professionalPhoto.setImageResource(professionalAvatar);
            } else {
                Glide.with(context)
                        .load(APIUtils.getAssetEndpoint(professional.imageUrl))
                        .error(professionalAvatar)
                        .placeholder(professionalAvatar)
                        .bitmapTransform(new CircleTransformation(context))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.professionalPhoto);
            }
            holder.professionalPhoto.setAlpha(0.1f);

            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateProfissionals(position,false);
                    ProfessionalAdapter.this.professional = professionals.get(position);
                    setSelectedPosition(position);
                    selected = false;
                    notifyDataSetChanged();
                }
            });

           /* if (position == 0 && selected) {
                setSelectedPosition(position);
                updateProfissionals(position);
                ProfessionalAdapter.this.professional = professionals.get(position);
            }*/

            if (selectedPosition == position) {
                holder.professionalPhoto.setAlpha(1.0f);
                holder.professionalPhoto.setBackground(ContextCompat.getDrawable(context, R.drawable.imageview_border));
            } else {
                holder.professionalPhoto.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_background));
                holder.professionalPhoto.setAlpha(0.1f);
            }

            selected = true;

        }

        @Override
        public int getItemCount() {
            return professionals.size();
        }

        class ProfessionalViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.professional_photo)
            ImageView professionalPhoto;
            @BindView(R.id.professional_name)
            TextView professionalName;
            View v;

            public ProfessionalViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                v = itemView;
            }
        }
    }

    class DateListDayAdapter extends RecyclerView.Adapter<DateListDayAdapter.DateListViewHolder> {
        Context context;
        List<AppointmentDateChild> days;
        int selectPosition = -1;

        public DateListDayAdapter(Context context, List<AppointmentDateChild> days) {
            this.context = context;
            this.days = days;
        }

        public void setDays(List<AppointmentDateChild> days) {
            this.days = days;
        }

        public void selectedPosition(int position) {
            this.selectPosition = position;
        }

        @Override
        public DateListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_list_date_available, parent, false);
            DateListViewHolder vh = new DateListViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(final DateListViewHolder holder, final int position) {
            final AppointmentDateChild date = days.get(position);
            holder.dateAvaliable.setText(date.getDayDescription() + ", " + date.getDayNumber() + " de " + date.getMonthDescription().substring(0, 3));
            holder.hourAvaliable.setText(date.Time);

            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ProfessionalAdapter.this.professional != null) {
                        selectedPosition(position);
                        notifyDataSetChanged();
                    }
                }
            });

            if (selectPosition == position) {
                holder.dateAvaliable.setTextColor(ContextCompat.getColor(context, R.color.brand_primary));
                holder.hourAvaliable.setTextColor(ContextCompat.getColor(context, R.color.brand_primary));
                appointmentDate = professional.availableDates.get(date.position);
                onProfessionalSelected.selectedProfessional(professional, appointmentDate, date,position,false);
            } else {
                holder.dateAvaliable.setTextColor(ContextCompat.getColor(context, R.color.text_gray));
                holder.hourAvaliable.setTextColor(ContextCompat.getColor(context, R.color.text_gray));
            }
        }

        @Override
        public int getItemCount() {
            return days.size();
        }

        class DateListViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.separator_line_top)
            View separatorTop;
            @BindView(R.id.date_avaliable)
            TextView dateAvaliable;
            @BindView(R.id.hour_avaliable)
            TextView hourAvaliable;
            View v;

            public DateListViewHolder(View itemView) {
                super(itemView);
                v = itemView;
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public interface OnProfessionalSelected {
        void selectedProfessional(Professional professional, AppointmentDate appointmentDate, AppointmentDateChild chil,int position, boolean edit);
    }
}
