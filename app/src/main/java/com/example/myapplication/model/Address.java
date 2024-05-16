package com.example.myapplication.model;

public class Address {
    private String username;
    private String name;
    private String phone;
    private String address;
    private boolean isSelected;
    public Address(String username, String name, String phone, String address, boolean isSelected) {
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isSelected = isSelected;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
