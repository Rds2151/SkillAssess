package com.rds.SkillAssess;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionModel implements Parcelable {
    String question;
    String CorrectAnswer;
    String SelectedAnswer;
    List<String> Options;

    public QuestionModel(String question, String correctAnswer, List<String> options) {
        this.question = question.trim();
        this.CorrectAnswer = correctAnswer.trim();
        this.Options = options;
    }

    public QuestionModel() {}

    protected QuestionModel(Parcel in) {
        question = in.readString();
        CorrectAnswer = in.readString();
        SelectedAnswer = in.readString();
        Options = in.createStringArrayList();
    }

    public static final Creator<QuestionModel> CREATOR = new Creator<QuestionModel>() {
        @Override
        public QuestionModel createFromParcel(Parcel in) {
            return new QuestionModel(in);
        }

        @Override
        public QuestionModel[] newArray(int size) {
            return new QuestionModel[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getSelectedAnswer() {
        return   SelectedAnswer;
    }

    public boolean isAnswerCorrect() {
        return this.CorrectAnswer.equals(this.SelectedAnswer);
    }

    public void setSelectedAnswer(String SelectedAnswer) {
        this.SelectedAnswer = SelectedAnswer;
    }

    public List<String> getOptions() {
        return Options;
    }

    public Map<String,Object> getData() {
        Map<String,Object> data = new HashMap<>();
        data.put("Text",getQuestion());
        data.put("CorrectAnswer",getCorrectAnswer());
        data.put("SelectedAnswer",getSelectedAnswer());
        data.put("Options",getOptions());

        return data;
    }

    public void setOptions(List<String> options) {
        this.Options = options;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(CorrectAnswer);
        dest.writeString(SelectedAnswer);
        dest.writeStringList(Options);
    }
}
