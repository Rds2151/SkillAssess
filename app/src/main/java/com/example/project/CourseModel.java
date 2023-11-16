package com.example.project;

import java.io.Serializable;

public class CourseModel implements Serializable {

    private String course_Name;
    private int img_URL;

    public CourseModel(String course_Name,int img_URL)
    {
        this.course_Name = course_Name;
        this.img_URL = img_URL;
    }

    public String getCourse_Name(){
        return course_Name;
    }

    public int getImg_URL(){
        return img_URL;
    }

    public void setCourse_Name(String course_Name) {
        this.course_Name = course_Name;
    }

    public void setImg_URL(int img_URL) {
        this.img_URL = img_URL;
    }
}
