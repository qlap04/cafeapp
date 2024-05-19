package com.example.myapplication.model;

public class Order {
    //
    private String orderId;
    private String clientName;
    private String name;
    private String phone;
    private String address;
    private double total;
    private int valueDiscount;
    private String paymentMethod;
    public Order(String orderId, String clientName, String name, String phone, String address, double total, int valueDiscount, String paymentMethod) {
        this.orderId = orderId;
        this.clientName = clientName;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.valueDiscount = valueDiscount;
        this.paymentMethod = paymentMethod;
    }
    public String getOrderId() {
        return orderId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public double getTotal() {
        return total;
    }

    public int getValueDiscount() {
        return valueDiscount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
