package com.petbooking.UI.RecoverPassword;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.petbooking.BaseActivity;
import com.petbooking.Events.ShowSnackbarEvt;
import com.petbooking.R;
import com.petbooking.Utils.FormUtils;

import org.greenrobot.eventbus.EventBus;

public class RecoverPasswordActivity extends BaseActivity {

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

        getSupportActionBar().setTitle(R.string.title_forgot_password);

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

        if (!FormUtils.isValidEmail(email)) {
            EventBus.getDefault().post(new ShowSnackbarEvt(R.string.error_invalid_email, Snackbar.LENGTH_SHORT));
            return;
        }
    }
}