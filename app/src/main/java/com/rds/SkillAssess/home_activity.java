package com.rds.SkillAssess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

public class home_activity extends AppCompatActivity {
    ImageView homebtn,recentbtn,settingbtn;
    String action = null;

    protected final static profileDetail profileDetail = new profileDetail();
    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homebtn = findViewById(R.id.homeImg);
        recentbtn = findViewById(R.id.recentImg);
        settingbtn = findViewById(R.id.settingImg);

        Intent intent = getIntent();
        if ( intent != null) {
            action = intent.getStringExtra("Fragment");
        }

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.home_color, typedValue, true);
        color = typedValue.data;

        // Initialize the button click listeners
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateButtonColor(homebtn);
                replaceFragment(new HomeFragment());
            }
        });

        recentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateButtonColor(recentbtn);
                replaceFragment(new RecentFragment());
            }
        });

        settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateButtonColor(settingbtn);
                replaceFragment(new SettingFragment());
            }
        });

        // Start with the initial fragment and button color
        if (action != null && action.equals("Recent")) {
            recentbtn.performClick();
        } else {
            homebtn.performClick();
        }
    }

    private void updateButtonColor(ImageView newSelectedButton) {
        // Reset the color of all buttons
        homebtn.setColorFilter(ContextCompat.getColor(this, R.color.nav_img));
        recentbtn.setColorFilter(ContextCompat.getColor(this, R.color.nav_img));
        settingbtn.setColorFilter(ContextCompat.getColor(this, R.color.nav_img));

        // Update the selected button and its color
        newSelectedButton.setColorFilter(color);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }
}