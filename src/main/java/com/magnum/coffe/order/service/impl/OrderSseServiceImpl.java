package com.magnum.coffe.order.service.impl;

import com.magnum.coffe.order.model.Order;
import com.magnum.coffe.order.model.OrderEvent;
import com.magnum.coffe.order.service.OrderSseService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OrderSseServiceImpl implements OrderSseService {

    private static final Pattern TABLE_NUMBER_PATTERN = Pattern.compile("(\\d+)");

    private final List<ClientConnection> clients = new CopyOnWriteArrayList<>();

    @Override
    public SseEmitter connect(String scope) {
        String normalizedScope = normalizeScope(scope);
        SseEmitter emitter = new SseEmitter(0L);
        ClientConnection client = new ClientConnection(emitter, normalizedScope);

        clients.add(client);

        emitter.onCompletion(() -> clients.remove(client));
        emitter.onTimeout(() -> {
            clients.remove(client);
            try {
                emitter.complete();
            } catch (Exception ignored) {
            }
        });
        emitter.onError((ex) -> {
            clients.remove(client);
            try {
                emitter.complete();
            } catch (Exception ignored) {
            }
        });

        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("connected"));
        } catch (IOException e) {
            clients.remove(client);
            try {
                emitter.complete();
            } catch (Exception ignored) {
            }
        }

        return emitter;
    }

    @Override
    public void broadcast(OrderEvent event) {
        List<ClientConnection> deadClients = new ArrayList<>();

        for (ClientConnection client : clients) {
            if (!matchesScope(event.getOrder(), client.scope)) {
                continue;
            }

            try {
                client.emitter.send(SseEmitter.event()
                        .name(event.getType())
                        .data(event));
            } catch (Exception e) {
                deadClients.add(client);
            }
        }

        for (ClientConnection deadClient : deadClients) {
            clients.remove(deadClient);
            try {
                deadClient.emitter.complete();
            } catch (Exception ignored) {
            }
        }
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

    private static class ClientConnection {
        private final SseEmitter emitter;
        private final String scope;

        private ClientConnection(SseEmitter emitter, String scope) {
            this.emitter = emitter;
            this.scope = scope;
        }
    }
}