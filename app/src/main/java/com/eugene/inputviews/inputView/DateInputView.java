package com.eugene.inputviews.inputView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.eugene.inputviews.DateDialog;
import com.eugene.inputviews.R;
import com.eugene.inputviews.inputView.savedState.DataSavedState;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Поле для ввода даты
 */
public class DateInputView extends BaseInputView implements DialogInterface.OnClickListener {

    private Date date;
    private DateDialog dateDialog;
    private OnDateSelectedListener listener;

    public DateInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addButton(R.drawable.ic_calendar);
        dateDialog = new DateDialog(context);
        dateDialog.setPositiveButton(language.getEveryLetterUpperCaseText("ok"), this);
        dateDialog.setNegativeButton(language.getEveryLetterUpperCaseText("cancel"), null);
    }

    /**
     * Клик на кнопки ок в диалоге
     * @param dialogInterface диалог
     * @param i позиция кнопки
     */
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        date = dateDialog.getDate();
        setValue(date);
        if (listener != null)
            listener.onDateSelected(this, date);
    }

    /**
     * Установить значение поля
     * @param date дата
     */
    private void setValue(Date date) {
        this.date = date;
        SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy", Locale.getDefault());
        setTextWithTrackChanges(format.format(date));
    }

    /**
     * Установиь значение поля текущей датой
     */
    public void setCurrentDate() {
        setValue(new Date());
    }

    @Override
    protected void onFirstButtonClick() {
        dateDialog.show();
    }

    @Override
    protected void onEditTextClick() {
        if (firstFullTextOfEditText == null) {
            firstFullTextOfEditText = "";
        }
        dateDialog.show();
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
        } else {
            this.date = null;
            setTextWithTrackChanges("");
            dateDialog.setDate(new Date());
        }
    }

    /**
     * Установить значение поля
     * @param time миллисекунды
     */
    public void setDate(long time) {
        if (time != 0) {
            this.date = new Date(time);
            setDate(date);
        } else {
            setDate(null);
        }
    }

    /**
     * Установить минимальную дату для выбора
     * @param date дата
     */
    public void setMinDate(Date date) {
        if (date != null) {
            dateDialog.getPicker().setMinDate(0);
            dateDialog.getPicker().setMinDate(date.getTime());
            dateDialog.setDate(getDate());
            dateDialog.updatePicker();
        }
    }

    /**
     * Установить максимальную дату для выбора
     * @param date дата
     */
    public void setMaxDate(Date date) {
        if (date != null) {
            dateDialog.getPicker().setMaxDate(0);
            dateDialog.getPicker().setMaxDate(date.getTime());
            dateDialog.setDate(getDate());
            dateDialog.updatePicker();
        }
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

    public void addOnDateSelectedListener(OnDateSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * Слушатель изменения даты
     */
    public interface OnDateSelectedListener {
        void onDateSelected(DateInputView dateInputView, Date date);
    }
}
