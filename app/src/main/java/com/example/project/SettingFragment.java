package com.example.project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
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

        profileImg = rootView.findViewById(R.id.profileImg);
        profileTxt = rootView.findViewById(R.id.profiletxt);

        Picasso.get()
                .load(MainActivity.profileDetail.getprofileUri())
                .error(R.drawable.usericon)
                .into(profileImg);

        if (MainActivity.profileDetail.getFullName() != null) {
            profileTxt.setText(MainActivity.profileDetail.getFullName());
        }

        btnSignOut = rootView.findViewById(R.id.btnSignOut);
        leaderBtn = rootView.findViewById(R.id.leaderBtn);
        historyBtn = rootView.findViewById(R.id.historyBtn);
        editBtn = rootView.findViewById(R.id.editBtn);
        changePasswdBtn = rootView.findViewById(R.id.changePasswdBtn);
        privacyBtn = rootView.findViewById(R.id.privacyBtn);
        forwardBtn = rootView.findViewById(R.id.forwardBtn);

        btnSignOut.setOnClickListener(this::logout);

        historyBtn.setOnClickListener(v -> {
            Intent recent = new Intent(requireActivity(), home_activity.class);
            recent.putExtra("Fragment","Recent");
            startActivity(recent);
            requireActivity().finish();
        });

        forwardBtn.setOnClickListener(view -> editBtn.performClick());

        editBtn.setOnClickListener(view -> startActivity(new Intent(requireActivity(), profile_activity.class)));

        return rootView;
    }

    public void logout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Log Out?")
                .setMessage("Are you sure, you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Log out", (dialogInterface, i) -> {
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(requireActivity(), login_activity.class));
                    requireActivity().finish(); // Optionally finish the current activity
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.get()
                .load(MainActivity.profileDetail.getprofileUri())
                .error(R.drawable.usericon)
                .into(profileImg);

        if (MainActivity.profileDetail.getFullName() != null) {
            profileTxt.setText(MainActivity.profileDetail.getFullName());
        }
    }
}
