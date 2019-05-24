package com.eugene.inputviews.inputView;

import android.content.Context;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.SparseArray;

/**
 * Поле для ввода текста по шаблону
 */
public class MaskInputView extends TextInputView {

    private String mask = "###-###-###"; // Пока просто содержит статический шаблон

    private SparseArray<String> keyLists = new SparseArray<>();

    public MaskInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i < mask.length(); i++) {
            final char c = mask.charAt(i);
            if (c != 35) {
                keyLists.append(i, String.valueOf(c));
            }
        }
        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mask.length())});
        setError(mask);
    }

    @Override
    public boolean isValid() {
        return isEmpty() || editText.getText().toString().length() == mask.length();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        editText.removeTextChangedListener(this);
        String digits = getDigits(charSequence);
        StringBuilder stringBuilder = new StringBuilder(digits);
        for (int j = 0; j < keyLists.size(); j++) {
            int key = keyLists.keyAt(j);
            if (stringBuilder.length() > key) {
                stringBuilder.insert(key, keyLists.get(key));
            } else {
                break;
            }
        }
        editText.setText(stringBuilder.toString());
        editText.setSelection(stringBuilder.length());
        editText.addTextChangedListener(this);
    }
}
