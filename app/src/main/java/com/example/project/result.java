package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class result extends AppCompatActivity {
    TextView percentTextView, resultDetail, header_title;
    ImageView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        int timerValue = intent.getIntExtra("timeValue",0);
        ArrayList<QuestionModel> dataList = intent.getParcelableArrayListExtra("data");

        percentTextView = findViewById(R.id.percent);
        resultDetail = findViewById(R.id.result_detail);
        header_title = findViewById(R.id.header_title);
        cancel = findViewById(R.id.cancel_button);

        int totalCorrectAns = 0;
        for (int i = 0; i < timerValue; i++) {
            if (dataList.get(i).isAnswerCorrect()) {
                totalCorrectAns++;
            }
        }

        if (totalCorrectAns != 0) {
            int percentage = ((totalCorrectAns*100)/timerValue);
            Toast.makeText(this, timerValue+"", Toast.LENGTH_SHORT).show();

            resultDetail.setText("You attempted "+timerValue+" questions and got "+totalCorrectAns+" correct answers.");
            percentTextView.setText(percentage+"% Score");
            if (percentage > 60) {
                header_title.setText("Congrats!");
            } else {
                header_title.setText("Failed!");
            }
        }

        cancel.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
}