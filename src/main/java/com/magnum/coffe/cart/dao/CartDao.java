package com.magnum.coffe.cart.dao;



import com.magnum.coffe.cart.model.Cart;

import java.util.Optional;

public interface CartDao {

    Optional<Cart> findByCartKey(String cartKey);

    Cart save(Cart cart);

    boolean existsByCartKey(String cartKey);

    void deleteByCartKey(String cartKey);
}
