package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ViewResult extends AppCompatActivity {

    TextView QuestionText,SelectedAns,CorrectAns,optionsText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);

        optionsText = findViewById(R.id.optionsText);

        float density = getResources().getDisplayMetrics().density;

        StringBuilder optionsStringBuilder = new StringBuilder();
        optionsStringBuilder.append("Options:\n");

        int numberOfTabs = (int) (density * 2);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < numberOfTabs; j++) {
                optionsStringBuilder.append("\t");
            }
            optionsStringBuilder.append("o akdjf\n");
        }

        optionsText.setText(optionsStringBuilder.toString());
        optionsText.setTextSize(16); // in sp
        optionsText.setPadding(0, 0, 0, 4);
    }
}