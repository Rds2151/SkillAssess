package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseUser;

import java.net.URI;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class profile_activity extends AppCompatActivity {

    ImageButton imageButton;
    ImageView editprofileImg;
    EditText firstName,lastName,emailTxt,birthDate;
    AlertDialog alertDialog;
    Button cancel;
    Uri profileUri = null;
    RadioGroup genderRG;
    private String gender = null;
    int year,month,day;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageButton = findViewById(R.id.imageButton);
        editprofileImg = findViewById(R.id.editprofileImg);
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        emailTxt = findViewById(R.id.emailTxt);
        birthDate = findViewById(R.id.birthDate);
        genderRG = findViewById(R.id.gender);
        cancel = findViewById(R.id.cancel_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_progress_dialog, null);
        builder.setView(dialogView);

        alertDialog = builder.create();
        ColorDrawable transparent = new ColorDrawable(android.graphics.Color.TRANSPARENT);
        alertDialog.getWindow().setBackgroundDrawable(transparent);
        alertDialog.setCancelable(false);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData",Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email","");
        emailTxt.setText(email);

        Map<String,Object> data = MainActivity.profileDetail.getData();

        firstName.setText((CharSequence) data.get("first_Name"));
        lastName.setText((CharSequence) data.get("last_Name"));
        birthDate.setText((CharSequence) data.get("Birthdate"));
        editprofileImg.setImageURI((Uri)data.get("profileUri"));
        this.profileUri = (Uri)data.get("profileUri");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(profile_activity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                TextView errorFname = findViewById(R.id.errorFn);
                if(!hasFocus) {
                    if(validation(firstName,R.id.errorFn) == null) {
                        errorFname.setText("");
                    }
                }
            }
        });
        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                TextView errorLname = findViewById(R.id.errorLn);
                if(!hasFocus) {
                    if(validation(lastName,R.id.errorLn) == null) {
                        errorLname.setText("");
                    }
                }
            }
        });

        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                gender = radioButton.getText().toString();
            }
        });

        calendar = Calendar.getInstance();
        birthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {datePicker();}}});
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {datePicker();}});
    }

    private void datePicker()
    {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(profile_activity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                birthDate.setText(datePicker.getDayOfMonth() + "/" + (i1 + 1) + "/" + datePicker.getYear()); // Adjust month +1
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    @Nullable
    private String validation(@NonNull EditText editText,int id) {
        TextView error = findViewById(id);
        String name = editText.getText().toString().trim();
        if (name.isEmpty()) {
            error.setText("Name field cannot be empty.");
        } else if (!name.matches("[a-zA-Z]+")) {
            error.setText("Name must contain only letters.");
        } else if ("null".equals(name) || "NA".equals(name)) {
            error.setText("Invalid value for the name.");
        } else {
            error.setText("");
        }

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        profileUri = data.getData();
        editprofileImg.setImageURI(profileUri);
    }
    public void saveData(View view) {
        String fname = firstName.getText().toString().trim();
        String lname = lastName.getText().toString().trim();
        String date = birthDate.getText().toString();

        if (validation(firstName, R.id.errorFn) != null) {
            return; // Input validation failed
        }

        if (validation(lastName, R.id.errorLn) != null) {
            return; // Input validation failed
        }

        if (profileUri == null) {
            Toast.makeText(this, "Please set a profile image.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!date.isEmpty() && !date.matches("^([1-9]|[12][0-9]|3[01])/([1-9]|1[0-2])/(\\d{4})$")) {
            Toast.makeText(this, "Invalid date format. Please use the format dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
            return;
        }

        alertDialog.show();
        if (!fname.isEmpty()) {
            fname = fname.substring(0, 1).toUpperCase() + fname.substring(1);
        }

        if (!lname.isEmpty()) {
            lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);
        }

        DataConnectivity dc = new DataConnectivity(emailTxt.getText().toString());
        dc.updateProfile(profileUri, fname + " " + lname, date, gender)
                .thenAccept(result -> {
                    runOnUiThread(() -> {
                        alertDialog.cancel();
                        if (result.equals("Profile updated successfully")) {
                            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                            goBack(view);
                        } else {
                            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .exceptionally(e -> {
                    runOnUiThread(() -> {
                        alertDialog.cancel();
                        Toast.makeText(this, "An error occurred while updating the profile.", Toast.LENGTH_SHORT).show();
                    });
                    return null;
                });
    }

    public void goBack(View view) {
        onBackPressed();
    }
}