package com.petbooking.UI.Menu.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.petbooking.R;
import com.petbooking.UI.Widget.StyledSwitch;

public class SettingsActivity extends AppCompatActivity {

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

        mTvMaxDistance = (TextView) findViewById(R.id.maxDistance);
        mSbDistance = (SeekBar) findViewById(R.id.distanceBar);
        mSwPush = (StyledSwitch) findViewById(R.id.push);
        mSwEmail = (StyledSwitch) findViewById(R.id.email);
        mSwSms = (StyledSwitch) findViewById(R.id.sms);
        mSbDistance.setOnSeekBarChangeListener(distanceListener);
    }
}
