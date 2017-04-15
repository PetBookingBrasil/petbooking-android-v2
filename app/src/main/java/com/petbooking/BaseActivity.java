package com.petbooking;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.petbooking.Events.HideLoadingEvt;
import com.petbooking.Events.ShowLoadingEvt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity extends AppCompatActivity {

    protected final EventBus mEventBus = EventBus.getDefault();

    private AlertDialog mLoadingDialog;

    @Override
    protected void onStart() {
        super.onStart();
        mEventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mEventBus.unregister(this);
        onHideLoading(null);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void onShowLoading(ShowLoadingEvt showEvt) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new AlertDialog.Builder(this)
                .setView(View.inflate(getApplicationContext(), R.layout.dialog_loading, null), 0, 0, 0, 0)
                .create();
        mLoadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mLoadingDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void onHideLoading(HideLoadingEvt hideEvt) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

}
