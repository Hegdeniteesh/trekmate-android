package com.app.trekmate.model;

public class User {

    public String uid;
    public String email;
    public String name;
    public String phone;
    public long createdAt;

    public User() {
        // Firestore needs empty constructor
    }

    public User(String uid, String email, String name, String phone) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.createdAt = System.currentTimeMillis();
    }
}
