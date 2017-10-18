package com.petbooking.UI.Dashboard.Business.BusinessServices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petbooking.Models.Artist;
import com.petbooking.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

/**
 * Created by Bruno Tortato Furtado on 18/10/17.
 */

final class GenreAdapter extends ExpandableRecyclerViewAdapter<GenreAdapter.GenreViewHolder, GenreAdapter.ArtistViewHolder> {

    public GenreAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    //region - ExpandableRecyclerViewAdapter

    @Override
    public GenreViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_genre, viewGroup, false);
        return new GenreViewHolder(view);
    }

    @Override
    public ArtistViewHolder onCreateChildViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_artist, viewGroup, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(GenreViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTitle(group.getTitle());
    }

    @Override
    public void onBindChildViewHolder(ArtistViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Artist artist = (Artist) group.getItems().get(childIndex);
        holder.setTitle(artist.getTitle());
    }

    //endregion

    //region - ViewHolder

    class GenreViewHolder extends GroupViewHolder {

        private TextView mTxtTitle;

        GenreViewHolder(View itemView) {
            super(itemView);
            mTxtTitle = itemView.findViewById(R.id.txt_title);
        }

        void setTitle(String title) {
            mTxtTitle.setText(title);
        }

    }

    class ArtistViewHolder extends ChildViewHolder {

        private TextView mTxtTitle;

        ArtistViewHolder(View itemView) {
            super(itemView);
            mTxtTitle = itemView.findViewById(R.id.txt_title);
        }

        void setTitle(String title) {
            mTxtTitle.setText(title);
        }

    }

    //endregion

}
