package com.example.btl.models;

import java.sql.Timestamp;

public class User {
    private int user_id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
    private Timestamp create_at;
    private Timestamp update_at;

    public User() {
    }

    public User(Timestamp create_at, String email, String password, String phone, String role, Timestamp update_at, int user_id, String username) {
        this.create_at = create_at;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.update_at = update_at;
        this.user_id = user_id;
        this.username = username;
    }

    public Timestamp getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Timestamp update_at) {
        this.update_at = update_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
