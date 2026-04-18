package com.magnum.coffe.order.service.impl;

import com.magnum.coffe.category.dao.MenuCategoryDao;
import com.magnum.coffe.notification.model.Notification;
import com.magnum.coffe.notification.service.NotificationService;
import com.magnum.coffe.order.dao.OrderDao;
import com.magnum.coffe.order.model.Order;
import com.magnum.coffe.order.model.OrderEvent;
import com.magnum.coffe.order.model.OrderItem;
import com.magnum.coffe.order.service.OrderService;
import com.magnum.coffe.order.service.OrderSseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private static final Pattern TABLE_NUMBER_PATTERN = Pattern.compile("(\\d+)");

    private final OrderDao dao;
    private final OrderSseService orderSseService;
    private final NotificationService notificationService;
    private final MenuCategoryDao categoryDao;

    public OrderServiceImpl(
            OrderDao dao,
            OrderSseService orderSseService,
            NotificationService notificationService,
            MenuCategoryDao categoryDao
    ) {
        this.dao = dao;
        this.orderSseService = orderSseService;
        this.notificationService = notificationService;
        this.categoryDao = categoryDao;
    }
    private void enrichItemsWithCategoryAndSubgroupNames(Order order) {
        if (order == null || order.getItems() == null) {
            return;
        }

        for (OrderItem item : order.getItems()) {
            if (item == null) {
                continue;
            }

            String categoryId = item.getCategory_id();
            String subgroupId = item.getSubgroup_id(); // ou getSubgroupId()

            if (categoryId == null || categoryId.isBlank()) {
                continue;
            }

            categoryDao.findById(categoryId).ifPresent(category -> {
                item.setCategory_name(category.getName()); // ou getTitle()

                if (subgroupId != null && !subgroupId.isBlank() && category.getSubgroups() != null) {
                    category.getSubgroups().stream()
                            .filter(subgroup -> subgroup != null && subgroupId.equals(subgroup.getId()))
                            .findFirst()
                            .ifPresent(subgroup -> {
                                item.setSubgroup_name(subgroup.getTitle()); // ou getTitle()
                            });
                }
            });
        }
    }
    @Override
    public List<Order> getAll(String scope) {
        String normalizedScope = normalizeScope(scope);

        return dao.findAll().stream()
                .filter(order -> matchesScope(order, normalizedScope))
                .collect(Collectors.toList());
    }
    private void enrichItemsWithCategoryNames(Order order) {
        if (order.getItems() == null) {
            return;
        }

        for (OrderItem item : order.getItems()) {
            if (item == null) {
                continue;
            }

            String categoryId = item.getCategory_id();
            if (categoryId == null || categoryId.isBlank()) {
                continue;
            }

            categoryDao.findById(categoryId).ifPresent(category -> {
                item.setCategory_name(category.getName());
            });
        }
    }

    @Override
    public Order create(Order payload) {
        Instant now = Instant.now();

        payload.setId(null);
        payload.setOrder_number("ORD-" + System.currentTimeMillis());

        enrichItemsWithCategoryAndSubgroupNames(payload);

        if (payload.getItems() != null) {
            payload.getItems().forEach(item -> {
                log.info(
                        "Order item: productId={}, productName={}, categoryId={}, categoryName={}",
                        item.getProduct_id(),
                        item.getProduct_name(),
                        item.getCategory_id(),
                        item.getCategory_name()
                );
            });
        }

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
                        .scope(resolveScope(saved.getTable_number()))
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
        existing.setUpdated_at(Instant.now());

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
                        .scope(resolveScope(saved.getTable_number()))
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

    private String resolveScope(String tableValue) {
        Integer tableNumber = extractTableNumber(tableValue);

        if (tableNumber == null) {
            return "ALL";
        }

        if (tableNumber >= 1 && tableNumber <= 15) {
            return "SESSION1";
        }

        if (tableNumber >= 16 && tableNumber <= 30) {
            return "SESSION2";
        }

        return "ALL";
    }

    private boolean matchesScope(Order order, String scope) {
        if ("ALL".equals(scope)) {
            return true;
        }

        Integer tableNumber = extractTableNumber(order == null ? null : order.getTable_number());
        if (tableNumber == null) {
            return false;
        }

        if ("SESSION1".equals(scope)) {
            return tableNumber >= 1 && tableNumber <= 15;
        }

        if ("SESSION2".equals(scope)) {
            return tableNumber >= 16 && tableNumber <= 30;
        }

        return true;
    }

    private Integer extractTableNumber(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        Matcher matcher = TABLE_NUMBER_PATTERN.matcher(value);
        if (!matcher.find()) {
            return null;
        }

        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String normalizeScope(String scope) {
        if (scope == null) {
            return "ALL";
        }

        String normalized = scope.trim().toUpperCase();
        if ("SESSION1".equals(normalized) || "SESSION2".equals(normalized) || "ALL".equals(normalized)) {
            return normalized;
        }

        return "ALL";
    }
}