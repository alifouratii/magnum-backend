package com.magnum.coffe.waiterCall.service;

import com.magnum.coffe.waiterCall.model.WaiterCallEvent;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface WaiterCallSseService {
    SseEmitter connect();
    void broadcast(WaiterCallEvent event);
}