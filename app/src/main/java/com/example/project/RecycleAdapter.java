package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    private final ArrayList<CourseModel> courseModels;
    private Context context;
    private OnItemClickListener onItemClickListener;

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
        Picasso.get()
                .load(currentCourse.getImg_URL())
                .error(R.drawable.usericon)
                .into(holder.courseImage);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImage;
        CardView cardView;
        TextView courseName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImg);
            courseName = itemView.findViewById(R.id.courseTxt);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter method for the item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
