package com.example.project;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetTimer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetTimer extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int selectedTimer = -1;

    private String Subject_Id;
    private String subjectName;

    private Button btn15 , btn30 , btn60;
    public SetTimer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetTimer.
     */
    // TODO: Rename and change types and number of parameters
    public static SetTimer newInstance(String param1, String param2) {
        SetTimer fragment = new SetTimer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_set_timer, container, false);

        Bundle args = getArguments();
        if(args == null) {
            getParentFragmentManager().popBackStack();
        }
        this.Subject_Id = args.getString("Subject_Id");
        this.subjectName = args.getString("subjectName");


        btn15 = rootView.findViewById(R.id.Btn15);
        btn30 = rootView.findViewById(R.id.Btn30);
        btn60 = rootView.findViewById(R.id.Btn60);
        Button submit = rootView.findViewById(R.id.submit);

        btn15.setOnClickListener(v -> {
            selectedTimer = 15;
            changeColor(btn15);
        });

        btn30.setOnClickListener(v -> {
            selectedTimer = 30;
            changeColor(btn30);
        });

        btn60.setOnClickListener(v -> {
            selectedTimer = 60;
            changeColor(btn60);
        });

        submit.setOnClickListener(v -> {
            if(selectedTimer != -1) {
                DataQuery dataQuery = new DataQuery();
                dataQuery.getSubjectData(this.Subject_Id,selectedTimer, new DataQuery.LoadQuestionCallback() {
                    @Override
                    public void onQuestionLoaded(ArrayList<QuestionModel> questionModels) {
                        if (questionModels.size() < selectedTimer) {
                            Toast.makeText(requireActivity(), "Oops! We currently have only " + questionModels.size() + " questions available for selection.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent startTest = new Intent(requireActivity(), Question_Activity.class);
                        startTest.putParcelableArrayListExtra("Subject_Data",questionModels);
                        startTest.putExtra("Timer",selectedTimer);
                        startTest.putExtra("Subject_Name",subjectName);
                        startActivity(startTest);
                        requireActivity().getSupportFragmentManager().beginTransaction().remove(SetTimer.this).commit();
                    }

                    @Override
                    public void onQuestionLoadedFailed(String error) {
                        Toast.makeText(requireActivity().getApplicationContext(), "Error: Data not found", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(requireActivity(), "Please select a timer", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    private void changeColor(Button btn) {
        int color = getResources().getColor(R.color.select_btn_hover,null);
        int set = getResources().getColor(R.color.Btn_default,null);
        btn15.setBackgroundColor(set);
        btn30.setBackgroundColor(set);
        btn60.setBackgroundColor(set);
        btn.setBackgroundColor(color);
    }

}