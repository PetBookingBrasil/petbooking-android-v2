package com.petbooking.UI.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.petbooking.R;


public class FeedbackDialogFragment extends DialogFragment implements DialogInterface.OnCancelListener {

    private FinishDialogListener finishDialogListener;
    private Dialog mDialog;
    private TextView mTvTitle;
    private TextView mTvText;
    private Button mBtnDialog;
    private Button mBtnSecond;

    private int mAction;
    private int mSecondAction;
    boolean showSecond;
    private int mTitle;
    private int mText;
    private String mTextDialog;
    private int mSecondText;
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

    View.OnClickListener secondListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishDialogListener.onFinishDialog(mSecondAction);
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
        mBtnSecond = (Button) view.findViewById(R.id.secondButton);

        mBtnDialog.setOnClickListener(buttonListener);

        finishDialogListener = (FinishDialogListener) getActivity();

        updateInfo();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = super.onCreateDialog(savedInstanceState);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setOnCancelListener(this);

        return mDialog;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();

    }

    public void setDialogInfo(int title, int text, int buttonText, int action) {
        this.mTitle = title;
        this.mText = text;
        this.mButtonText = buttonText;
        this.mAction = action;
    }

    public void setDialogInfo(int title, String text, int buttonText, int action) {
        this.mTitle = title;
        this.mTextDialog = text;
        this.mButtonText = buttonText;
        this.mAction = action;
    }

    public void updateInfo() {
        mTvTitle.setText(mTitle);
        if(mTextDialog !=null)
            mTvText.setText(mTextDialog);
        else
            mTvText.setText(mText);
        mBtnDialog.setText(mButtonText);

        if (showSecond) {
            mBtnSecond.setText(mSecondText);
            mBtnSecond.setOnClickListener(secondListener);
            mBtnSecond.setVisibility(View.VISIBLE);
        } else {
            mBtnSecond.setVisibility(View.GONE);
        }
    }

    public void setSecondButton(int text, int action) {
        mSecondAction = action;
        mSecondText = text;
    }

    public void showSecondButton(boolean show) {
        showSecond = show;
    }

    public interface FinishDialogListener {
        void onFinishDialog(int action);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        finishDialogListener.onFinishDialog(mAction);
    }
}
