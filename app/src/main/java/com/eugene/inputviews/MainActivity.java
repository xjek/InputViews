package com.eugene.inputviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eugene.inputviews.inputView.DateInputView;
import com.eugene.inputviews.inputView.SpinnerInputView;
import com.eugene.inputviews.inputView.TextInputView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputView textInputView;
    private SpinnerInputView<Animal> spinnerInputView;
    private DateInputView dateInputView;

    private List<Animal> animals = new ArrayList<Animal>() {{
        add(new Animal("Коровы"));
        add(new Animal("Козы"));
        add(new Animal("Курицы"));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputView = findViewById(R.id.textInputView);
        spinnerInputView = findViewById(R.id.spinnerInputView);
        dateInputView = findViewById(R.id.dateInputView);

        textInputView.initialize("Название фермы");
        textInputView.setValue("Иркутска ферма");

        spinnerInputView.initialize("Животные", new SpinnerInputView.SpinnerInputViewAdapter<Animal>(this, animals) {
            @Override
            public String getValue(Animal value) {
                return value.name;
            }
        });

        dateInputView.initialize("Дата создания");
        dateInputView.setCurrentDate();

    }

    static class Animal {
        String name;
        Animal(String name) {
            this.name = name;
        }
    }
}
