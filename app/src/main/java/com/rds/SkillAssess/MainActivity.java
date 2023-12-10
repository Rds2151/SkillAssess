package com.rds.SkillAssess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, getStarted.class));
            finish();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
            String email = sharedPreferences.getString("email", "");

            if (!email.trim().isEmpty()) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    startActivity(new Intent(this, home_activity.class));
                    finish();
                }, 2000);
            } else {
                startActivity(new Intent(this, getStarted.class));
                finish();
            }
        }
    }
}
