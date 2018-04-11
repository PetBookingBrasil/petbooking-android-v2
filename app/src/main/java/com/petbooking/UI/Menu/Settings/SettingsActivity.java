package com.petbooking.UI.Menu.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.User.UserService;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Menu.Settings.Contact.ContactActivity;
import com.petbooking.UI.Menu.Settings.Privacy.PrivacyActivity;
import com.petbooking.UI.Menu.Settings.Terms.TermsActivity;
import com.petbooking.UI.Widget.StyledSwitch;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;

public class SettingsActivity extends AppCompatActivity {

    private UserService mUserService;
    private SessionManager mSessionManager;

    private TextView mTvMaxDistance;
    private SeekBar mSbDistance;
    private StyledSwitch mSwPush;
    private StyledSwitch mSwEmail;
    private StyledSwitch mSwSms;

    private TextView mTvContact;
    private TextView mTvTerms;
    private TextView mTvPrivacy;

    SeekBar.OnSeekBarChangeListener distanceListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mTvMaxDistance.setText(progress + " Km");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == mTvContact.getId()) {
                goToContact();
            } else if (id == mTvTerms.getId()) {
                goToPrivacy();
            }else if(id == mTvPrivacy.getId()){
                goToTerms();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mUserService = new UserService();
        mSessionManager = SessionManager.getInstance();

        getSupportActionBar().setElevation(0);

        mTvMaxDistance = (TextView) findViewById(R.id.maxDistance);
        mSbDistance = (SeekBar) findViewById(R.id.distanceBar);
        mSwPush = (StyledSwitch) findViewById(R.id.push);
        mSwEmail = (StyledSwitch) findViewById(R.id.email);
        mSwSms = (StyledSwitch) findViewById(R.id.sms);
        mSbDistance.setOnSeekBarChangeListener(distanceListener);

        mTvContact = (TextView) findViewById(R.id.contact);
        mTvTerms = (TextView) findViewById(R.id.terms);
        mTvPrivacy = (TextView) findViewById(R.id.support);

        mTvPrivacy.setOnClickListener(clickListener);
        mTvContact.setOnClickListener(clickListener);
        mTvTerms.setOnClickListener(clickListener);

        getUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        User user = new User(mSessionManager.getUserLogged().id, mSbDistance.getProgress(),
                mSwSms.isChecked(), mSwEmail.isChecked(), mSwPush.isChecked());
        updateRequest(user);
    }

    /**
     * Render user info
     *
     * @param user
     */
    private void renderInfo(User user) {
        mSbDistance.setProgress(user.searchRange);
        mTvMaxDistance.setText(user.searchRange + " Km");
        mSwPush.setChecked(user.acceptsNotifications);
        mSwEmail.setChecked(user.acceptsEmail);
        mSwSms.setChecked(user.acceptsSms);
    }

    /**
     * Get User information
     * from API
     */
    private void getUserInfo() {
        AppUtils.showLoadingDialog(this);
        mUserService.getUser(mSessionManager.getUserLogged().id, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                AuthUserResp authUserResp = (AuthUserResp) response;
                User user = APIUtils.parseUser(authUserResp);
                renderInfo(user);
                mSessionManager.setUserLogged(user);
                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
                AppUtils.hideDialog();
            }
        });
    }

    /**
     * Request to Update User
     *
     * @param user
     */
    public void updateRequest(User user) {
        mUserService.updateUser(user.id, user, new APICallback() {
            @Override
            public void onSuccess(Object response) {
            }

            @Override
            public void onError(Object error) {
            }
        });
    }

    public void goToContact() {
        Intent contactIntent = new Intent(this, ContactActivity.class);
        startActivity(contactIntent);
    }

    public void goToTerms() {
        Intent contactIntent = new Intent(this, TermsActivity.class);
        startActivity(contactIntent);
    }

    public void goToPrivacy() {
        Intent contactIntent = new Intent(this, PrivacyActivity.class);
        startActivity(contactIntent);
    }
}
