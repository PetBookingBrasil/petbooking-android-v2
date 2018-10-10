package com.petbooking;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.petbooking.Events.HideLoadingEvt;
import com.petbooking.Events.ShowLoadingEvt;
import com.petbooking.Events.ShowSnackbarEvt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity extends AppCompatActivity {

    private Snackbar mSnackbar;
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
        hideLoading(null);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void showLoading(ShowLoadingEvt showEvt) {
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
    public synchronized void hideLoading(HideLoadingEvt hideEvt) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void showSnackbar(ShowSnackbarEvt showSnackbarEvt) {
        hideLoading(null);
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        mSnackbar = Snackbar.make(rootView, showSnackbarEvt.getMessage(),
                showSnackbarEvt.getDuration());

        if (showSnackbarEvt.getAction() != null) {
            mSnackbar.setAction(showSnackbarEvt.getActionMessage(), showSnackbarEvt.getAction());
        }

        mSnackbar.show();
    }
}
