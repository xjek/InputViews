package com.eugene.inputviews.inputView;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.eugene.inputviews.inputView.savedState.DataSavedState;

/**
 * Поле для ввода текста
 */
public class TextInputView extends BaseInputView {

    public TextInputView(Context context, AttributeSet attrs) {
        super(context, attrs);

        editText.setFocusableInTouchMode(true);
        editText.setOnClickListener(null);
        editText.setLongClickable(true);
        firstFullTextOfEditText = "";
        editText.setGravity(Gravity.TOP);

        setTextWatcher();
    }

    /**
     * Инициализация
     * @param hintKeyLanguage ключ названия
     * @param countLines кол-во строк у поля
     * @param isReqiured обязательное поле
     */
    public void initialize(String hintKeyLanguage, int countLines, boolean... isReqiured) {
        super.initialize(hintKeyLanguage, isReqiured);
        setLinesCount(countLines);
    }

    /**
     * Установить значение
     * @param value значение
     */
    public void setValue(String value) {
        if (value != null && !value.isEmpty())
            editText.setText(value);
        firstFullTextOfEditText = editText.getText().toString();
    }

    @Override
    public boolean didChanged() {
        return !editText.getText().toString().equals(firstFullTextOfEditText);
    }

    public void resetChanged() {
        firstFullTextOfEditText = editText.getText().toString();
    }

    /**
     * Получить цифры из поля
     * @param input
     * @return
     */
    protected String getDigits(final CharSequence input){
        final StringBuilder sb = new StringBuilder(input.length());
        for(int i = 0; i < input.length(); i++){
            final char c = input.charAt(i);
            if(c > 47 && c < 58){
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Тип вводимых данных
     * @param type тип
     * @see android.text.InputType
     */
    public void setInputType(int type) {
        editText.setInputType(type);
    }


    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return DataSavedState.setString(editText.getText().toString(), super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        DataSavedState value = (DataSavedState) state;
        super.onRestoreInstanceState(value.getSuperState());
        if (!value.string.isEmpty())
            editText.setText(value.string);
    }
}
