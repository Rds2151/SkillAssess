package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class getStarted extends AppCompatActivity {
    Button getStarted;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        getStarted = findViewById(R.id.getstartbtn);
        progressBar = findViewById(R.id.progressBar);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                getStarted.setVisibility(View.INVISIBLE);

                Intent loginIntent = new Intent(getApplicationContext(), login_activity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }
}