package com.eugene.inputviews;

import android.app.Activity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Работа с переводом
 */
public class Language {

    private static Language instance;
    private JSONObject data;

    private static final String TEXT_VIEW_CLASS_NAME = AppCompatTextView.class.getName();
    public static final String TEMPLATE_OF_KEY = "$\\";

    public static Language getInstance() {
        if (instance == null) {
            instance = new Language();
        }
        return instance;
    }

    /**
     * Получиь перевод, где каждыое слово начинается с большой буквы
     * @param key ключ
     * @return перевод
     */
    public String getEveryWordUpperCaseText(String key) {
        String text = getText(key).trim().replaceAll("  ", " ");
        if (!text.isEmpty()) {
            StringBuilder resultText = new StringBuilder();
            for (String word: text.split(" ")) {
                char[] textArray = word.toCharArray();
                if (resultText.length() == 0 || !(word.equals("as") || word.equals("to") || word.equals("is") || word.equals("the"))) {
                    textArray[0] = Character.toUpperCase(textArray[0]);
                }
                resultText.append(new String(textArray)).append(" ");
            }
            text = resultText.toString().trim();
        }
        return text;
    }

    /**
     * Получиь перевод, где первая буква является большой
     * @param key ключ
     * @return первод
     */
    public String getUpperCaseText(String key) {
        String text = getText(key);
        if (!text.isEmpty()) {
            //text = text.toLowerCase();
            char[] textArray = text.toCharArray();
            textArray[0] = Character.toUpperCase(textArray[0]);
            text = new String(textArray);
        }
        return text;
    }

    /**
     * Получиь перевод, где каждыое слово является маленькой
     * @param key ключ
     * @return перевод
     */
    public String getLowerCaseText(String key) {
        return getText(key).toLowerCase();
    }

    /**
     * Получиь перевод, где каждая буква является большой
     * @param key
     * @return
     */
    public String getEveryLetterUpperCaseText(String key) {
        return getText(key).toUpperCase();
    }

    /**
     * Получить перевод
     * @param key ключ
     * @return первод
     */
    public synchronized String getText(String key) {
        try {
            if (!data.isNull(key)) {
                return data.getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (key != null && !key.isEmpty())
            return  key.replaceAll("_", " ");
        return "";
    }

    /**
     * Установить переведенный текст кнопки
     * @param button кнопка
     * @param key ключ
     * @return Language
     */
    public Language setText(Button button, String key) {
        String text = getText(key);
        if (!text.isEmpty()) {
            button.setText(text);
        }
        return this;
    }

    /**
     * Установить переведенный текст textView. И этот textView явяется обязательным (добавляется *)
     * @param view родительский view
     * @param id id textView
     * @param key ключ
     * @return Language
     */
    public Language setRequiredTextView(View view, int id, String key) {
        TextView textView = view.findViewById(id);
        String text = getEveryWordUpperCaseText(key);
        if (!text.isEmpty()) {
            textView.setText(text + " *");
        }
        return this;
    }

    /**
     * Установить перевод подсказке
     * @param editText EditText
     * @param key ключ
     * @return Language
     */
    public Language setHintEditText(EditText editText, String key) {
        editText.setHint(getEveryWordUpperCaseText(key));
        return this;
    }

    /**
     * Установить перевод вью, где каждая первая буква слов большая
     * @param activity активность
     * @param resId id вью
     * @param key ключ
     * @return Language
     */
    public Language setEveryWordUpperCaseText(Activity activity, int resId, String key) {
        return setText(activity.findViewById(resId), getEveryWordUpperCaseText(key));
    }

    /**
     * Установить перевод вью, где словосочетание начинается с большой буквы
     * @param activity активность
     * @param resId id вью
     * @param key ключ
     * @return Language
     */
    public Language setUpperCaseText(Activity activity, int resId, String key) {
        return setText(activity.findViewById(resId), getUpperCaseText(key));
    }

    /**
     * Установить перевод вью, где все с маленькой буквы
     * @param activity активность
     * @param resId id вью
     * @param key ключ
     * @return Language
     */
    public Language setLowerCaseText(Activity activity, int resId, String key) {
        return setText(activity.findViewById(resId), getLowerCaseText(key));
    }

    /**
     * Установить перевод вью,где каждое слово начинается с большой буквы
     * @param view вью
     * @param key ключ
     * @return Language
     */
    public Language setEveryWordUpperCaseText(View view, String key) {
        return setText(view, getEveryWordUpperCaseText(key));
    }

    /**
     * Установить перевод, где первая буква текста большая
     * @param view вью
     * @param key клю
     * @return Language
     */
    public Language setUpperCaseText(View view, String key) {
        return setText(view, getUpperCaseText(key));
    }

    public Language setLowerCaseText(TextView view, String key) {
        return setText(view, getLowerCaseText(key));
    }

    /**
     * Установить текс
     * @param view вью
     * @param text текст
     * @return Language
     */
    public Language setText(View view, String text) {
        if (view != null) {
            if (!text.isEmpty()) {
                if (view instanceof Button) {
                    ((Button) view).setText(text);
                } else if (view instanceof TextView) {
                    ((TextView) view).setText(text);
                }
            }
        }

        return this;
    }

    /**
     * Установить перевод вью, где каждая буква слова с большой буквы
     */
    public Language setEveryWordUpperCaseText(View view, int resId, String key) {
        return setText(view.findViewById(resId), getEveryWordUpperCaseText(key));
    }

    /**
     * Установить перевод вью
     */
    public Language setText(View view, int resId, String key) {
        return setText(view.findViewById(resId), getText(key));
    }

    /**
     * Установтиь перевод вью, где текст начинается с большой буквы
     */
    public Language setUpperCaseText(View view, int resId, String key) {
        return setText(view.findViewById(resId), getUpperCaseText(key));
    }

    /**
     * Установить перевод вью, где весь текст маленькими буквами
     */
    public Language setLowerCaseText(View view, int resId, String key) {
        return setText(view.findViewById(resId), getLowerCaseText(key));
    }

    /**
     * Установть json с переводами
     * @param data json
     */
    public void setData(JSONObject data) {
        this.data = data;
    }


}