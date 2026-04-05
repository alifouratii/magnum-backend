package com.magnum.coffe.cart.model.request;



import com.magnum.coffe.cart.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class SyncCartRequest {

    private String userId;
    private List<CartItem> items = new ArrayList<>();

    public SyncCartRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setItems(List<CartItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }
}