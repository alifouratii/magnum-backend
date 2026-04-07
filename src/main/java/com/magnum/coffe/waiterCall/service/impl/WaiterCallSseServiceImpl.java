package com.magnum.coffe.waiterCall.service.impl;

import com.magnum.coffe.waiterCall.model.WaiterCallEvent;
import com.magnum.coffe.waiterCall.service.WaiterCallSseService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class WaiterCallSseServiceImpl implements WaiterCallSseService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Override
    public SseEmitter connect() {
        SseEmitter emitter = new SseEmitter(0L);

        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            emitter.complete();
        });
        emitter.onError((ex) -> {
            emitters.remove(emitter);
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
            emitters.remove(emitter);
            try {
                emitter.complete();
            } catch (Exception ignored) {
            }
        }

        return emitter;
    }

    @Override
    public void broadcast(WaiterCallEvent event) {
        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name(event.getType())
                        .data(event));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        }

        for (SseEmitter deadEmitter : deadEmitters) {
            emitters.remove(deadEmitter);
            try {
                deadEmitter.complete();
            } catch (Exception ignored) {
            }
        }
    }
}