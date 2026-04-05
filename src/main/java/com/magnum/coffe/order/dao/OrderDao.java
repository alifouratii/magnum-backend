package com.magnum.coffe.order.dao;


import com.magnum.coffe.order.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    List<Order> findAll();

    Optional<Order> findById(String id);

    Order save(Order order);
}