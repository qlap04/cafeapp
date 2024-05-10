package com.example.myapplication.model;

public class Category {
    private String title;
    private int imageResource;

    public Category(String title, int imageResource) {
        this.title = title;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResource() {
        return imageResource;
    }
}
