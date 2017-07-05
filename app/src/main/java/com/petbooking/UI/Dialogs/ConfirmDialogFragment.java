package com.petbooking.UI.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.petbooking.Constants.AppConstants;
import com.petbooking.R;


public class ConfirmDialogFragment extends DialogFragment implements DialogInterface.OnCancelListener {

    private FinishDialogListener finishDialogListener;
    private Dialog mDialog;
    private TextView mTvTitle;
    private TextView mTvText;
    private Button mBtnConfirm;
    private Button mBtnCancel;

    private int mTitle;
    private int mText;
    private int mButtonConfirmText;

    public ConfirmDialogFragment() {
        super();
    }

    public static ConfirmDialogFragment newInstance() {
        return new ConfirmDialogFragment();
    }

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.confirm_button) {
                finishDialogListener.onFinishDialog(AppConstants.CONFIRM_ACTION);
            } else {
                finishDialogListener.onFinishDialog(AppConstants.CANCEL_ACTION);
            }
            dismiss();
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTvTitle = (TextView) view.findViewById(R.id.dialogTitle);
        mTvText = (TextView) view.findViewById(R.id.dialogText);
        mBtnConfirm = (Button) view.findViewById(R.id.confirm_button);
        mBtnCancel = (Button) view.findViewById(R.id.cancel_button);

        mBtnConfirm.setOnClickListener(buttonListener);
        mBtnCancel.setOnClickListener(buttonListener);

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

    public void setDialogInfo(int title, int text, int confirmText) {
        this.mTitle = title;
        this.mText = text;
        this.mButtonConfirmText = confirmText;
    }

    public void updateInfo() {
        mTvTitle.setText(mTitle);
        mTvText.setText(mText);
        mBtnConfirm.setText(mButtonConfirmText);
        mBtnCancel.setText(R.string.dialog_cancel);
    }

    public interface FinishDialogListener {
        void onFinishDialog(int action);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        finishDialogListener.onFinishDialog(AppConstants.CANCEL_ACTION);
    }
}
