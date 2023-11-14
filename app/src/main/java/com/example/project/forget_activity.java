package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class forget_activity extends AppCompatActivity {

    EditText etEmailAddr;
    TextView signUp;
    Button resetBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        etEmailAddr = findViewById(R.id.etEmailAddr);
        signUp = findViewById(R.id.btnSignUp);
        resetBtn = findViewById(R.id.resetBtn);
        progressBar = findViewById(R.id.progressBar);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                resetBtn.setVisibility(View.INVISIBLE);
                DataConnectivity dc = new DataConnectivity(etEmailAddr.getText().toString());

                dc.resetPassword().thenAccept(result -> {
                    runOnUiThread(() -> {
                        Toast.makeText(forget_activity.this, result, Toast.LENGTH_SHORT).show();
                        if (result.equals("Password reset email sent successfully. Please check your email inbox.")) {
                            Intent intent = new Intent(forget_activity.this, login_activity.class);
                            intent.putExtra("email",dc.email_addr);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            resetBtn.setVisibility(View.VISIBLE);
                        }
                    });
                });

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(forget_activity.this, signup_activity.class));
                finish();
            }
        });
    }
}