package com.magnum.coffe.cart.service;


import com.magnum.coffe.cart.model.Cart;
import com.magnum.coffe.cart.model.request.AddCartItemRequest;
import com.magnum.coffe.cart.model.request.SyncCartRequest;

public interface CartService {

    Cart getByCartKey(String cartKey);

    Cart addItem(String cartKey, AddCartItemRequest request);

    Cart updateItemQuantity(String cartKey, String productId, int quantity);

    Cart removeItem(String cartKey, String productId);

    Cart sync(String cartKey, SyncCartRequest request);

    void clear(String cartKey);
}