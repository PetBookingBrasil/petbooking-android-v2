package com.petbooking.UI.Dashboard.Business.Scheduling.model;

import com.petbooking.Models.AppointmentDate;
import com.petbooking.Utils.CommonUtils;

import java.util.Date;

/**
 * Created by victorneves on 06/12/17.
 */

public class AppointmentDateChild {

   public int position;
   public String month;
   public String dayNumber;
   public String dayDescription;
   public String Time;
   public Date date;

    public AppointmentDateChild(int position, String month, Date date, String time) {
        this.position = position;
        this.month = month;
        this.date = date;
        Time = time;
    }

    public String getDayNumber() {
        return CommonUtils.formatDate(CommonUtils.DAY_FORMAT,this.date);
    }

    public String getDayDescription() {
        return CommonUtils.formatDate(CommonUtils.DAY_FORMAT_DESCRIOTION,this.date).toUpperCase();
    }

    public String getMonthDescription(){
        return CommonUtils.formatDate(CommonUtils.MONTH_DESCRIPTION_FORMAT,this.date);
    }
}
