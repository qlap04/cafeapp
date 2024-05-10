package com.example.myapplication.modelResponse;

import com.example.myapplication.model.Cart;

import java.util.List;

public class CartResponse {
    private List<Cart> listProductInCart;
    private double totalPrice;
    public CartResponse(List<Cart> listProductInCart, double totalPrice) {
        this.listProductInCart = listProductInCart;
        this.totalPrice = totalPrice;
    }
    public List<Cart> getListProductInCart() {
        return listProductInCart;
    }

    public void setListProductInCart(List<Cart> listProductInCart) {
        this.listProductInCart = listProductInCart;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
