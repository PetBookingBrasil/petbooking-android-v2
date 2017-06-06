package com.petbooking.UI.Menu.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.petbooking.UI.Widget.StyledSwitch;
import com.petbooking.Utils.APIUtils;

public class SettingsActivity extends AppCompatActivity {

    private UserService mUserService;
    private SessionManager mSessionManager;

    private TextView mTvMaxDistance;
    private SeekBar mSbDistance;
    private StyledSwitch mSwPush;
    private StyledSwitch mSwEmail;
    private StyledSwitch mSwSms;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mUserService = new UserService();
        mSessionManager = SessionManager.getInstance();

        mTvMaxDistance = (TextView) findViewById(R.id.maxDistance);
        mSbDistance = (SeekBar) findViewById(R.id.distanceBar);
        mSwPush = (StyledSwitch) findViewById(R.id.push);
        mSwEmail = (StyledSwitch) findViewById(R.id.email);
        mSwSms = (StyledSwitch) findViewById(R.id.sms);
        mSbDistance.setOnSeekBarChangeListener(distanceListener);

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
        mUserService.getUser(mSessionManager.getUserLogged().id, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                AuthUserResp authUserResp = (AuthUserResp) response;
                User user = APIUtils.parseUser(authUserResp);
                renderInfo(user);
                mSessionManager.setUserLogged(user);
            }

            @Override
            public void onError(Object error) {

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
}
