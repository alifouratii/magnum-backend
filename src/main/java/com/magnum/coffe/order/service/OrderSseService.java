package com.magnum.coffe.order.service;

import com.magnum.coffe.order.model.OrderEvent;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface OrderSseService {
    SseEmitter connect();
    void broadcast(OrderEvent event);
}