package com.eugene.inputviews.inputView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eugene.inputviews.Language;
import com.eugene.inputviews.R;


/**
 * Базовый класс вью ввода данных
 */
public class BaseInputView extends LinearLayout implements View.OnClickListener, TextWatcher {

    protected LinearLayout mainView; // Родительским вью данного вью является LinearLayout
    //protected TextInputLayout textInputLayout;
    protected TextView titleTextView; // Название
    protected TextView errorTextView; // Ошибка
    protected EditText editText; // Поле ввода
    protected View contaiterWithButtons; // Контейнер для кнопок
    protected View containerEditText; // Контейнер, в котором лежит поле ввода и контейнер для кнопок
    protected ImageView firstButton; // первая кнопка
    protected ImageView secondButton; // вторая кнопка

    private boolean firstButtonIsFill = false; // Первая кнопка назначена

    private boolean isRequired = false; // Обязательное поле

    private String error; // Текст с ошибкой

    boolean isTruncateValue = false; // Обрезать текст для поля

    protected String firstFullTextOfEditText = null; // Первый полный текст поля
    private boolean valueDidChanged = false; // Значение было изменено

    protected Language language = Language.getInstance();

    private int fieldBackground = R.color.colorEnableInputView; // Цвет фона поля
    private boolean isTransparentEditText = true; // Поле является бесцветным


    public BaseInputView(Context context) {
        super(context);
        init();
    }

    public BaseInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Закрытая инициализация
     */
    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_universal, this);
        mainView = view.findViewById(R.id.mainView);
        //textInputLayout = view.findViewById(R.id.textInputLayout);
        titleTextView = view.findViewById(R.id.title);
        errorTextView = view.findViewById(R.id.error);
        editText = view.findViewWithTag("editText");
        contaiterWithButtons = view.findViewById(R.id.containerWithButtons);
        containerEditText = view.findViewById(R.id.containerEditText);
        containerEditText.setBackgroundResource(fieldBackground);
        firstButton = view.findViewById(R.id.firstButton);
        secondButton = view.findViewById(R.id.secondButton);
        firstButton.setVisibility(GONE);
        secondButton.setVisibility(GONE);
        editText.setOnClickListener(this);
        setTextWatcher();

        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.half_standard_margin);
        editText.setPadding(padding, padding, 0, padding);
    }

    public void isTransparentEditText(boolean isTransparent) {
        this.isTransparentEditText = isTransparent;
    }

    // Готово
    public void setFieldBackground(int fieldBackground) {
        this.fieldBackground = fieldBackground;
        if (isEnabled())
            containerEditText.setBackgroundResource(fieldBackground);
    }

    // Не нужно
    /**
     * Установить наблюдателя полю. Данный класс является им уже
     */
    protected void setTextWatcher() {
        editText.addTextChangedListener(this);
    }

    // Готово
    /**
     * Инициализация поля
     * @param hintKeyLanguage ключ названия поля
     * @param isReqiured является ли обзательным
     */
    public void initialize(String hintKeyLanguage, boolean ... isReqiured) {
        String required = "";
        if (this.isRequired = isReqiured.length > 0 && isReqiured[0]) {
            required = " *";
        }
        titleTextView.setText(language.getEveryWordUpperCaseText(hintKeyLanguage) + required);
        if (isTransparentEditText) {
            editText.setBackgroundColor(Color.TRANSPARENT);
        }
        //textInputLayout.setHint(language.getEveryWordUpperCaseText(hintKeyLanguage) + required);
    }

    // Готово
    /**
     * Установить количество линий (высоту) у поля. По умолчанию одна линия
     * @param countLines
     */
    protected void setLinesCount(int countLines) {
        editText.setSingleLine(false);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLines(countLines);
        editText.setMaxLines(countLines);
        editText.setVerticalScrollBarEnabled(true);
        editText.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
    }

    /**
     * Добавить кнопку
     * @param icon id иконки
     */
    public void addButton(int icon) {
        contaiterWithButtons.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        if (!firstButtonIsFill) {
            firstButtonIsFill = true;
            firstButton.setVisibility(VISIBLE);
            firstButton.setImageResource(icon);
            firstButton.setOnClickListener(this);
        } else {
            secondButton.setVisibility(VISIBLE);
            secondButton.setImageResource(icon);
            secondButton.setOnClickListener(this);
        }
    }

    /**
     * Скрыть кнопки
     */
    public void hideButtons() {
        contaiterWithButtons.setVisibility(GONE);
    }

    /**
     * Установить развер текста поля
     * @param dimen id размера
     * @param heightOfEditText id высоты поля
     */
    public void setSizeEditText(int dimen, int ... heightOfEditText) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(dimen));
        if (heightOfEditText.length > 0) {
            RelativeLayout.LayoutParams params;
            int height = getContext().getResources().getDimensionPixelSize(heightOfEditText[0]);
            if (editText.getLayoutParams() == null) {
                params = new RelativeLayout.LayoutParams(height, ViewGroup.LayoutParams.MATCH_PARENT);
            } else {
                params = (RelativeLayout.LayoutParams) editText.getLayoutParams();
                params.height = height;
            }
            editText.setLayoutParams(params);
        }
    }

    /**
     * Установить размер текста названия поля
     * @param dimen id размера
     */
    public void setSizeHint(int dimen) {
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(dimen));
    }

    /**
     * Установить размер текста ошибки
     * @param dimen id размера
     */
    public void setSizeError(int dimen) {
        errorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(dimen));
    }

    /**
     * Установить цвет фона у кнопок
     * @param res
     */
    public void setButtonBackgroundColor(int res) {
         firstButton.setBackgroundResource(res);
         secondButton.setBackgroundResource(res);
         contaiterWithButtons.setBackgroundResource(res);
    }

    /**
     * Показать ошибку
     */
    public void showError() {
        errorTextView.setText(error);
        errorTextView.setVisibility(VISIBLE);
    }

    /**
     * Показать ошибки
     * @param errorKeyLanguage ключ ошибки
     */
    public void showError(String errorKeyLanguage) {
        setError(errorKeyLanguage);
        showError();
    }

    /**
     * Установить ошибку
     * @param errorKeyLanguage ключ ошибки
     */
    public void setError(String errorKeyLanguage) {
        this.error = language.getEveryWordUpperCaseText(errorKeyLanguage);
    }

    /**
     * Скрыть ошибку
     */
    public void hideError() {
        //textInputLayout.setErrorEnabled(false);
        errorTextView.setVisibility(GONE);
    }

    /**
     * Валидно ли поле
     * @return валидно
     */
    public boolean isValid() {
        return true;
    }

    /**
     * Пусто ли поле
     * @return пусто
     */
    public boolean isEmpty() {
        return editText.getText().toString().isEmpty();
    }

    /**
     * Было ли изменено
     * @return изменено
     */
    public boolean didChanged() {
        return valueDidChanged;
    }

    /**
     * Сбросить информацию об изменении
     */
    public void resetChanged() {
        valueDidChanged = false;
    }

    /**
     * Показать ошибку если пусто или не валидно
     * @return пусто или не валидно
     */
    public boolean showErrorIfIsEmptyOrNotValid() {
        if (isRequired && isEmpty() || !isValid()) {
            showError();
            return true;
        } else {
            hideError();
            return false;
        }
    }

    /**
     * Установить разрешение на изменения поля
     * @param enabled разрешено
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        //textInputLayout.setEnabled(enabled);
        titleTextView.setEnabled(enabled);
        editText.setEnabled(enabled);
        firstButton.setEnabled(enabled);
        secondButton.setEnabled(enabled);
        contaiterWithButtons.setVisibility(enabled ? VISIBLE : GONE);
        if (enabled) {
            containerEditText.setBackgroundColor(ContextCompat.getColor(getContext(), fieldBackground));
        } else {
            containerEditText.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * Получиь значение поля
     * @return значение
     */
    public String getFieldValue() {
        return editText.getText().toString();
    }

    /**
     * Получить поле
     * @return поле
     */
    public EditText getEditText() {
        return editText;
    }

    /**
     * Обработка кликов
     * @param view какое-то вью
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.firstButton:
                onFirstButtonClick();
                break;
            case R.id.secondButton:
                onSecondButtonClick();
                break;
            default:
                onEditTextClick();
        }
    }

    protected void onEditTextClick() {

    }

    protected void onFirstButtonClick() {

    }

    protected void onSecondButtonClick() {

    }

    /**
     * Установить текст полю
     * @param text текст
     */
    protected void setText(final String text) {
        hideError(); // Скрать ошибку
        if (text == null || text.isEmpty()) {
            setTextWithTrackChanges("");
            return;
        }

        int width = editText.getWidth() - contaiterWithButtons.getWidth(); // Ширина поля

        if (width == 0) { // Если ширина равна 0
            // Устанавливается специальный слушатель. Вызовется метод onGlobalLayout когда произишли изменения у вью системой андроид. Это значит что вью построилось, значит ширина уже не 0, надо заново попробовать установить текст полю
            this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    BaseInputView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setText(text);
                }
            });
            return;
        }
        width *= editText.getMaxLines(); // Ширина умножается на количество линий поля
        Rect bound = new Rect();
        int lengthOriginalText = text.length();
        int length = 1;
        // В этом цикле устанавливается текст полю, причем обрезается лишний текст, который не влез и ставяться "..." в конце
        while (true) {
            String resultText = text.substring(0, length);

            editText.getPaint().getTextBounds(resultText, 0, resultText.length(), bound);
            if (bound.width() > width) {
                resultText = text.substring(0, length) + "...";
                setTextWithTrackChanges(resultText, text);
                return;

            } else if (length == lengthOriginalText) {
                setTextWithTrackChanges(text);
                return;
            } else {
                length++;
            }
        }
    }

    /**
     * Обновить изменения на текущее значение поля
     */
    public void updateChanges() {
        firstFullTextOfEditText = editText.getText().toString();
        valueDidChanged = false;
    }

    /**
     * Установить текст полю. Содержит цикл, кол-во итерация которого size, значение итератора отдает в listener, который возвращает имя из модели.
     * Используется, например, в OrganizationStructureInputView. Данный inputView содержит массив значений, который нужно вывести в поле через запятую
     * @param listener GetNameFromModelListener
     * @param size размер
     */
    public void setText(GetNameFromModelListener listener, int size) {
        if (size == 0) {
            if (!editText.getText().toString().isEmpty())
                editText.setText("");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(); // Результативная строка
        for (int i = 0; i < size; i++) {
            stringBuilder.append(listener.getName(i)).append(", "); // Берется значение из listener по i, и так же присодиняется запятая
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1); // Обрезается запятая и последний проел
        }
        setText(stringBuilder.toString()); // Отправлятеся на устрановку данного результата в поле
    }

    /**
     * Установить значение поле с фиксированием изменений
     * @param text текст
     * @param fullText так же можно отправить полный текст, в данном случает парамметр text может быть обрезанным по ширине поля
     */
    protected void setTextWithTrackChanges(String text, String... fullText) {
        if (!valueDidChanged) {
            String tempFullText = fullText.length > 0 ? fullText[0] : text;
            if (firstFullTextOfEditText == null) {
                firstFullTextOfEditText = tempFullText;
            } else if (!firstFullTextOfEditText.equals(tempFullText)) {
                valueDidChanged = true;
            }
        }
        if (editText.getText().toString().isEmpty() && text.isEmpty()) {
            return;
        }
        editText.setText(text);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    /**
     * Вызывает после ввода/удаление символа
     * @param editable -
     */
    @Override
    public void afterTextChanged(Editable editable) {
        showErrorIfIsEmptyOrNotValid();
    }

    /**
     * Получить имя из модели
     */
    public interface GetNameFromModelListener {
        String getName(int position);
    }
}
