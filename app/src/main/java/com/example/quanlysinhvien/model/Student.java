package com.example.quanlysinhvien.model;

import java.util.Date;

public class Student {

    private String id;
    private String name;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private Integer classId;
    private Integer active;

    public Student() {};

    public Student(Integer active, Integer classId, Date dateOfBirth, String email, String gender, String id, String name, String phoneNumber) {
        this.active = active;
        this.classId = classId;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.gender = gender;
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
