package com.app.trekmate.model;

import com.google.firebase.Timestamp;

public class User {

    private String uid;
    private String email;
    private String name;
    private String phone;
    public String photoUrl;
    private Timestamp createdAt;

    // 🔹 Required empty constructor for Firestore
    public User() {}

    public User(String uid, String email, String name, String phone, Timestamp createdAt) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.photoUrl = "";
        this.createdAt = createdAt;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
    public String getPhotoUrl() { return photoUrl; }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
