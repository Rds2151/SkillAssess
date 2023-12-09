package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private final ArrayList<QuestionModel> questionModels;
    private final Context context;
    private int timeValue;

    public HistoryAdapter(ArrayList<QuestionModel> questionModels,Context context){
        this.questionModels = questionModels;
        this.timeValue = 9;
        this.context = context;
    }
    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        holder.quizMarks.setText("");
        holder.quizTitle.setText("");
        holder.quizDate.setText("");

        holder.historyCard.setOnClickListener(v -> {
            Intent viewResultIntent = new Intent(context,ViewResult.class);
            viewResultIntent.putParcelableArrayListExtra("data",questionModels);
            viewResultIntent.putExtra("timeValue",timeValue);
            context.startActivity(viewResultIntent);
        });
    }

    @Override
    public int getItemCount() {
        return questionModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView historyCard;
        TextView quizTitle,quizDate,quizMarks;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            quizDate = itemView.findViewById(R.id.dateTextView);
            quizTitle = itemView.findViewById(R.id.quizNameTextView);
            quizMarks = itemView.findViewById(R.id.totalMarksTextView);
            historyCard = itemView.findViewById(R.id.historyCard);
        }
    }
}
