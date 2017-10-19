package com.petbooking.UI.Dashboard.Business.Schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleGroupItem;
import com.petbooking.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

final class ScheduleAdapter extends ExpandableRecyclerViewAdapter<ScheduleAdapter.SchedulingGroupViewHolder, ScheduleAdapter.SchedulingGroupItemViewHolder> {

    public ScheduleAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    //region - ExpandableRecyclerViewAdapter

    @Override
    public SchedulingGroupViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_schedule_group, viewGroup, false);
        return new SchedulingGroupViewHolder(view);
    }

    @Override
    public SchedulingGroupItemViewHolder onCreateChildViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_schedule_group_item, viewGroup, false);
        return new SchedulingGroupItemViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(SchedulingGroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTitle(group.getTitle());
    }

    @Override
    public void onBindChildViewHolder(SchedulingGroupItemViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ScheduleGroupItem artist = (ScheduleGroupItem) group.getItems().get(childIndex);
        holder.setTitle(artist.getTitle());
    }

    //endregion

    //region - ViewHolder

    class SchedulingGroupViewHolder extends GroupViewHolder {

        private TextView mTxtTitle;

        SchedulingGroupViewHolder(View itemView) {
            super(itemView);

            mTxtTitle = itemView.findViewById(R.id.txt_title);
        }

        void setTitle(String title) {
            mTxtTitle.setText(title);
        }

    }

    class SchedulingGroupItemViewHolder extends ChildViewHolder {

        private TextView mTxtTitle;

        SchedulingGroupItemViewHolder(View itemView) {
            super(itemView);
            mTxtTitle = itemView.findViewById(R.id.txt_title);
        }

        void setTitle(String title) {
            mTxtTitle.setText(title);
        }

    }

    //endregion

}
