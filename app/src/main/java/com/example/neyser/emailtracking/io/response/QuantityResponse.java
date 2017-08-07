package com.example.neyser.emailtracking.io.response;

public class QuantityResponse {

    private boolean success;
    private int quantity;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
