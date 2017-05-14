package com.petbooking;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.petbooking.Events.HideLoadingEvt;
import com.petbooking.Events.ShowLoadingEvt;
import com.petbooking.Events.ShowSnackbarEvt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Luciano José on 14/05/2017.
 */

public class BaseFragment extends Fragment {
    private Snackbar mSnackbar;
    protected final EventBus mEventBus = EventBus.getDefault();

    private AlertDialog mLoadingDialog;

    @Override
    public void onStart() {
        super.onStart();
        mEventBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mEventBus.unregister(this);
        hideLoading(null);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void showLoading(ShowLoadingEvt showEvt) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new AlertDialog.Builder(getContext())
                .setView(View.inflate(getContext(), R.layout.dialog_loading, null), 0, 0, 0, 0)
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
}
