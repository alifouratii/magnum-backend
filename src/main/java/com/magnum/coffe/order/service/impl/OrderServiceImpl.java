package com.magnum.coffe.order.service.impl;

import com.magnum.coffe.notification.model.Notification;
import com.magnum.coffe.notification.service.NotificationService;
import com.magnum.coffe.order.dao.OrderDao;
import com.magnum.coffe.order.model.Order;
import com.magnum.coffe.order.model.OrderEvent;
import com.magnum.coffe.order.service.OrderService;
import com.magnum.coffe.order.service.OrderSseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao dao;
    private final OrderSseService orderSseService;
    private final NotificationService notificationService;

    public OrderServiceImpl(
            OrderDao dao,
            OrderSseService orderSseService,
            NotificationService notificationService
    ) {
        this.dao = dao;
        this.orderSseService = orderSseService;
        this.notificationService = notificationService;
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

        safeBroadcast(
                OrderEvent.builder()
                        .type("ORDER_CREATED")
                        .orderId(saved.getId())
                        .message("Nouvelle commande reçue")
                        .order(saved)
                        .timestamp(System.currentTimeMillis())
                        .build()
        );

        safeCreateNotification(
                Notification.builder()
                        .type("ORDER_CREATED")
                        .title("Nouvelle commande")
                        .message(buildOrderMessage(saved))
                        .route("/admin/orders")
                        .entityId(saved.getId())
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

        safeBroadcast(
                OrderEvent.builder()
                        .type("ORDER_UPDATED")
                        .orderId(saved.getId())
                        .message("Commande mise à jour")
                        .order(saved)
                        .timestamp(System.currentTimeMillis())
                        .build()
        );

        safeCreateNotification(
                Notification.builder()
                        .type("ORDER_UPDATED")
                        .title("Commande mise à jour")
                        .message(buildOrderMessage(saved))
                        .route("/admin/orders")
                        .entityId(saved.getId())
                        .build()
        );

        return saved;
    }

    private void safeBroadcast(OrderEvent event) {
        try {
            orderSseService.broadcast(event);
        } catch (Exception e) {
            System.err.println("Order SSE broadcast failed: " + e.getMessage());
        }
    }

    private void safeCreateNotification(Notification notification) {
        try {
            notificationService.create(notification);
        } catch (Exception e) {
            System.err.println("Order notification creation failed: " + e.getMessage());
        }
    }

    private String buildOrderMessage(Order order) {
        String tableNumber = order.getTable_number() == null || order.getTable_number().isBlank()
                ? "—"
                : order.getTable_number().trim();

        String customerName = order.getCustomer_name() == null || order.getCustomer_name().isBlank()
                ? "Client"
                : order.getCustomer_name().trim();

        String note = order.getNote() == null ? "" : order.getNote().trim();

        String itemNames = order.getItems() == null ? "" : order.getItems().stream()
                .map(item -> item.getProduct_name() == null ? "" : item.getProduct_name().trim())
                .filter(name -> !name.isBlank())
                .limit(3)
                .collect(Collectors.joining(", "));

        StringBuilder sb = new StringBuilder();
        sb.append("Table ").append(tableNumber).append(" • ").append(customerName);

        if (!itemNames.isBlank()) {
            sb.append(" • ").append(itemNames);
        }

        if (!note.isBlank()) {
            sb.append(" • Note: ").append(note);
        }

        return sb.toString();
    }
}