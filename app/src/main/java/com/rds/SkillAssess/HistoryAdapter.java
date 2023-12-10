package com.rds.SkillAssess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private final ArrayList<QuestionModel> questionModels = new ArrayList<>();
    private final List<Map<String, Object>> dataList;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public HistoryAdapter(List<Map<String, Object>> dataList,Context context){
        this.dataList = dataList;
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
        Map<String,Object> singleQuiz = dataList.get(position);
        int questionSize = (int) singleQuiz.get("Question_Size");

        String subjectName = singleQuiz.get("Subject_Name")+" Programming";
        String submissionDate = ""+singleQuiz.get("Submission_Date");
        String total = singleQuiz.get("Total_Correct")+"/"+questionSize;

        holder.quizMarks.setText(total);
        holder.quizTitle.setText(subjectName);
        holder.quizDate.setText(submissionDate);

        holder.historyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter method for the item click listener
    public void setOnItemClickListener(HistoryAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
