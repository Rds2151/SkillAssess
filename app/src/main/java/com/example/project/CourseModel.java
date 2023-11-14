package com.example.project;

public class CourseModel {

    private String course_Name;
    private String img_URL;

    public CourseModel(String course_Name,String img_URL)
    {
        this.course_Name = course_Name;
        this.img_URL = img_URL;
    }

    public String getCourse_Name(){
        return course_Name;
    }

    public String getImg_URL(){
        return img_URL;
    }

    public void setCourse_Name(String course_Name) {
        this.course_Name = course_Name;
    }

    public void setImg_URL(String img_URL) {
        this.img_URL = img_URL;
    }
}
