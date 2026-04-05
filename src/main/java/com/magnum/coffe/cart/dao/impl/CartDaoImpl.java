package com.magnum.coffe.cart.dao.impl;



import com.magnum.coffe.cart.dao.CartDao;
import com.magnum.coffe.cart.model.Cart;
import com.magnum.coffe.cart.repositories.CartRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CartDaoImpl implements CartDao {

    private final CartRepository cartRepository;

    public CartDaoImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Optional<Cart> findByCartKey(String cartKey) {
        return cartRepository.findByCartKey(cartKey);
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public boolean existsByCartKey(String cartKey) {
        return cartRepository.existsByCartKey(cartKey);
    }

    @Override
    public void deleteByCartKey(String cartKey) {
        cartRepository.deleteByCartKey(cartKey);
    }
}