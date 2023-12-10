package com.rds.SkillAssess;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FrameLayout dataNotFound;
    private RecyclerView historyRV;

    public RecentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentFragment newInstance(String param1, String param2) {
        RecentFragment fragment = new RecentFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);

        historyRV = rootView.findViewById(R.id.historyRV);
        dataNotFound = rootView.findViewById(R.id.datanotfound);
        historyRV.setHasFixedSize(true);

        if (isAdded() && getContext() != null) {
            historyRV.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));

            new DataQuery().fetchData(new DataQuery.LoadQuizCallback() {
                @Override
                public void onQuizLoaded(List<Map<String, Object>> result) {
                    updateRecyclerView(result);
                }

                @Override
                public void onQuizLoadedFailed(String error) {
                    updateRecyclerView(null);
                }
            });
        }

        return rootView;
    }

    private void updateRecyclerView(List<Map<String, Object>> resultModels) {
        if (resultModels != null && isAdded()) {
            HistoryAdapter historyAdapter = new HistoryAdapter(resultModels, requireContext());
            historyRV.setAdapter(historyAdapter);
            historyAdapter.setOnItemClickListener(position -> {
                ArrayList<QuestionModel> dataList;
                dataList = (ArrayList<QuestionModel>) resultModels.get(position).get("Questions");

                Intent viewResultIntent = new Intent(requireActivity(), ViewResult.class);
                viewResultIntent.putParcelableArrayListExtra("data", dataList);
                viewResultIntent.putExtra("Fragment", "Recent");
                startActivity(viewResultIntent);
            });
        } else {
            dataNotFound.setVisibility(View.VISIBLE);
            historyRV.setVisibility(View.GONE);
        }
    }
}