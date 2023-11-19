package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Question_Activity extends AppCompatActivity {

    RadioGroup optionGroup;
    TextView questionNo,questionText,timeTxt;
    ArrayList<QuestionModel> dataList = null;
    String selectedOptions = null;
    int timerValue;
    int initialTimeSeconds;
    int currentQuestion = 0;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        questionNo = findViewById(R.id.QuestionNo);
        questionText = findViewById(R.id.questionText);
        timeTxt = findViewById(R.id.timeTxt);
        optionGroup = findViewById(R.id.optionGroup);
        nextBtn = findViewById(R.id.nextBtn);

        Intent intent = getIntent();
        if (intent != null) {
            timerValue = intent.getIntExtra("Timer",0);
            dataList = intent.getParcelableArrayListExtra("Subject_Data");

            if (timerValue == 0 || dataList == null) {
                Toast.makeText(this, "Error: Timer or Subject Data not found", Toast.LENGTH_SHORT).show();
                finish();
            }
            initialTimeSeconds = timerValue * 60;
        } else {
            finish();
        }

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            String timerDisplay = formatTime(initialTimeSeconds);
            runOnUiThread(() -> timeTxt.setText(timerDisplay));

            initialTimeSeconds--;
            if (initialTimeSeconds < 0) {
                service.shutdown();
                submitData();
            }
        },0,1, TimeUnit.SECONDS);

        displayQuestion();

        optionGroup.setOnCheckedChangeListener((radioGroup, id) -> {
            getSelectedOptions(radioGroup,id);
        });

        nextBtn.setOnClickListener(v -> {
            loadNextQuestion();
        });


    }

    private void getSelectedOptions(RadioGroup radioGroup,int id) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setBackgroundResource(R.drawable.borderline);
        }

        RadioButton selectedRadioButton = findViewById(id);
        selectedRadioButton.setBackgroundResource(R.drawable.button_pressed);
        selectedOptions = selectedRadioButton.getText().toString();
    }

    private void loadNextQuestion() {
        if (selectedOptions == null) {
            Toast.makeText(this, "Please select a Option", Toast.LENGTH_SHORT).show();
            return;
        }
        dataList.get(currentQuestion).setSelectedAnswer(selectedOptions);
        if (currentQuestion == timerValue-2) nextBtn.setText("Submit");
        if (currentQuestion == timerValue-1) submitData();
        this.currentQuestion++;
        displayQuestion();
    }

    private void displayQuestion() {
        questionNo.setText("Question " + (currentQuestion + 1) + " / " + timerValue);
        questionText.setText(dataList.get(currentQuestion).getQuestion());

        for (int i = 0; i < optionGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) optionGroup.getChildAt(i);
            radioButton.setText(dataList.get(currentQuestion).getOptions().get(i));
            radioButton.setBackgroundResource(R.drawable.borderline);
        }
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void submitData() {

    }
}