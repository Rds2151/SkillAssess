package com.example.project;

import java.io.Serializable;

public class CourseModel implements Serializable {

    private String course_id;
    private String course_Name;
    private String img_URL;

    public CourseModel(String courseId, String course_Name, String img_URL)
    {
        this.course_id = courseId;
        this.course_Name = course_Name;
        this.img_URL = img_URL;
    }

    public String getCourse_Name(){
        return course_Name;
    }

    public String getImg_URL(){
        return img_URL;
    }

    public String getCourse_id(){
        return course_id;
    }


    public void setCourse_Name(String course_Name) {
        this.course_Name = course_Name;
    }
    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setImg_URL(String img_URL) {
        this.img_URL = img_URL;
    }
}
