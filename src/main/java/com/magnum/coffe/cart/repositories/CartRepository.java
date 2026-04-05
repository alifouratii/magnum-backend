package com.magnum.coffe.cart.repositories;


import com.magnum.coffe.cart.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {

    Optional<Cart> findByCartKey(String cartKey);

    boolean existsByCartKey(String cartKey);

    void deleteByCartKey(String cartKey);
}