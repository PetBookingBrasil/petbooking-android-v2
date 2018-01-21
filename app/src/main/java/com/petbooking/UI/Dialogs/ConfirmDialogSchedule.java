package com.petbooking.UI.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petbooking.Constants.AppConstants;
import com.petbooking.R;

/**
 * Created by joice on 10/12/17.
 */

public class ConfirmDialogSchedule extends DialogFragment implements DialogInterface.OnCancelListener{
    private FinishDialogListener finishDialogListener;
    private Dialog mDialog;
    private TextView mTvTitle;
    private TextView mTvText;
    private Button mBtnConfirm;
    private Button mBtnCancel;
    private Button mOtherSchedule;
    TextView textAnim;

    private int mTitle;
    private int mText;
    private int mButtonConfirmText;
    private int mButtonCancelText;
    private int mButtonScheduleText;
    private int mAction = -1;

    public ConfirmDialogSchedule() {
        //no instance
        super();
    }

    public static ConfirmDialogSchedule newInstance() {
        return new ConfirmDialogSchedule();
    }

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.confirm_button) {
                int action = mAction == -1 ? AppConstants.CONFIRM_ACTION : mAction;
                finishDialogListener.onFinishDialog(action);
            } else if(v.getId() == R.id.cancel_button){
                finishDialogListener.onFinishDialog(AppConstants.CANCEL_ACTION);
            }else{
                finishDialogListener.onFinishDialog(AppConstants.NEWSCHEDULE_SERVICE);
            }
            dismiss();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_schedule, container);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTvTitle = (TextView) view.findViewById(R.id.dialogTitle);
        mTvText = (TextView) view.findViewById(R.id.dialogText);
        mBtnConfirm = (Button) view.findViewById(R.id.confirm_button);
        mBtnCancel = (Button) view.findViewById(R.id.cancel_button);
        mOtherSchedule = (Button) view.findViewById(R.id.schedule_other_pet);
        textAnim = (TextView) view.findViewById(R.id.cart_anim);
        final RelativeLayout container = (RelativeLayout) view.findViewById(R.id.container);
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        anim.reset();

        this.textAnim.clearAnimation();
        this.textAnim.setAnimation(anim);
        this.textAnim.getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.removeView(textAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.textAnim.startAnimation(textAnim.getAnimation());

        mBtnConfirm.setOnClickListener(buttonListener);
        mBtnCancel.setOnClickListener(buttonListener);
        mOtherSchedule.setOnClickListener(buttonListener);

        if (finishDialogListener == null) {
            finishDialogListener = (FinishDialogListener) getActivity();
        }

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

    public void setDialogInfo(int title, int text, int confirmText, int scheduleText) {
        this.mTitle = title;
        this.mText = text;
        this.mButtonConfirmText = confirmText;
        this.mButtonCancelText = R.string.dialog_cancel;
        this.mButtonScheduleText = scheduleText;
    }

    public void setAction(int action) {
        this.mAction = action;
    }

    public void setCancelText(int text) {
        this.mButtonCancelText = text;
    }

    public void updateInfo() {
        mTvTitle.setText(mTitle);
        mTvText.setText(mText);
        mBtnConfirm.setText(mButtonConfirmText);
        mBtnCancel.setText(mButtonCancelText);
        mOtherSchedule.setText(mButtonScheduleText);
    }

    public void hideText(){
        textAnim.setVisibility(View.GONE);
    }

    public void show(FragmentManager manager, String tag, Context context) {
        super.show(manager, tag);

    }

    public void animation(){

    }

    public interface FinishDialogListener {
        void onFinishDialog(int action);
    }

    public void setFinishDialogListener(FinishDialogListener finishDialogListener) {
        this.finishDialogListener = finishDialogListener;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        finishDialogListener.onFinishDialog(AppConstants.CANCEL_ACTION);
    }
}
