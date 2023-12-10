package com.rds.SkillAssess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    EditText oldPassword,newPassword,cfPassword;
    Button change;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPassword = findViewById(R.id.etPsswd);
        newPassword = findViewById(R.id.etConfirmPsswd);
        cfPassword = findViewById(R.id.etConfirmPsswd2);
        change = findViewById(R.id.btnChange);
        progressBar = findViewById(R.id.progressBar);

        cfPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!newPassword.getText().toString().trim().equals(cfPassword.getText().toString().trim())) {
                    cfPassword.setError("Password do not match");
                }
            }
        });

        change.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            change.setVisibility(View.INVISIBLE);
            String oldPasswd = oldPassword.getText().toString().trim();
            String passwd = newPassword.getText().toString().trim();
            String cpasswd = cfPassword.getText().toString().trim();

            if (passwd.equals(cpasswd)) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("email",null);
                if (email == null || email.isEmpty()) {
                    Toast.makeText(this, "Error: while fetching Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataConnectivity dataConnectivity = new DataConnectivity(email, oldPasswd, passwd);
                dataConnectivity.changePassword().thenAccept(result -> {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        change.setVisibility(View.VISIBLE);

                        if (result.equals("Password changed successfully!")) {
                            Toast.makeText(ChangePassword.this, result, Toast.LENGTH_SHORT).show();
                            getOnBackPressedDispatcher().onBackPressed();
                        } else {
                            Toast.makeText(this, "" + result, Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            } else {
                Toast.makeText(this, "Passwords do not match. Please check your password.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                change.setVisibility(View.VISIBLE);
            }
        });
    }
}