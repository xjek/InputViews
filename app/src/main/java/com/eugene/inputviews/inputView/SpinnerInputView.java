package com.eugene.inputviews.inputView;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.eugene.inputviews.R;
import com.eugene.inputviews.inputView.savedState.DataSavedState;

import java.util.ArrayList;
import java.util.List;

/**
 * Поле с выпадающим списком
 * @param <T> тип модели
 */
public class SpinnerInputView<T> extends BaseInputView implements AdapterView.OnItemSelectedListener  {

    private Spinner spinner;
    private SpinnerInputViewAdapter<T> adapter;
    private boolean firstValueIsEmpty; // первое значение в списке пустое
    private OnItemSelectedListener listener;
    private boolean hasUserClick; // Было ли пользовательское нажатие

    public SpinnerInputView(Context context) {
        super(context);
        spinner = new Spinner(context);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        mainView.addView(spinner);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

    }

    public SpinnerInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        spinner = new Spinner(context);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        mainView.addView(spinner);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    /**
     * Инициализация
     * @param hintKeyLanguage ключ названия
     * @param adapter адапте для списка
     * @param isReqiured обязательное поле
     */
    public void initialize(String hintKeyLanguage, SpinnerInputViewAdapter<T> adapter, boolean ... isReqiured) {
        super.initialize(hintKeyLanguage, isReqiured);
        addButton(R.drawable.ic_arrow_down);
        this.adapter = adapter;
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onFirstButtonClick() {
        hasUserClick = true;
        spinner.performClick();
    }

    /**
     * Установить выбранное значение
     * @param model модель
     */
    public void setSelected(T model) {
        if (model != null)
            setSelected(adapter.getValue(model));
    }

    /**
     * Установить выбранное значение
     * @param selection позиция в списке
     */
    public void setSelected(int selection) {
        if (firstValueIsEmpty) {
            spinner.setSelection(selection + 1);
        } else {
            spinner.setSelection(selection);
        }
    }

    /**
     * Установить выбранное значение
     * @param value значение. Является текстом, который выводит список. По нему и будет проверятся сходство
     */
    // BUT NOT ID
    public void setSelected(String value) {
        if (value == null) {
            spinner.setSelection(0);
        } else {
            for (int i = 0; i < adapter.getValues().size(); i++) {
                if (adapter.getValue(adapter.getValues().get(i)).equals(value)) {
                    if (firstValueIsEmpty) {
                        spinner.setSelection(i + 1);
                    } else {
                        spinner.setSelection(i);
                    }
                }
            }
        }

    }

    /**
     * Установить данные для списка
     * @param data данные
     * @param selection выбранная позиция в списке
     */
    public void setData(List<T> data, int ... selection) {
        adapter.update(data);
        if (data == null || data.isEmpty()) {
            spinner.setSelection(-1);
        } else {
            setSelected(selection.length > 0 ? selection[0] : 0);
        }
        T model = getSelectedModel();
        if (model == null) {
            setText("");
        } else {
            setText(adapter.getValue(model));
        }
    }

    /**
     * Установить данные для списка
     * @param data данные
     * @param selection значение. Является текстом, который выводит список. По нему и будет проверятся сходство
     */
    public void setData(List<T> data, String selection) {
        adapter.update(data);
        if (data == null || data.isEmpty() || TextUtils.isEmpty(selection)) {
            spinner.setSelection(-1);
        } else {
            setSelected(selection);
        }
        T model = getSelectedModel();
        if (model == null) {
            setText("");
        } else {
            setText(adapter.getValue(model));
        }
    }

    /**
     * Клик на позиции
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String value = (String) adapterView.getSelectedItem();
        setText(value);
        if (listener != null && hasUserClick) {
            listener.onItemSelected(this, adapterView.getSelectedItemPosition());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    protected void onEditTextClick() {
        hasUserClick = true;
        spinner.performClick();
    }

    public void setFirstValueIsEmpty() {
        this.firstValueIsEmpty = true;
        adapter.firstValueIsEmpty = true;
    }

    /**
     * Получить выбранную модель
     * @return модель
     */
    @Nullable
    public T getSelectedModel() {
        return adapter.getValue(spinner.getSelectedItemPosition());
    }

    /**
     * Получить выбранную позицию
     * @return
     */
    public int getSelected() {
        int selected = spinner.getSelectedItemPosition();
        if (firstValueIsEmpty) {
            selected -= 1;
        }
        return selected;
    }

    /**
     * Добавить слушателя по выбору позиции из списка
     * @param listener слушатель
     */
    public void addOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public SpinnerInputViewAdapter<T> getAdapter() {
        return adapter;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return DataSavedState.setInteger(spinner.getSelectedItemPosition(), super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        DataSavedState value = (DataSavedState) state;
        super.onRestoreInstanceState(value.getSuperState());
        if (value.integer != 0)
            spinner.setSelection(value.integer);
    }

    /**
     * Адаптер для спиннера
     * @param <T> тип модели
     */
    public static abstract class SpinnerInputViewAdapter<T> extends ArrayAdapter<String> {

        private Context context;
        public List<T> values;
        private final static int textViewResourceId = android.R.layout.simple_list_item_1;
        private int layoutRes = textViewResourceId;
        private boolean firstValueIsEmpty;

        protected SpinnerInputViewAdapter(Context context) {
            super(context, textViewResourceId);
            this.context = context;
            this.values = new ArrayList<>();
        }

        protected SpinnerInputViewAdapter(Context context, List<T> values) {
            super(context, textViewResourceId);
            this.context = context;
            this.values = values;
        }

        SpinnerInputViewAdapter(Context context, int layoutRes, List<T> values) {
            super(context, layoutRes);
            this.layoutRes = layoutRes;
            this.context = context;
            this.values = values;
        }

        public int getCount() {
            if (values == null)
                return 0;
            return firstValueIsEmpty ? values.size() + 1 : values.size();
        }

        public String getItem(int position){
            if (firstValueIsEmpty) {
                if (position == 0) {
                    return "";
                } else {
                    return getValue(values.get(position - 1));
                }
            }
            return getValue(values.get(position));
        }

        public long getItemId(int position){
            return position;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = View.inflate(context, layoutRes, null);
            TextView label = (TextView) convertView;

            label.setText(getItem(position));
            return convertView;
        }

        public void setValues(List<T> values) {
            this.values.clear();
            this.values.addAll(values);
            this.notifyDataSetChanged();
        }

        @Nullable
        T getValue(int position) {
            if (firstValueIsEmpty) {
                if (position == 0 || position == AdapterView.INVALID_POSITION) {
                    return null;
                } else {
                    return values.get(position - 1);
                }
            } else if (position == -1) {
                return null;
            }
            return values.get(position);
        }

        public List<T> getValues() {
            return values;
        }

        public abstract String getValue(T value);

        public void update(List<T> values) {
            this.values = values;
            notifyDataSetChanged();
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(SpinnerInputView<?> spinnerInputView, int position);
    }
}
