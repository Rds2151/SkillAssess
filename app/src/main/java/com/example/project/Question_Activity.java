package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Question_Activity extends AppCompatActivity {

    RadioGroup optionGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        optionGroup = findViewById(R.id.optionGroup);

        optionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    radioButton.setBackgroundResource(R.drawable.borderline);
                }

                RadioButton selectedRadioButton = findViewById(id);
                selectedRadioButton.setBackgroundResource(R.drawable.button_pressed);
            }
        });
    }
}