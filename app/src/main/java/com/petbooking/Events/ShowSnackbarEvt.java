package com.petbooking.Events;

import android.view.View;

/**
 * Created by Luciano José on 15/04/2017.
 */

public class ShowSnackbarEvt {

    private int message;
    private int duration;
    private int actionMessage;
    private View.OnClickListener action;

    public ShowSnackbarEvt(int message, int duration) {
        this.message = message;
        this.duration = duration;
    }

    public ShowSnackbarEvt withAction(int actionMessage, View.OnClickListener action) {
        this.actionMessage = actionMessage;
        this.action = action;
        return this;
    }


    public int getMessage() {
        return message;
    }

    public int getDuration() {
        return duration;
    }

    public int getActionMessage() {
        return actionMessage;
    }

    public View.OnClickListener getAction() {
        return action;
    }
}
