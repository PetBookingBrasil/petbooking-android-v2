package com.petbooking.UI.Dashboard.Business.Schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleItem;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleSection;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

final class ScheduleAdapter extends ExpandableRecyclerViewAdapter<ScheduleAdapter.SchedulingGroupViewHolder, ScheduleAdapter.SchedulingGroupItemViewHolder> {

    private ExpandableGroup mExpandedGroup;

    ScheduleAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);

        setOnGroupExpandCollapseListener(new GroupExpandCollapseListener() {
            @Override
            public void onGroupExpanded(ExpandableGroup group) {
                if (mExpandedGroup != null) {
                    toggleGroup(mExpandedGroup);
                }
                mExpandedGroup = group;
            }

            @Override
            public void onGroupCollapsed(ExpandableGroup group) { }
        });

        toggleGroup(0);
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
        holder.setTitle(section.getTitle());
    }

    @Override
    public void onBindChildViewHolder(SchedulingGroupItemViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ScheduleItem artist = (ScheduleItem) group.getItems().get(childIndex);
        holder.setTitle(artist.getTitle());
    }

    //endregion

    //region - ViewHolder

    class SchedulingGroupViewHolder extends GroupViewHolder {

        private TextView mTxtSelectedTitle;

        SchedulingGroupViewHolder(View itemView) {
            super(itemView);
            mTxtSelectedTitle = itemView.findViewById(R.id.txt_selected_title);
        }

        void setTitle(String title) {
            mTxtSelectedTitle.setText(title);
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
