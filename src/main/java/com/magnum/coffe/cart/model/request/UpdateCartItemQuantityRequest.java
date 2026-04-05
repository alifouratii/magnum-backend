package com.magnum.coffe.cart.model.request;


public class UpdateCartItemQuantityRequest {

    private int quantity;

    public UpdateCartItemQuantityRequest() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}