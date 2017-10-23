package com.petbooking.UI.Dashboard.Business.Schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleItem;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleSection;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

final class ScheduleAdapter extends ExpandableRecyclerViewAdapter<ScheduleAdapter.SchedulingGroupViewHolder, ScheduleAdapter.SchedulingGroupItemViewHolder> {

    ScheduleAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    //region - ExpandableRecyclerViewAdapter

    @Override
    public SchedulingGroupViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_schedule_section, viewGroup, false);
        return new SchedulingGroupViewHolder(view);
    }

    @Override
    public SchedulingGroupItemViewHolder onCreateChildViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_schedule_item, viewGroup, false);
        return new SchedulingGroupItemViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(SchedulingGroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        ScheduleSection section = (ScheduleSection) group;

        if (section.isSelected()) {
            holder.setSelected(section);
        } else {
            holder.setUnselected(section);
        }
    }

    @Override
    public void onBindChildViewHolder(SchedulingGroupItemViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ScheduleItem artist = (ScheduleItem) group.getItems().get(childIndex);
        holder.setTitle(artist.getTitle());
    }

    //endregion

    //region - ViewHolder

    class SchedulingGroupViewHolder extends GroupViewHolder {

        private LinearLayout mLlUnselected;
        private TextView mTxtUnselectedNumber;
        private TextView mTxtUnselectedTitle;

        private LinearLayout mLlSelected;
        private TextView mTxtSelectedTitle;

        SchedulingGroupViewHolder(View itemView) {
            super(itemView);

            mLlUnselected = itemView.findViewById(R.id.ll_unselected);
            mTxtUnselectedNumber = itemView.findViewById(R.id.txt_unselected_number);
            mTxtUnselectedTitle = itemView.findViewById(R.id.txt_unselected_title);

            mLlSelected = itemView.findViewById(R.id.ll_selected);
            mTxtSelectedTitle = itemView.findViewById(R.id.txt_selected_title);
        }

        void setUnselected(ScheduleSection section) {
            mLlSelected.setVisibility(View.GONE);
            mLlUnselected.setVisibility(View.VISIBLE);
            mTxtUnselectedNumber.setText(section.getTitle());
            mTxtUnselectedTitle.setText(section.getTitle());
        }

        void setSelected(ScheduleSection section) {
            mLlSelected.setVisibility(View.VISIBLE);
            mLlUnselected.setVisibility(View.GONE);
            mTxtSelectedTitle.setText(section.getTitle());
        }

    }

    class SchedulingGroupItemViewHolder extends ChildViewHolder {

        private TextView mTxtTitle;

        SchedulingGroupItemViewHolder(View itemView) {
            super(itemView);
            mTxtTitle = itemView.findViewById(R.id.txt_selected_title);
        }

        void setTitle(String title) {
            mTxtTitle.setText(title);
        }

    }

    //endregion

}
