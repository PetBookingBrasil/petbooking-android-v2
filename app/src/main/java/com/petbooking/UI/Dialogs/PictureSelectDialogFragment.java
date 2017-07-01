package com.petbooking.UI.Dialogs;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.petbooking.Constants.AppConstants;
import com.petbooking.R;


public class PictureSelectDialogFragment extends DialogFragment {

    private FinishDialogListener finishDialogListener;
    private Dialog mDialog;
    private Button mBtnGallery;
    private Button mBtnCamera;
    private Button mBtnCancel;

    private Uri mImgUri;

    public PictureSelectDialogFragment() {
        super();
    }

    public static PictureSelectDialogFragment newInstance() {
        return new PictureSelectDialogFragment();
    }

    View.OnClickListener mCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishDialogListener.onFinishDialog(AppConstants.TAKE_PHOTO);
            dismiss();
        }
    };

    View.OnClickListener mGaleryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishDialogListener.onFinishDialog(AppConstants.PICK_PHOTO);
            dismiss();
        }
    };

    View.OnClickListener mCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_picture_select, container);

        finishDialogListener = (FinishDialogListener) getActivity();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnCamera = (Button) view.findViewById(R.id.camera);
        mBtnGallery = (Button) view.findViewById(R.id.gallery);
        mBtnCancel = (Button) view.findViewById(R.id.cancel_button);

        mBtnCamera.setOnClickListener(mCameraListener);
        mBtnGallery.setOnClickListener(mGaleryListener);
        mBtnCancel.setOnClickListener(mCancelListener);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = super.onCreateDialog(savedInstanceState);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

    public interface FinishDialogListener {
        void onFinishDialog(int action);
    }

}
