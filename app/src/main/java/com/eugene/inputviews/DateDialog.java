package com.eugene.inputviews;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Диалоговое окно с календарем
 */
public class DateDialog extends AlertDialog.Builder {

    private DatePicker picker;
    private double ammount = 9.0;
    private OnDaySelectedListener listener;

    public DateDialog(Context context) {
        super(context);
        picker = new DatePicker(context);
        setView(picker);
    }

    public DateDialog(Context context, double ammount) {
        super(context);
        picker = new DatePicker(context);
        this.ammount = ammount;
        setView(picker);
    }

    private Day getDayDate(DatePicker picker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth(), 8, 0, 0);
        Day dayData = new Day();
        dayData.setAmount(ammount);
        dayData.setDate(calendar.getTimeInMillis());
        return dayData;
    }

    public void setDate(Date date) {
        setDate(date.getTime());
    }

    public void setDate(Long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        picker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }


    public long getDateInMilliseconds() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth(), 8, 0, 0);
        return calendar.getTimeInMillis();
    }

    public Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
        return calendar.getTime();
    }

    public Day getDayDate() {
        return getDayDate(picker);
    }

    public void setMultiSelect() {
        Calendar calendar = Calendar.getInstance();
        picker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                if (listener != null) {
                    listener.onDaySelected(getDayDate(datePicker));
                }
            }
        });
    }

    public DatePicker getPicker() {
        return picker;
    }

    public void updatePicker() {
        Date date = getDate();
        long maxDate = picker.getMaxDate();
        long minDate = picker.getMinDate();
        picker = new DatePicker(getContext());
        setView(picker);
        setDate(date);
        picker.setMaxDate(maxDate);
        picker.setMinDate(minDate);
    }


    public void setListener(OnDaySelectedListener listener) {
        this.listener = listener;
    }

    public interface OnDaySelectedListener {
        void onDaySelected(Day day);
    }

    @Override
    public AlertDialog show() {
        if (picker.getParent() != null) {
            ((ViewGroup) picker.getParent()).removeView(picker);
        }
        return super.show();
    }

    public static class Day {
        private double amount;
        private Date date;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public void setDate(long date) {
            this.date = new Date(date);
        }
    }
}