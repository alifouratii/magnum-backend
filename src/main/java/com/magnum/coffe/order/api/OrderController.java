package com.magnum.coffe.order.api;


import com.magnum.coffe.order.model.Order;
import com.magnum.coffe.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
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
}