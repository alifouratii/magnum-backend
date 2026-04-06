package com.magnum.coffe.order.service.impl;

import com.magnum.coffe.order.dao.OrderDao;
import com.magnum.coffe.order.model.Order;
import com.magnum.coffe.order.model.OrderEvent;
import com.magnum.coffe.order.service.OrderService;
import com.magnum.coffe.order.service.OrderSseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao dao;
    private final OrderSseService orderSseService;

    public OrderServiceImpl(OrderDao dao, OrderSseService orderSseService) {
        this.dao = dao;
        this.orderSseService = orderSseService;
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

        double subtotal = payload.getItems() == null ? 0 : payload.getItems().stream()
                .mapToDouble(i -> i.getUnit_price() * i.getQuantity())
                .sum();

        payload.setSubtotal(subtotal);
        payload.setTotal(subtotal);

        payload.setCreated_at(now);
        payload.setUpdated_at(now);

        if (payload.getStatus() == null || payload.getStatus().trim().isEmpty()) {
            payload.setStatus("pending");
        }

        Order saved = dao.save(payload);

        orderSseService.broadcast(
                OrderEvent.builder()
                        .type("ORDER_CREATED")
                        .orderId(saved.getId())
                        .message("Nouvelle commande reçue")
                        .order(saved)
                        .timestamp(System.currentTimeMillis())
                        .build()
        );

        return saved;
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

        double subtotal = payload.getItems() == null ? 0 : payload.getItems().stream()
                .mapToDouble(i -> i.getUnit_price() * i.getQuantity())
                .sum();

        existing.setSubtotal(subtotal);
        existing.setTotal(subtotal);
        existing.setUpdated_at(LocalDateTime.now());

        Order saved = dao.save(existing);

        orderSseService.broadcast(
                OrderEvent.builder()
                        .type("ORDER_UPDATED")
                        .orderId(saved.getId())
                        .message("Commande mise à jour")
                        .order(saved)
                        .timestamp(System.currentTimeMillis())
                        .build()
        );

        return saved;
    }
}