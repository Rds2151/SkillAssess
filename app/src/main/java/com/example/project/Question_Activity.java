package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Question_Activity extends AppCompatActivity {

    private RadioGroup optionGroup;
    private TextView questionNo, questionText, timeTxt;
    private ArrayList<QuestionModel> dataList = null;
    private String selectedOptions = null;
    private int timerValue;
    private int initialTimeSeconds;
    private int currentQuestion = 0;
    private Button nextBtn;
    private ProgressBar progressBar;
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initializeViews();
        initializeDataFromIntent();

        setOnBackPressedCallback();

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            uiHandler.post(() -> updateTimerDisplay());
            updateTimer();

            if (initialTimeSeconds < 0) {
                service.shutdown();
                submitData();
            }
        }, 0, 1, TimeUnit.SECONDS);

        displayQuestion();

        setOptionGroupListener();

        nextBtn.setOnClickListener(v -> loadNextQuestion());
    }

    private void initializeViews() {
        questionNo = findViewById(R.id.QuestionNo);
        questionText = findViewById(R.id.questionText);
        timeTxt = findViewById(R.id.timeTxt);
        optionGroup = findViewById(R.id.optionGroup);
        nextBtn = findViewById(R.id.nextBtn);
        progressBar = findViewById(R.id.progress_circular);
    }

    private void initializeDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            timerValue = intent.getIntExtra("Timer", 0);
            dataList = intent.getParcelableArrayListExtra("Subject_Data");

            if (timerValue == 0 || dataList == null) {
                Toast.makeText(this, "Error: Timer or Subject Data not found", Toast.LENGTH_SHORT).show();
                finish();
            }
            initialTimeSeconds = timerValue * 60;
        } else {
            finish();
        }
    }

    private void setOnBackPressedCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                showCloseQuizDialog();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void showCloseQuizDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Question_Activity.this);
        builder.setTitle("Close Quiz?")
                .setMessage("Are you sure you want to close the quiz? Your progress will be lost.")
                .setCancelable(false)
                .setPositiveButton("Close Quiz", (dialogInterface, i) -> {
                    dataList.clear();
                    startActivity(new Intent(this, home_activity.class));
                    finish();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateTimerDisplay() {
        String timerDisplay = formatTime(initialTimeSeconds);
        timeTxt.setText(timerDisplay);
    }

    private void updateTimer() {
        initialTimeSeconds--;
    }

    private void setOptionGroupListener() {
        optionGroup.setOnCheckedChangeListener((radioGroup, id) -> uiHandler.post(() -> {
            getSelectedOptions(radioGroup, id);
        }));
    }

    private void getSelectedOptions(RadioGroup radioGroup, int id) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setBackgroundResource(R.drawable.borderline);
        }

        RadioButton selectedRadioButton = findViewById(id);
        selectedRadioButton.setBackgroundResource(R.drawable.button_pressed);
        selectedOptions = selectedRadioButton.getText().toString();
        selectedRadioButton.setChecked(false);
    }

    private void loadNextQuestion() {
        if (selectedOptions == null) {
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
            return;
        }
        dataList.get(currentQuestion).setSelectedAnswer(selectedOptions);

        if (currentQuestion == timerValue - 2) {
            nextBtn.setText("Submit");
        }
        if (currentQuestion == timerValue - 1) {
            submitData();
            return;
        }
        currentQuestion++;
        selectedOptions = null;
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                displayQuestion();
            }
        });
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
        nextBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        List<Map<String,Object>> data = new ArrayList<>();
        Map<String,Object> total = new HashMap<>();
        int Total_Correct = 0;

        for(int i = 0;i < dataList.size();i++)
        {
            data.add(dataList.get(i).getData());
            if (dataList.get(i).isAnswerCorrect()) {
                Total_Correct++;
            }
        }

        DataQuery dq = new DataQuery();
        dq.submitData(data,Total_Correct, new DataQuery.SubmitDataCallback() {
            @Override
            public void onSubmitData() {
                Intent resultIntent = new Intent(getApplicationContext(), Result.class);
                resultIntent.putParcelableArrayListExtra("data",dataList);
                resultIntent.putExtra("timeValue",timerValue);
                startActivity(resultIntent);
                dataList.clear();
                finish();
            }

            @Override
            public void onSubmitDataFailed(String error) {
                Toast.makeText(Question_Activity.this, ""+error, Toast.LENGTH_SHORT).show();
                nextBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
