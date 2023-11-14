package com.example.project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {

    LinearLayout btnSignOut, leaderBtn, historyBtn, editBtn, changePasswdBtn, privacyBtn, forwardBtn;
    ImageView profileImg;
    TextView profileTxt;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        btnSignOut = rootView.findViewById(R.id.btnSignOut);
        leaderBtn = rootView.findViewById(R.id.leaderBtn);
        historyBtn = rootView.findViewById(R.id.historyBtn);
        editBtn = rootView.findViewById(R.id.editBtn);
        changePasswdBtn = rootView.findViewById(R.id.changePasswdBtn);
        privacyBtn = rootView.findViewById(R.id.privacyBtn);
        profileImg = rootView.findViewById(R.id.profileImg);
        profileTxt = rootView.findViewById(R.id.profiletxt);
        forwardBtn = rootView.findViewById(R.id.forwardBtn);

        if (MainActivity.profileDetail.getprofileUri() != null) {
            profileImg.setImageURI(MainActivity.profileDetail.getprofileUri());
        }

        if (MainActivity.profileDetail.getFullName() != null) {
            profileTxt.setText(MainActivity.profileDetail.getFullName());
        }

        btnSignOut.setOnClickListener(this::logout);

        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), profile_activity.class));
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), profile_activity.class));
            }
        });

        return rootView;
    }

    public void logout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Log Out?")
                .setMessage("Are you sure, you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("email");
                        editor.apply();
                        startActivity(new Intent(requireActivity(), login_activity.class));
                        requireActivity().finish(); // Optionally finish the current activity
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.profileDetail.getprofileUri() != null) {
            profileImg.setImageURI(MainActivity.profileDetail.getprofileUri());
        }

        if (MainActivity.profileDetail.getFullName() != null) {
            profileTxt.setText(MainActivity.profileDetail.getFullName());
        }
    }
}
