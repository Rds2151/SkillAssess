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

import java.util.concurrent.CompletableFuture;

public class signup_activity extends AppCompatActivity {
    TextView btnLogin;
    Button btnSignUp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                btnSignUp.setVisibility(View.INVISIBLE);

                EditText etEmailAddr = findViewById(R.id.etEmailAddr);
                EditText etPsswd = findViewById(R.id.etPsswd);
                EditText etConfirmPsswd = findViewById(R.id.etConfirmPsswd);

                DataConnectivity dc = new DataConnectivity(etEmailAddr.getText().toString(),etPsswd.getText().toString(),etConfirmPsswd.getText().toString());

                dc.signUpUser().thenAccept(result -> {
                    runOnUiThread(() -> {
                        Toast.makeText(signup_activity.this, result, Toast.LENGTH_SHORT).show();
                        if (result.equals("Registration successful. Verification email sent.")) {
                            // Sign-up was successful, navigate to another activity
                            Intent intent = new Intent(signup_activity.this, login_activity.class);
                            intent.putExtra("email",dc.email_addr);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            progressBar.setVisibility(View.INVISIBLE);
                            btnSignUp.setVisibility(View.VISIBLE);
                        }
                    });
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(), login_activity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

}