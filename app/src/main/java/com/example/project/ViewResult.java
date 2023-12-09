package com.example.project;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewResult extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton backBtn;
    private int timeValue;
    private ArrayList<QuestionModel> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);

        Intent intent = getIntent();
        timeValue = intent.getIntExtra("timeValue",0);
        dataList = intent.getParcelableArrayListExtra("data");

        if (dataList == null || dataList.isEmpty()) {
            finish();
        }

        recyclerView = findViewById(R.id.resultRV);
        backBtn = findViewById(R.id.backBtn);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        loadQuestion(dataList);

        backBtn.setOnClickListener(v -> {
            Intent Viewintent = new Intent(ViewResult.this, home_activity.class);
            Viewintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Viewintent);
            finish();
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backBtn.performClick();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void loadQuestion(ArrayList<QuestionModel> questionModels) {
        ResultAdapter resultAdapter = new ResultAdapter(questionModels);
        recyclerView.setAdapter(resultAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataList.clear();
    }
}