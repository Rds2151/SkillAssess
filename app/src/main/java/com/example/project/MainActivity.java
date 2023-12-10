package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends AppCompatActivity {

    protected final static profileDetail profileDetail = new profileDetail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        if (!email.trim().isEmpty()) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(this, home_activity.class));
                finish();
            }, 2000);
        }else {
            startActivity(new Intent(this, getStarted.class));
            finish();
        }
    }
}
