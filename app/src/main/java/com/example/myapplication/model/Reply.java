package com.example.myapplication.model;

public class Reply {
    private String name;
    private String phoneNumber;
    private String email;
    private String content;
    public Reply(String name, String phoneNumber, String email, String content) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.content = content;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
