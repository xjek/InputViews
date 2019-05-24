package com.eugene.inputviews.inputView.util;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;

/**
 * Обертка над PhoneNumberFormattingTextWatcher. Внедряется свой слушатель, который реагирует на изменения текста
 */
public class PhoneTextWatch extends PhoneNumberFormattingTextWatcher {

    private final PhoneTextWatchListener listener;

    public PhoneTextWatch(PhoneTextWatchListener listener) {
        this.listener = listener;
    }

    @Override
    public synchronized void afterTextChanged(Editable s) {
        super.afterTextChanged(s);
        listener.afterTextChanged(s);
    }

    public interface PhoneTextWatchListener {
        void afterTextChanged(Editable s);
    }
}
