package com.example.quanlysinhvien.model;

public class User {
    private int id;
    private String username;
    private String password;
    private boolean active;

    public User(int id, String password, String username) {
        this.id = id;
        this.password = password;
        this.username = username;
    }

    public User(boolean active, int id, String password, String username) {
        this.active = active;
        this.id = id;
        this.password = password;
        this.username = username;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
