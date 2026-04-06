package com.magnum.coffe.order.api;

import com.magnum.coffe.order.model.Order;
import com.magnum.coffe.order.service.OrderService;
import com.magnum.coffe.order.service.OrderSseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService service;
    private final OrderSseService orderSseService;

    public OrderController(OrderService service, OrderSseService orderSseService) {
        this.service = service;
        this.orderSseService = orderSseService;
    }

    @GetMapping
    public List<Order> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Order create(@RequestBody Order payload) {
        return service.create(payload);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable String id, @RequestBody Order payload) {
        return service.update(id, payload);
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        return orderSseService.connect();
    }
}