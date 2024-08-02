package com.harshsingh.chatapplication;

public class User {
    String name = null;
    String email = null;
    String uid = null;

    public User(){}

    public User(String name, String email, String uid){
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }
}
