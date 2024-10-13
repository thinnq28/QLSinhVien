package com.example.quanlysinhvien.model;

public class Classes {
    private Integer id;
    private String name;
    private Integer active;

    public Classes(Integer active, Integer id, String name) {
        this.active = active;
        this.id = id;
        this.name = name;
    }

    public Classes() {
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
