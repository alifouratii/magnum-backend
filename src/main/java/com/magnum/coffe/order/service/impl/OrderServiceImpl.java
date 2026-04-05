package com.magnum.coffe.order.service.impl;


import com.magnum.coffe.order.dao.OrderDao;
import com.magnum.coffe.order.model.Order;
import com.magnum.coffe.order.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao dao;

    public OrderServiceImpl(OrderDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Order> getAll() {
        return dao.findAll();
    }

    @Override
    public Order create(Order payload) {

        LocalDateTime now = LocalDateTime.now();

        payload.setId(null);

        payload.setOrder_number("ORD-" + System.currentTimeMillis());

        double subtotal = payload.getItems().stream()
                .mapToDouble(i -> i.getUnit_price() * i.getQuantity())
                .sum();

        payload.setSubtotal(subtotal);
        payload.setTotal(subtotal);

        payload.setCreated_at(now);
        payload.setUpdated_at(now);

        if (payload.getStatus() == null) {
            payload.setStatus("pending");
        }

        return dao.save(payload);
    }

    @Override
    public Order update(String id, Order payload) {

        Order existing = dao.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        existing.setCustomer_name(payload.getCustomer_name());
        existing.setCustomer_phone(payload.getCustomer_phone());
        existing.setTable_number(payload.getTable_number());
        existing.setNote(payload.getNote());
        existing.setStatus(payload.getStatus());
        existing.setItems(payload.getItems());

        double subtotal = payload.getItems().stream()
                .mapToDouble(i -> i.getUnit_price() * i.getQuantity())
                .sum();

        existing.setSubtotal(subtotal);
        existing.setTotal(subtotal);

        existing.setUpdated_at(LocalDateTime.now());

        return dao.save(existing);
    }
}