package com.eugene.inputviews.inputView;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.eugene.inputviews.R;
import com.eugene.inputviews.inputView.util.CustomCheckBox;
import com.eugene.inputviews.inputView.util.GetIdListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Выпадающий список, который содержит чекбоксы
 * @param <T> тип модели
 */
public class CheckBoxSpinnerInputView<T> extends BaseInputView {

    private Spinner spinner; // Выпадающий список
    private CheckBoxSpinnerAdapter<T> adapter; // Адаптер для выпадающего списка

    public CheckBoxSpinnerInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        spinner = new Spinner(context);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        mainView.addView(spinner);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    /**
     * Инициализация
     * @param hintKeyLanguage ключ названия
     * @param adapter Адаптер для выпадающего списка
     * @param isReqiured является ли обязательным полем
     */
    public void initialize(String hintKeyLanguage, CheckBoxSpinnerAdapter<T> adapter, boolean ... isReqiured) {
        super.initialize(hintKeyLanguage, isReqiured);
        addButton(R.drawable.ic_arrow_down);
        addButton(R.drawable.ic_trash);
        this.adapter = adapter;
        adapter.setBaseInputView(this);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getValues().size());
    }

    /**
     * Установить выбранные значения
     * @param selection массив
     */
    public void setSelection(List<T> selection) {
        adapter.setSelection(selection);
    }

    /**
     * Устрановть выбранные значения
     * @param ids массив с id моделей
     * @param getIdListener с помощью этого слушателя получается id модели
     */
    public void setSelection(List<String> ids, GetIdListener<T> getIdListener) {
        adapter.setSelection(ids, getIdListener);
    }

    /**
     * Установть выбранные значения
     * @param selection массив с моделями
     */
    public void setSelection(ArrayList<T> selection) {
        adapter.setSelection(selection);
    }

    public List<T> getSelectedModels() {
        return adapter.getSelectedValues();
    }

    /**
     * Получить id-шники выбранных значений
     * @param getIdListener с помощью этого слушателя получается id модели
     * @return
     */
    public List<String> getSelectedIds(GetIdListener<T> getIdListener) {
        List<String> ids = new ArrayList<>();
        for (T item: getSelectedModels()) {
            ids.add(getIdListener.getId(item));
        }
        return ids;
    }

    @Override
    protected void onFirstButtonClick() {
        spinner.performClick();
    }

    @Override
    protected void onSecondButtonClick() {
        setSelection(new ArrayList<T>());
    }

    @Override
    protected void onEditTextClick() {
        spinner.performClick();
    }

    /**
     * Универсальный адаптер спиннера
     * @param <T> тип модели
     */
    public static abstract class UniversalSpinnerAdapter<T> extends ArrayAdapter<String> {

        private Context context;
        private List<T> values;
        private final static int textViewResourceId = android.R.layout.simple_list_item_1;
        protected int layoutRes = textViewResourceId;

        public UniversalSpinnerAdapter(Context context) {
            super(context, textViewResourceId);
            this.context = context;
            this.values = new ArrayList<>();
        }

        public UniversalSpinnerAdapter(Context context, List<T> values) {
            super(context, textViewResourceId);
            this.context = context;
            this.values = values;
        }

        UniversalSpinnerAdapter(Context context, int layoutRes, List<T> values) {
            super(context, layoutRes);
            this.layoutRes = layoutRes;
            this.context = context;
            this.values = values;
        }

        public int getCount(){
            return values.size();
        }

        public String getItem(int position){
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
            label.setText(getValue(values.get(position)));
            return convertView;
        }

        public void setValues(List<T> values) {
            this.values.clear();
            this.values.addAll(values);
            this.notifyDataSetChanged();
        }

        T getValue(int position) {
            return values.get(position);
        }

        public List<T> getValues() {
            return values;
        }

        public abstract String getValue(T value);
    }

    /**
     * Адаптер с чекбоксами
     * @param <T>
     */
    public static abstract class CheckBoxSpinnerAdapter<T> extends UniversalSpinnerAdapter<T> implements OnClickListener {

        private final static int textViewResourceId = R.layout.view_custom_checkbox;

        private SparseArray<String> checkedValues = new SparseArray<>();

        private BaseInputView baseInputView;

        public CheckBoxSpinnerAdapter(Context context, List<T> values) {
            super(context, textViewResourceId, values);
        }

        public CheckBoxSpinnerAdapter(Context context) {
            super(context, textViewResourceId, new ArrayList<T>());
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            CustomCheckBox checkBox;
            if (convertView == null) {
                convertView = View.inflate(getContext(), layoutRes, null);
                checkBox = (CustomCheckBox) convertView;
                checkBox.setOnClickListener(this);
            } else
                checkBox = (CustomCheckBox) convertView;
            if (getValues().size() == position) {
                checkBox.setText("");
                checkBox.setVisibility(GONE);
                return checkBox;
            }
            checkBox.setTag(position);
            convertView.setVisibility(VISIBLE);
            boolean hasOnCheck = checkedValues.get(position) != null;
            checkBox.setCheckedOnClick(hasOnCheck);
            checkBox.setText(getValue(getValues().get(position)));
            return checkBox;
        }

        @Override
        public int getCount() {
            return super.getCount() + 1;
        }

        @Override
        public String getItem(int position) {
            if (getValues().size() == position) {
                return "";
            }
            return super.getItem(position);
        }

        @Override
        public void onClick(View v) {
            CustomCheckBox checkBox = (CustomCheckBox) v;
            checkBox.setCheckedOnClick(!checkBox.isChecked());
            int position = (int) checkBox.getTag();
            if (checkBox.isChecked()) {
                String text = checkBox.getText().toString();
                if (!text.isEmpty())
                    checkedValues.put(position, checkBox.getText().toString());
                else
                    return;
            } else {
                checkedValues.delete(position);
            }
            outResult();
        }


        void outResult() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < checkedValues.size(); i++) {
                String text = checkedValues.get(checkedValues.keyAt(i));
                stringBuilder.append(text).append(", ");
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);
            }
            baseInputView.setText(stringBuilder.toString());
        }

        List<T> getSelectedValues() {
            List<T> result = new ArrayList<>();
            for (int i = 0; i < checkedValues.size(); i++) {
                result.add(getValues().get(checkedValues.keyAt(i)));
            }
            return result;
        }

        public void setSelection(List<String> ids, GetIdListener<T> getIdListener) {
            if (!ids.isEmpty()) {
                for (int i = 0; i < getValues().size(); i++) {
                    T value = getValues().get(i);
                    if (ids.contains(getIdListener.getId(value))) {
                        checkedValues.append(i, getValue(value));
                    }
                }
            } else {
                checkedValues.clear();
            }
            outResult();
        }

        public void setSelection(List<T> selection) {
            if (selection.size() > 0) {
                for (int i = 0; i < getValues().size(); i++) {
                    T value = getValues().get(i);
                    if (selection.contains(value)) {
                        checkedValues.append(i, getValue(value));
                    }
                }
            } else {
                checkedValues.clear();
            }

            outResult();
        }

        void setBaseInputView(BaseInputView baseInputView) {
            this.baseInputView = baseInputView;
        }
    }
}
