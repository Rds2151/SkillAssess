package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String Course = null;
    private RecyclerView courseRV;
    private FrameLayout dataNotFound;
    private ShimmerFrameLayout shimmerFrameLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Bundle args = getArguments();
        if(args != null) {
            Course = args.getString("Course_Name");
        }
        courseRV = rootView.findViewById(R.id.courseRV);
        dataNotFound = rootView.findViewById(R.id.datanotfound);
        shimmerFrameLayout = rootView.findViewById(R.id.shimmerId);
        shimmerFrameLayout.startShimmer();

        DataQuery dataQuery = new DataQuery();

        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(new GridLayoutManager(requireActivity(),2));

        if (this.Course != null) {
            dataQuery.loadSubjects(this.Course, courseModels -> {
                updateRecyclerView(courseModels);
            });
        } else {
            dataQuery.loadCategories(courseModels -> {
                updateRecyclerView(courseModels);
            });
        }

        return rootView;
    }

    private void updateRecyclerView(ArrayList<CourseModel> courseModels) {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        if (courseModels.size() == 0) {
            dataNotFound.setVisibility(View.VISIBLE);
            return;
        }
        if(isAdded()) {
            courseRV.setVisibility(View.VISIBLE);
            RecycleAdapter recycleAdapter = new RecycleAdapter(courseModels, requireActivity());
            if (this.Course == null) {
                recycleAdapter.setOnItemClickListener(position -> {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    HomeFragment homeFragment = new HomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Course_Name",courseModels.get(position).getCourse_id());
                    homeFragment.setArguments(bundle);

                    transaction.replace(R.id.fragment,homeFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                });
            } else {
                recycleAdapter.setOnItemClickListener(position -> {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    SetTimer setTimer = new SetTimer();
                    Bundle bundle = new Bundle();
                    bundle.putString("Subject_Id",courseModels.get(position).getCourse_id());
                    setTimer.setArguments(bundle);

                    fragmentTransaction.replace(R.id.fragment,setTimer);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });
            }
            courseRV.setAdapter(recycleAdapter);
        }
    }
}