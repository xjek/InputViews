package com.eugene.inputviews.inputView.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.TimePicker;


import com.eugene.inputviews.Language;

import java.util.Calendar;
import java.util.Date;

public class TimeDialo1g extends AlertDialog.Builder {

    private TimePicker timePicker;

    public TimeDialo1g(@NonNull Context context) {
        super(context);

       timePicker = new TimePicker(context);

        setView(timePicker);
        setNegativeButton(Language.getInstance().getUpperCaseText("cancel"), null);
    }

    public void setTime(Date date) {
        if (date != null)
            setTime(date.getTime());
    }

    public void setTime(Long mills) {
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);

        timePicker.setCurrentHour(calendar.get(Calendar.HOUR));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));*/
    }

    public Date getDate(Date date) {
        if (date == null)
            date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int time = (calendar.get(Calendar.HOUR) * 60 * 60 + calendar.get(Calendar.MINUTE) * 60) * 1000;
        long dateWithoutTime = date.getTime() - time;
        return new Date(dateWithoutTime + getTime() * 60 * 1000);
    }

    public int getTime() {
        /*int minute;
        if (Build.VERSION.SDK_INT < 23) {
            minute = timePicker.getCurrentHour() * 60 + timePicker.getCurrentMinute();
        } else {
            minute = timePicker.getHour() * 60 + timePicker.getMinute();
        }*/
        return 0;
    }

    @Override
    public AlertDialog show() {
        ViewGroup parent = (ViewGroup) timePicker.getParent();
        if (parent != null) {
            parent.removeView(timePicker);
        }
        return super.show();
    }
}