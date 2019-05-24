package com.eugene.inputviews.inputView.util;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

/**
 * Checkbox for UniversalEditText
 */
public class CustomCheckBox extends AppCompatCheckBox {
    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {}

    public void setCheckedOnClick(boolean checked) {
        super.setChecked(checked);
    }
}
