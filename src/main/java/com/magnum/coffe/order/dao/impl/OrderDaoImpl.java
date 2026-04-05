package com.magnum.coffe.order.dao.impl;


import com.magnum.coffe.order.dao.OrderDao;
import com.magnum.coffe.order.model.Order;
import com.magnum.coffe.order.repositories.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderDaoImpl implements OrderDao {

    private final OrderRepository repo;

    public OrderDaoImpl(OrderRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Order> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Order> findById(String id) {
        return repo.findById(id);
    }

    @Override
    public Order save(Order order) {
        return repo.save(order);
    }
}