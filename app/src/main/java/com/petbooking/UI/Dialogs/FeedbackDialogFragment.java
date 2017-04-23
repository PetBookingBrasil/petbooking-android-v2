package com.petbooking.UI.Dialogs;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.petbooking.R;


public class FeedbackDialogFragment extends DialogFragment {

    private FinishDialogListener finishDialogListener;
    private Dialog mDialog;
    private TextView mTvTitle;
    private TextView mTvText;
    private Button mBtnDialog;

    private int mAction;
    private int mTitle;
    private int mText;
    private int mButtonText;

    public FeedbackDialogFragment() {
        super();
    }

    public static FeedbackDialogFragment newInstance() {
        return new FeedbackDialogFragment();
    }

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishDialogListener.onFinishDialog(mAction);
            dismiss();
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_feedback, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTvTitle = (TextView) view.findViewById(R.id.dialogTitle);
        mTvText = (TextView) view.findViewById(R.id.dialogText);
        mBtnDialog = (Button) view.findViewById(R.id.dialogButton);
        mBtnDialog.setOnClickListener(buttonListener);

        finishDialogListener = (FinishDialogListener) getActivity();

        updateInfo();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = super.onCreateDialog(savedInstanceState);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return mDialog;
    }

    public void setDialogInfo(int title, int text, int buttonText, int action) {
        this.mTitle = title;
        this.mText = text;
        this.mButtonText = buttonText;
        this.mAction = action;
    }

    public void updateInfo() {
        mTvTitle.setText(mTitle);
        mTvText.setText(mText);
        mBtnDialog.setText(mButtonText);
    }

    public interface FinishDialogListener {
        void onFinishDialog(int action);
    }
}
