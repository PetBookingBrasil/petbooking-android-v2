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
import android.widget.Button;

import com.petbooking.R;


public class TableDialogFragment extends DialogFragment implements DialogInterface.OnCancelListener {

    private Dialog mDialog;
    private Button mBtnOk;
    private int mLayout;

    public TableDialogFragment() {
        super();
    }

    public static TableDialogFragment newInstance() {
        return new TableDialogFragment();
    }

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(mLayout, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnOk = (Button) view.findViewById(R.id.okButton);
        mBtnOk.setOnClickListener(buttonListener);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = super.onCreateDialog(savedInstanceState);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setOnCancelListener(this);

        return mDialog;
    }

    public void setLayout(int layout) {
        this.mLayout = layout;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
