package com.petbooking.UI.SignUp;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.petbooking.Constants.AppConstants;
import com.petbooking.R;

import java.net.URI;

import static android.app.Activity.RESULT_OK;


public class PictureSelectDialogFragment extends DialogFragment {

    private FinishDialogListener finishDialogListener;
    private Dialog mDialog;
    private ImageView mIvGallery;
    private ImageView mIvCamera;

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

        mIvCamera = (ImageView) view.findViewById(R.id.camera);
        mIvGallery = (ImageView) view.findViewById(R.id.gallery);

        mIvCamera.setOnClickListener(mCameraListener);
        mIvGallery.setOnClickListener(mGaleryListener);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = super.onCreateDialog(savedInstanceState);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return mDialog;
    }

    public interface FinishDialogListener {
        void onFinishDialog(int action);
    }

}
