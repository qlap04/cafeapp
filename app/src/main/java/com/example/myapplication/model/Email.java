package com.example.myapplication.model;

public class Email {
    //
    private String resetPassword;
    public Email(String resetPassword) {
        this.resetPassword = resetPassword;
    }
    public String getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(String resetPassword) {
        this.resetPassword = resetPassword;
    }

}
