package com.eugene.inputviews.inputView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.eugene.inputviews.Common;
import com.eugene.inputviews.DateDialog;
import com.eugene.inputviews.R;
import com.eugene.inputviews.inputView.savedState.DataSavedState;
import com.eugene.inputviews.inputView.util.TimeDialo1g;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Поле для ввода даты и времени
 */
public class DateTimeInputView extends BaseInputView {

    private Date date = new Date();
    private DateDialog dateDialog;
    private TimeDialo1g timeDialog;

    public DateTimeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addButton(R.drawable.ic_calendar);
        addButton(R.drawable.ic_clock);
        dateDialog = new DateDialog(context);
        dateDialog.setPositiveButton(language.getEveryLetterUpperCaseText("ok"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setValue(dateDialog.getDate());
            }
        });
        dateDialog.setNegativeButton(language.getEveryLetterUpperCaseText("cancel"), null);
        timeDialog = new TimeDialo1g(context);
        timeDialog.setPositiveButton(language.getEveryLetterUpperCaseText("ok"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setValue(timeDialog.getDate(date));
            }
        });
        timeDialog.setNegativeButton(language.getEveryLetterUpperCaseText("cancel"), null);
    }

    /**
     * Установить значение поля
     * @param date дата
     */
    private void setValue(Date date) {
        this.date = date;
        SimpleDateFormat format = new SimpleDateFormat(Common.FORMAT_DATE_AND_TIME);
        setTextWithTrackChanges(format.format(date));
    }

    @Override
    protected void onEditTextClick() {
        dateDialog.show();
    }

    @Override
    protected void onFirstButtonClick() {
        dateDialog.show();
    }

    @Override
    protected void onSecondButtonClick() {
        timeDialog.show();
    }

    public Date getDate() {
        return date;
    }

    /**
     * Установить значение поля
     * @param date дата
     */
    public void setDate(Date date) {
        if (date != null) {
            setValue(date);
            dateDialog.setDate(date);
            timeDialog.setTime(date);
        } else {
            this.date = null;
            setTextWithTrackChanges("");
            dateDialog.setDate(new Date());
            timeDialog.setTime(new Date());
        }
    }

    /**
     * Установить значение поля
     * @param time миллисекунды
     */
    public void setDate(Long time) {
        setDate(new Date(time));
    }

    /**
     * Установиь значение поля текущей датой
     */
    public void setCurrentDate() {
        setValue(new Date());
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return DataSavedState.setDate(date, super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        DataSavedState value = (DataSavedState) state;
        super.onRestoreInstanceState(value.getSuperState());
        if (value.date != null)
            setValue(value.date);
    }
}
