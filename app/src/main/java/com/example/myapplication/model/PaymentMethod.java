package com.example.myapplication.model;
public class PaymentMethod {
    private int icon;
    private String title;
    private String description;

    public PaymentMethod(int icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
