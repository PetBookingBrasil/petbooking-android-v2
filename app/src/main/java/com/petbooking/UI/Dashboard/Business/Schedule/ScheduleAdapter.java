package com.petbooking.UI.Dashboard.Business.Schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.Models.Pet;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleItem;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleSection;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

final class ScheduleAdapter extends ExpandableRecyclerViewAdapter<ScheduleAdapter.SchedulingGroupViewHolder, ScheduleAdapter.SchedulingGroupItemViewHolder> {

    private Context mContext;
    private ExpandableGroup mExpandedGroup;

    ScheduleAdapter(Context context) {
        super(ScheduleAdapter.getGenres(context));
        mContext = context;

        setOnGroupExpandCollapseListener(new GroupExpandCollapseListener() {
            @Override
            public void onGroupExpanded(ExpandableGroup group) {
                if (mExpandedGroup != null) {
                    toggleGroup(mExpandedGroup);
                }
                mExpandedGroup = group;
            }

            @Override
            public void onGroupCollapsed(ExpandableGroup group) {
                mExpandedGroup = null;
            }
        });
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
        final ScheduleItem item = (ScheduleItem) group.getItems().get(childIndex);
        holder.setTitle(item.getTitle());

        holder.setOnClickListener(view -> {
            ScheduleSection section = (ScheduleSection) group;
            section.setTitle(item.getTitle());

            int position = section.getType().getValue();
            notifyItemChanged(position);
            toggleGroup(position);
        });

    }

    //endregion

    //region - Protected

    void listPetsLoaded(List<Pet> pets) {
        ScheduleSection.Type type = ScheduleSection.Type.SELECT_PET;
        List<ScheduleItem> items = new ArrayList<>();

        if (pets != null) {
            for (Pet pet: pets) {
                items.add(new ScheduleItem(pet.name));
            }
        }

        loaded(type, items);
        toggleGroup(type.getValue());
    }

    void listBusinessCategoriesLoaded(CategoryResp categoryResp) {
        ScheduleSection.Type type = ScheduleSection.Type.SERVICE_CATEGORY;
        List<ScheduleItem> items = new ArrayList<>();

        if (categoryResp != null) {
            for (CategoryResp.Item item: categoryResp.data) {
                items.add(new ScheduleItem(item.attributes.name));
            }
        }

        loaded(type, items);
    }

    //endregion

    //region - Private

    private void loaded(ScheduleSection.Type type, List<ScheduleItem> items) {
        int position = type.getValue();

        List<ScheduleSection> sections = (List<ScheduleSection>) getGroups();
        ScheduleSection oldSection = sections.get(position);
        ScheduleSection newSection = new ScheduleSection(mContext, type, items);

        sections.remove(oldSection);
        sections.add(position, newSection);

        notifyItemChanged(position);
    }

    //endregion

    //region - Static

    private static List<ScheduleSection> getGenres(Context context) {
        List<ScheduleSection> genres = new ArrayList<>();

        genres.add(new ScheduleSection(context, ScheduleSection.Type.SELECT_PET));
        genres.add(new ScheduleSection(context, ScheduleSection.Type.SERVICE_CATEGORY));
        genres.add(new ScheduleSection(context, ScheduleSection.Type.ADDITIONAL_SERVICES));
        genres.add(new ScheduleSection(context, ScheduleSection.Type.PROFESSIONAL));
        genres.add(new ScheduleSection(context, ScheduleSection.Type.DAY_AND_TIME));

        return genres;
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

        private LinearLayout mLinearLayout;
        private TextView mTxtTitle;

        SchedulingGroupItemViewHolder(View itemView) {
            super(itemView);
            mLinearLayout = itemView.findViewById(R.id.linear_layout);
            mTxtTitle = itemView.findViewById(R.id.txt_selected_title);
        }

        void setOnClickListener(View.OnClickListener listener) {
            mLinearLayout.setOnClickListener(listener);
        }

        void setTitle(String title) {
            mTxtTitle.setText(title);
        }

    }

    //endregion

}
