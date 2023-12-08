package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {

    private final ArrayList<QuestionModel> dataList;
    public ResultAdapter(ArrayList<QuestionModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ResultAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_result_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.MyViewHolder holder, int position) {
        QuestionModel questionModel = dataList.get(position);

        holder.questionNo.setText("Question "+(position+1)+":");
        holder.questionText.setText(questionModel.getQuestion());
        holder.correctAns.setText(questionModel.getCorrectAnswer());
        holder.selectedAns.setText(questionModel.getSelectedAnswer());
        int color = ContextCompat.getColor(holder.itemView.getContext(), R.color.correctColor);
        if(questionModel.isAnswerCorrect()) {
            holder.selectedAns.setTextColor(color);
        } else {
            int wrongColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.selectColor);
            holder.selectedAns.setTextColor(wrongColor);
        }
        holder.correctAns.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView questionText,selectedAns,correctAns,questionNo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            selectedAns = itemView.findViewById(R.id.givenAnswer);
            correctAns = itemView.findViewById(R.id.correctAnswer);
            questionNo = itemView.findViewById(R.id.questionNo);
        }
    }
}
