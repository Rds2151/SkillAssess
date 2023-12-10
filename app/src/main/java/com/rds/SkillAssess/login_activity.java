package com.rds.SkillAssess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class login_activity extends AppCompatActivity {
    TextView btnSignUp;
    TextView btnForget;
    EditText etEmailAddr;
    Button btnLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        etEmailAddr = findViewById(R.id.etEmailAddr);
        btnForget = findViewById(R.id.forget);
        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        etEmailAddr.setText(intent.getStringExtra("email"));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etPsswd = findViewById(R.id.etPsswd);

                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                DataConnectivity dc = new DataConnectivity(etEmailAddr.getText().toString(),etPsswd.getText().toString());

                dc.loginUser(getApplicationContext()).thenAccept(result -> {
                    runOnUiThread(() -> {
                        if (result.equals("Login successful")) {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(getApplicationContext(), home_activity.class);
                            startActivity(homeIntent);
                            finish();
                        } else {
                            Toast.makeText(login_activity.this, result, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            btnLogin.setVisibility(View.VISIBLE);
                        }
                    });
                });
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(getApplicationContext(), signup_activity.class);
                startActivity(signupIntent);
                finish();
            }
        });
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgetIntent = new Intent(getApplicationContext(), forget_activity.class);
                startActivity(forgetIntent);
            }
        });
    }
}