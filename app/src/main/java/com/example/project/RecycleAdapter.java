package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    private final ArrayList<CourseModel> courseModels;
    private final ArrayList<CourseModel> filteredModels;
    private OnItemClickListener onItemClickListener;

    public RecycleAdapter(ArrayList<CourseModel> courseModels) {
        this.courseModels = new ArrayList<>(courseModels);
        this.filteredModels = new ArrayList<>(courseModels);
    }

    @NonNull
    @Override
    public RecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_course_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.MyViewHolder holder, int position) {
        CourseModel currentCourse = filteredModels.get(position);
        holder.shimmerFrameLayout.stopShimmer();

        holder.courseName.setText(currentCourse.getCourse_Name());
        Picasso.get()
                .load(currentCourse.getImg_URL())
                .error(R.drawable.usericon)
                .into(holder.courseImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.shimmerFrameLayout.stopShimmer(); // stop the shimmer effect when the image is successfully loaded
                        holder.shimmerFrameLayout.setVisibility(View.GONE); // hide the shimmer container
                        holder.courseImage.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onError(Exception e) {}
                });
        holder.cardView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                int position1 = holder.getAdapterPosition();
                String courseId = filteredModels.get(position1).getCourse_id();
                String courseName = filteredModels.get(position1).getCourse_Name();
                onItemClickListener.onItemClick(courseId,courseName);
            }
        });
    }

    public void filter(String query) {
        filteredModels.clear();

        if(query.isEmpty()) {
            filteredModels.addAll(courseModels);
        } else {
            for (CourseModel course : courseModels) {
                if (course.getCourse_Name().toLowerCase().contains(query.toLowerCase())) {
                    filteredModels.add(course);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImage;
        CardView cardView;
        ShimmerFrameLayout shimmerFrameLayout;
        TextView courseName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImg);
            courseName = itemView.findViewById(R.id.courseTxt);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_Img);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String courseId,String courseName);
    }

    // Setter method for the item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
