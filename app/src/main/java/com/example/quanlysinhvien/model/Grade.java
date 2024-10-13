package com.example.quanlysinhvien.model;

public class Grade {
    private Integer id;
    private Integer enrollmentId;
    private String grade;

    public Grade(Integer enrollmentId, String grade, Integer id) {
        this.enrollmentId = enrollmentId;
        this.grade = grade;
        this.id = id;
    }

    public Grade() {
    }

    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Integer enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
