package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

public class home_activity extends AppCompatActivity {
    ImageView homebtn,recentbtn,settingbtn;
    int color;


        private Fragment currentFragment; // Track the currently selected fragment

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            homebtn = findViewById(R.id.homeImg);
            recentbtn = findViewById(R.id.recentImg);
            settingbtn = findViewById(R.id.settingImg);

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
            currentFragment = new HomeFragment();
            updateButtonColor(homebtn);
            replaceFragment(currentFragment);
        }

        @Override
        public void onBackPressed() {
            // Check if the current fragment is not null
            if (currentFragment != null) {
                if (currentFragment instanceof HomeFragment) {
                    updateButtonColor(homebtn);
                } else if (currentFragment instanceof RecentFragment) {
                    updateButtonColor(recentbtn);
                } else if (currentFragment instanceof SettingFragment) {
                    updateButtonColor(settingbtn);
                }
            }
            super.onBackPressed();
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
            currentFragment = fragment; // Update the current fragment
        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//
//        homebtn = findViewById(R.id.homeImg);
//        recentbtn = findViewById(R.id.recentImg);
//        settingbtn = findViewById(R.id.settingImg);
//
//        TypedValue typedValue = new TypedValue();
//        getTheme().resolveAttribute(R.attr.home_color, typedValue, true);
//        color = typedValue.data;
//
//        replaceFragment(new HomeFragment(),homebtn,recentbtn,settingbtn);
//    }
//
//    public void homeClick(View view) {
//        replaceFragment(new HomeFragment(),homebtn,recentbtn,settingbtn);
//    }
//
//    public void recentClick(View view) {
//        replaceFragment(new RecentFragment(),recentbtn,homebtn,settingbtn);
//    }
//
//    public void settingClick(View view) {
//        replaceFragment(new SettingFragment(),settingbtn,homebtn,recentbtn);
//    }
//
//    private void replaceFragment(Fragment fragment, ImageView choosen, ImageView grey, ImageView grey2)
//    {
//        choosen.setColorFilter(color);
//        grey.setColorFilter(ContextCompat.getColor(this, R.color.nav_img));
//        grey2.setColorFilter(ContextCompat.getColor(this, R.color.nav_img));
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment,fragment);
//        fragmentTransaction.commit();
//    }
//}