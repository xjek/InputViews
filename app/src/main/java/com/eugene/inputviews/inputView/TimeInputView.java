package com.eugene.inputviews.inputView;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;

import com.eugene.inputviews.Common;
import com.eugene.inputviews.R;
import com.eugene.inputviews.inputView.util.TimeDialo1g;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Поле для ввода времени
 */
public class TimeInputView extends BaseInputView {

    private Date date = new Date();
    private TimeDialo1g timeDialog;
    private OnTimeSelectedListener listener;

    public TimeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addButton(R.drawable.ic_clock);
        timeDialog = new TimeDialo1g(context);
        timeDialog.setPositiveButton(language.getEveryLetterUpperCaseText("ok"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setValue(timeDialog.getDate(date));
                if (listener != null) {
                    listener.onTimeSelected(TimeInputView.this, date);
                }
            }
        });
        timeDialog.setNegativeButton(language.getEveryLetterUpperCaseText("cancel"), null);
    }

    /**
     * Установить дату
     * @param date дата
     */
    private void setValue(Date date) {
        this.date = date;
        SimpleDateFormat format = new SimpleDateFormat(Common.FORMAT_HOURS_MINUTES_FORMAT, Locale.getDefault());
        setTextWithTrackChanges(format.format(date));
    }

    @Override
    protected void onEditTextClick() {
        timeDialog.show();
    }

    @Override
    protected void onFirstButtonClick() {
        timeDialog.show();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        setValue(date);
        timeDialog.setTime(date);
    }

    public void setDate(Long time) {
        setDate(new Date(time));
    }

    public void addOnTimeSelectedListener(OnTimeSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * Слушатель изменения даты
     */
    public interface OnTimeSelectedListener {
        void onTimeSelected(TimeInputView timeInputView, Date date);
    }
}
