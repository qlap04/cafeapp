package com.example.myapplication.model;
import java.io.Serializable;

public class Product implements Serializable {

    private String image;
    private String title;
    private Double price;
    private Double star;
    private String time;
    private String category;
    private String popular;
    public Product(String image, String title, Double price, Double star, String time, String category, String popular) {
        this.image = image;
        this.title = title;
        this.price = price;
        this.star = star;
        this.time = time;
        this.category = category;
        this.popular = popular;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getPopular() {
        return popular;
    }

    public void setPopular(String popular) {
        this.popular = popular;
    }
}
