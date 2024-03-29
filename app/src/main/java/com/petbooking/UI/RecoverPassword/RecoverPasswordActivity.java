package com.petbooking.UI.RecoverPassword;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.petbooking.API.User.UserService;
import com.petbooking.BaseActivity;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Events.ShowSnackbarEvt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.R;
import com.petbooking.UI.Dialogs.FeedbackDialogFragment;
import com.petbooking.UI.Login.LoginActivity;
import com.petbooking.Utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

public class RecoverPasswordActivity extends BaseActivity implements FeedbackDialogFragment.FinishDialogListener {

    private UserService mUserService;

    private FeedbackDialogFragment mFeedbackDialgoFragment;
    private EditText mEdtRecoverEmail;
    private Button mBtnSendEmail;

    View.OnClickListener sendButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendRequest();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserService = new UserService();

        mFeedbackDialgoFragment = FeedbackDialogFragment.newInstance();
        mEdtRecoverEmail = (EditText) findViewById(R.id.recoverEmail);
        mBtnSendEmail = (Button) findViewById(R.id.sendButton);

        mBtnSendEmail.setOnClickListener(sendButtonListener);
    }

    /**
     * Send Recover Password Request
     */
    private void sendRequest() {
        String email = mEdtRecoverEmail.getText().toString();

        if (email.equals("")) {
            EventBus.getDefault().post(new ShowSnackbarEvt(R.string.email_empty, Snackbar.LENGTH_SHORT));
            return;
        }

        if (!CommonUtils.isValidEmail(email)) {
            EventBus.getDefault().post(new ShowSnackbarEvt(R.string.error_invalid_email, Snackbar.LENGTH_SHORT));
            return;
        }

        mUserService.recoverPassword(email, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                mFeedbackDialgoFragment.setDialogInfo(R.string.recover_email_title, R.string.recover_email_text,
                        R.string.dialog_button_ok, AppConstants.OK_ACTION);
                mFeedbackDialgoFragment.show(getSupportFragmentManager(), "RECOVER_EMAIL");
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    /**
     * Go to Login
     */
    public void goToLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public void onFinishDialog(int action) {
        if (action == AppConstants.OK_ACTION) {
            goToLogin();
        }
    }
}
