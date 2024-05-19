package com.example.myapplication.modelResponse;

public class TotalPriceResponse {
    //
    private final double totalPrice;

    public TotalPriceResponse(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

