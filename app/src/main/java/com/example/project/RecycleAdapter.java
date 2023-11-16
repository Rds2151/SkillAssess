package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    private ArrayList<CourseModel> courseModels;
    private Context context;

    public RecycleAdapter(ArrayList<CourseModel> courseModels, Context context) {
        this.courseModels = courseModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_course_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.MyViewHolder holder, int position) {
        CourseModel currentCourse = courseModels.get(position);
        holder.courseName.setText(currentCourse.getCourse_Name());
        holder.courseImage.setImageResource(currentCourse.getImg_URL());
    }

    @Override
    public int getItemCount() {
        return courseModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImage;
        TextView courseName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImg);
            courseName = itemView.findViewById(R.id.courseTxt);
        }
    }
}
