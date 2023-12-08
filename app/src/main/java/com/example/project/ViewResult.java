package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
            finish();
        });
    }

    private void loadQuestion(ArrayList<QuestionModel> questionModels) {
        ResultAdapter resultAdapter = new ResultAdapter(questionModels);
        recyclerView.setAdapter(resultAdapter);
    }
}