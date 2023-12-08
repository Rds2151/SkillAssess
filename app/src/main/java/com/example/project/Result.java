package com.example.project;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import java.util.ArrayList;

public class Result extends AppCompatActivity {
    TextView percentTextView, resultDetail, header_title;
    Button viewResultBtn;
    ImageView cancel;
    private int timerValue;
    private ArrayList<QuestionModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        timerValue = intent.getIntExtra("timeValue",0);
        dataList = intent.getParcelableArrayListExtra("data");

        percentTextView = findViewById(R.id.percent);
        resultDetail = findViewById(R.id.result_detail);
        header_title = findViewById(R.id.header_title);
        cancel = findViewById(R.id.cancel_button);
        viewResultBtn = findViewById(R.id.ViewResult);

        int totalCorrectAns = 0;
        for (int i = 0; i < timerValue; i++) {
            if (dataList.get(i).isAnswerCorrect()) {
                totalCorrectAns++;
            }
        }

        if (totalCorrectAns != 0) {
            int percentage = ((totalCorrectAns*100)/timerValue);

            resultDetail.setText("You attempted "+timerValue+" questions and got "+totalCorrectAns+" correct answers.");
            percentTextView.setText(percentage+"% Score");
            if (percentage > 60) {
                header_title.setText("Congrats!");
            } else {
                header_title.setText("Failed!");
            }
        }

        cancel.setOnClickListener(v -> {
            Intent reintent = new Intent(Result.this, home_activity.class);
            reintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(reintent);
            finish();
        });

        viewResultBtn.setOnClickListener(v -> {
            Intent viewResultIntent = new Intent(Result.this,ViewResult.class);
            viewResultIntent.putParcelableArrayListExtra("data",dataList);
            viewResultIntent.putExtra("timeValue",timerValue);
            startActivity(viewResultIntent);
            finish();
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent reintent = new Intent(Result.this, home_activity.class);
                reintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(reintent);
                finish();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}