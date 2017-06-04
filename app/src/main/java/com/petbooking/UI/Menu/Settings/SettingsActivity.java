package com.petbooking.UI.Menu.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.petbooking.R;

public class SettingsActivity extends AppCompatActivity {

    private TextView mTvMaxDistance;
    private SeekBar mSbDistance;

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
        mSbDistance.setOnSeekBarChangeListener(distanceListener);
    }
}
