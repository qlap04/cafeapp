package com.example.myapplication.model;

public class Discount {
    private int valueDiscount;
    private String conditionDiscount;
    private double conditionTotal;

    public Discount(int valueDiscount, String conditionDiscount, double conditionTotal) {
        this.valueDiscount = valueDiscount;
        this.conditionDiscount = conditionDiscount;
        this.conditionTotal = conditionTotal;
    }
    public int getValueDiscount() {
        return valueDiscount;
    }

    public void setValueDiscount(int valueDiscount) {
        this.valueDiscount = valueDiscount;
    }

    public String getConditionDiscount() {
        return conditionDiscount;
    }

    public void setConditionDiscount(String conditionDiscount) {
        this.conditionDiscount = conditionDiscount;
    }
    public double getConditionTotal() {
        return conditionTotal;
    }

    public void setConditionTotal(double conditionTotal) {
        this.conditionTotal = conditionTotal;
    }
}
