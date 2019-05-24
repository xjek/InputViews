package com.eugene.inputviews.inputView;

import android.content.Context;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;

import com.eugene.inputviews.inputView.util.PhoneTextWatch;

/**
 * Поле для ввода телефона
 */
public class PhoneInputView extends TextInputView implements PhoneTextWatch.PhoneTextWatchListener {

    public PhoneInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(14);
        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789()- "));
        editText.setFilters(fArray);
        setError("not_a_valid_phone");
    }

    @Override
    protected void setTextWatcher() {
        editText.addTextChangedListener(new PhoneTextWatch(this));
    }

    @Override
    public boolean isValid() {
        return isEmpty() || getDigits(editText.getText().toString()).length() == 10;
    }

    @Override
    public String getFieldValue() {
        return getDigits(editText.getText().toString());
    }


}
