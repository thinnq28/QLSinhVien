package com.example.quanlysinhvien.model;

public class Course {
    private int id;
    private String courseName;
    private String courseDescription;
    private int credits;

    public Course(String courseDescription, int id, String courseName, int credits) {
        this.courseDescription = courseDescription;
        this.id = id;
        this.courseName = courseName;
        this.credits = credits;
    }

    public Course() {
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getCourseId() {
        return id;
    }

    public void setCourseId(int courseId) {
        this.id = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}