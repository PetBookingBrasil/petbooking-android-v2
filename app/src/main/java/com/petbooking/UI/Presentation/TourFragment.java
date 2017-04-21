package com.petbooking.UI.Presentation;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petbooking.Constants.AppConstants;
import com.petbooking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TourFragment extends Fragment {

    private static final String PAGE_POSITION = "position";
    private ConstraintLayout mPresentationLayout;
    private ImageView mIvPresentation;
    private TextView mTvTitle;
    private TextView mTvText;


    public TourFragment() {
        // Required empty public constructor
    }


    public static Fragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(PAGE_POSITION, position);

        TourFragment fragment = new TourFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour, container, false);

        int position = getPosition();
        int colorId = AppConstants.PRESENTATION_COLORS[position];
        position++;
        String packageName = getContext().getPackageName();
        String imageId = getContext().getString(R.string.presentation_image_format, position);
        String titleId = getContext().getString(R.string.presentation_title_format, position);
        String textId = getContext().getString(R.string.presentation_text_format, position);

        mPresentationLayout = (ConstraintLayout) view.findViewById(R.id.presentationLayout);
        mIvPresentation = (ImageView) view.findViewById(R.id.presentationImagem);
        mTvTitle = (TextView) view.findViewById(R.id.presentationTitle);
        mTvText = (TextView) view.findViewById(R.id.presentationText);

        mIvPresentation.setImageResource(getResources().getIdentifier(imageId, "drawable", packageName));
        mTvTitle.setText(getResources().getIdentifier(titleId, "string", packageName));
        //mTvText.setText(getResources().getIdentifier(textId, "string", packageName));
        mTvText.setText(R.string.small_lorem);
        mPresentationLayout.setBackgroundColor(getResources().getColor(colorId));

        return view;
    }

    private int getPosition() {
        int position = 0;
        if (getArguments() != null) {
            position = getArguments().getInt(PAGE_POSITION, 0);
        }
        return position;
    }

}
