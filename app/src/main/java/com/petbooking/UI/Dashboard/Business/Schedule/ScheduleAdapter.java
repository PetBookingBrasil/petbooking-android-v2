package com.petbooking.UI.Dashboard.Business.Schedule;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petbooking.API.Business.Models.CategoryResp;
import com.petbooking.Models.AppointmentDate;
import com.petbooking.Models.BusinessServices;
import com.petbooking.Models.Pet;
import com.petbooking.Models.Professional;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleChild;
import com.petbooking.UI.Dashboard.Business.Schedule.Models.ScheduleGroup;
import com.thoughtbot.expandablerecyclerview.MultiTypeExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

final class ScheduleAdapter extends MultiTypeExpandableRecyclerViewAdapter<ScheduleAdapter.GroupViewHolder, ScheduleAdapter.ChildViewHolder> {

    private static final int GROUP_SECTION_VIEW_TYPE = 3;
    private static final int GROUP_BUTTON_VIEW_TYPE = 4;
    private static final int CHILD_VIEW_TYPE = 5;

    private Context mContext;
    private OnClickListener mOnClickListener;

    private ExpandableGroup mExpandedGroup;
    private List<Professional> mProfessionals;

    ScheduleAdapter(Context context, OnClickListener onClickListener) {
        super(ScheduleAdapter.getGenres(context));

        mContext = context;
        mOnClickListener = onClickListener;

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

    //region - MultiTypeExpandableRecyclerViewAdapter

    @Override
    public int getGroupViewType(int position, ExpandableGroup group) {
        ScheduleGroup scheduleGroup = (ScheduleGroup) group;

        if (scheduleGroup.getType() == ScheduleGroup.Type.BUTTON) {
            return GROUP_BUTTON_VIEW_TYPE;
        }

        return GROUP_SECTION_VIEW_TYPE;
    }

    @Override
    public int getChildViewType(int position, ExpandableGroup group, int childIndex) {
        return CHILD_VIEW_TYPE;
    }

    @Override
    public boolean isGroup(int viewType) {
        return (viewType == GROUP_SECTION_VIEW_TYPE || viewType == GROUP_BUTTON_VIEW_TYPE);
    }

    @Override
    public boolean isChild(int viewType) {
        return (viewType == CHILD_VIEW_TYPE);
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == GROUP_BUTTON_VIEW_TYPE) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_schedule_group_button, viewGroup, false);
            return new GroupButtonViewHolder(view);
        }

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_schedule_group_section, viewGroup, false);
        return new GroupSectionViewHolder(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_schedule_child, viewGroup, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        if (getGroupViewType(flatPosition, group) == GROUP_BUTTON_VIEW_TYPE) {
            onBindGroupButtonViewHolder(holder);
        } else {
            onBindGroupSectionViewHolder(holder, group);
        }
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ScheduleChild item = (ScheduleChild) group.getItems().get(childIndex);
        holder.setTitle(item.getTitle());

        holder.setOnClickListener(view -> {
            ScheduleGroup section = (ScheduleGroup) group;
            section.setId(item.getId());
            section.setTitle(item.getTitle());

            ScheduleGroup.Type type = section.getType();
            int position = section.getType().getValue();

            checkIfBusinessCategoryClicked(type, item.getId());
            checkIfServicesClicked(type, item.getId());
            checkIfProfessionalsClicked(type, item.getId());

            notifyItemChanged(position);
            toggleGroup(position);

            checkIfDayAndTimeClicked(type);
        });

    }

    //endregion

    //region - OnBind

    private void onBindGroupSectionViewHolder(GroupViewHolder holder, ExpandableGroup group) {
        GroupSectionViewHolder sectionHolder = (GroupSectionViewHolder) holder;
        ScheduleGroup section = (ScheduleGroup) group;

        sectionHolder.setTitle(section.getTitle());
    }

    private void onBindGroupButtonViewHolder(GroupViewHolder holder) {
        GroupButtonViewHolder buttonHolder = (GroupButtonViewHolder) holder;

        boolean enabled = (getDayAndTimeId() != null);
        buttonHolder.setEnabled(enabled);
    }

    //endregion

    //region - Getters

    String getPetId() {
        return getId(ScheduleGroup.Type.SELECT_PET);
    }

    String getCategoryId() {
        return getId(ScheduleGroup.Type.SERVICE_CATEGORY);
    }

    String getServiceId() {
        return getId(ScheduleGroup.Type.ADDITIONAL_SERVICES);
    }

    String getProfessionalId() {
        return getId(ScheduleGroup.Type.PROFESSIONAL);
    }

    String getDayAndTimeId() {
        return getId(ScheduleGroup.Type.DAY_AND_TIME);
    }

    private String getId(ScheduleGroup.Type type) {
        ScheduleGroup section = (ScheduleGroup) getGroups().get(type.getValue());
        return section.getId();
    }

    //endregion

    //region - Loaded

    void listPetsLoaded(List<Pet> pets) {
        ScheduleGroup.Type type = ScheduleGroup.Type.SELECT_PET;
        List<ScheduleChild> items = new ArrayList<>();

        if (pets != null) {
            for (Pet pet: pets) {
                items.add(new ScheduleChild(pet.id, pet.name));
            }
        }

        loaded(type, items);
        toggleGroup(type.getValue());
    }

    void listBusinessCategoriesLoaded(CategoryResp categoryResp) {
        ScheduleGroup.Type type = ScheduleGroup.Type.SERVICE_CATEGORY;
        List<ScheduleChild> items = new ArrayList<>();

        if (categoryResp != null) {
            for (CategoryResp.Item item: categoryResp.data) {
                items.add(new ScheduleChild(item.id, item.attributes.name));
            }
        }

        loaded(type, items);
    }

    void listServicesLoaded(List<BusinessServices> services) {
        ScheduleGroup.Type type = ScheduleGroup.Type.ADDITIONAL_SERVICES;
        List<ScheduleChild> items = new ArrayList<>();

        if (services != null) {
            for (BusinessServices service: services) {
                items.add(new ScheduleChild(service.id, service.name));
            }
        }

        loaded(type, items);
    }

    void listProfessionalsLoaded(List<Professional> professionals) {
        mProfessionals = professionals;

        ScheduleGroup.Type type = ScheduleGroup.Type.PROFESSIONAL;
        List<ScheduleChild> items = new ArrayList<>();

        if (professionals != null) {
            for (Professional professional: professionals) {
                items.add(new ScheduleChild(professional.id, professional.name));
            }
        }

        loaded(type, items);
    }

    private void listAvailableDatesLoaded(List<AppointmentDate> availableDates) {
        ScheduleGroup.Type type = ScheduleGroup.Type.DAY_AND_TIME;
        List<ScheduleChild> items = new ArrayList<>();

        if (availableDates != null) {
            for (AppointmentDate date: availableDates) {
                items.add(new ScheduleChild(date.monthName, date.monthName));
            }
        }

        loaded(type, items);
    }

    private void loaded(ScheduleGroup.Type type, List<ScheduleChild> items) {
        int position = type.getValue();

        List<ScheduleGroup> sections = (List<ScheduleGroup>) getGroups();
        ScheduleGroup oldSection = sections.get(position);
        ScheduleGroup newSection = new ScheduleGroup(mContext, type, items);

        sections.remove(oldSection);
        sections.add(position, newSection);

        notifyItemChanged(position);
    }

    //endregion

    //region - Check

    private void checkIfBusinessCategoryClicked(ScheduleGroup.Type type, String id) {
        if (mOnClickListener != null) {
            if (type == ScheduleGroup.Type.SERVICE_CATEGORY) {
                mOnClickListener.onBusinessCategoryClicked(id);
            }
        }

    }

    private void checkIfServicesClicked(ScheduleGroup.Type type, String id) {
        if (mOnClickListener != null) {
            if (type == ScheduleGroup.Type.ADDITIONAL_SERVICES) {
                mOnClickListener.onServicesClicked(id);
            }
        }
    }

    private void checkIfProfessionalsClicked(ScheduleGroup.Type type, String id) {
        if (mOnClickListener != null) {
            if (type == ScheduleGroup.Type.PROFESSIONAL) {
                for (Professional professional: mProfessionals) {
                    if (professional.id.equals(id)) {
                        listAvailableDatesLoaded(professional.availableDates);
                        break;
                    }
                }
            }
        }
    }

    private void checkIfDayAndTimeClicked(ScheduleGroup.Type type) {
        if (type == ScheduleGroup.Type.DAY_AND_TIME) {
            int position = ScheduleGroup.Type.BUTTON.getValue();
            notifyItemChanged(position);
        }
    }

    //endregion

    //region - Static

    private static List<ScheduleGroup> getGenres(Context context) {
        List<ScheduleGroup> genres = new ArrayList<>();

        genres.add(new ScheduleGroup(context, ScheduleGroup.Type.SELECT_PET));
        genres.add(new ScheduleGroup(context, ScheduleGroup.Type.SERVICE_CATEGORY));
        genres.add(new ScheduleGroup(context, ScheduleGroup.Type.ADDITIONAL_SERVICES));
        genres.add(new ScheduleGroup(context, ScheduleGroup.Type.PROFESSIONAL));
        genres.add(new ScheduleGroup(context, ScheduleGroup.Type.DAY_AND_TIME));
        genres.add(new ScheduleGroup(context, ScheduleGroup.Type.BUTTON));

        return genres;
    }

    //endregion

    //region - ViewHolder

    class GroupSectionViewHolder extends GroupViewHolder {

        private TextView mTxtSelectedTitle;

        GroupSectionViewHolder(View itemView) {
            super(itemView);
            mTxtSelectedTitle = itemView.findViewById(R.id.txt_selected_title);
        }

        void setTitle(String title) {
            mTxtSelectedTitle.setText(title);
        }

    }

    class GroupButtonViewHolder extends GroupViewHolder {

        private Button mButton;

        GroupButtonViewHolder(View itemView) {
            super(itemView);
            mButton = itemView.findViewById(R.id.button);
        }

        void setEnabled(boolean enabled) {
            mButton.setEnabled(enabled);
        }

    }

    class GroupViewHolder extends com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder {

        GroupViewHolder(View itemView) {
            super(itemView);
        }

    }

    class ChildViewHolder extends com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder {

        private LinearLayout mLinearLayout;
        private TextView mTxtTitle;

        ChildViewHolder(View itemView) {
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

    //region - Interface

    interface OnClickListener {
        void onBusinessCategoryClicked(String id);
        void onServicesClicked(String id);
    }

    //endregion

}
