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

    private final int[] titles = {R.string.presentation_1_title, R.string.presentation_2_title, R.string.presentation_3_title};
    private final int[] texts = {R.string.presentation_1_text, R.string.presentation_2_text, R.string.presentation_3_text};
    private final int[] colors = {R.color.presentation_1, R.color.presentation_2, R.color.presentation_3};
    private final int[] images = {R.drawable.presentation_1, R.drawable.presentation_2, R.drawable.presentation_3};

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

        mPresentationLayout = (ConstraintLayout) view.findViewById(R.id.presentationLayout);
        mIvPresentation = (ImageView) view.findViewById(R.id.presentationImagem);
        mTvTitle = (TextView) view.findViewById(R.id.presentationTitle);
        mTvText = (TextView) view.findViewById(R.id.presentationText);

        mIvPresentation.setImageResource(images[position]);
        mTvTitle.setText(getContext().getResources().getString(titles[position]));
        mTvText.setText(getContext().getResources().getString(texts[position]));
        mPresentationLayout.setBackgroundColor(getContext().getResources().getColor(colors[position]));

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
