package com.magnum.coffe.order.service;


import com.magnum.coffe.order.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    Order create(Order payload);

    Order update(String id, Order payload);
}