package com.magnum.coffe.waiterCall.service.impl;

import com.magnum.coffe.notification.model.Notification;
import com.magnum.coffe.notification.service.NotificationService;
import com.magnum.coffe.waiterCall.dao.WaiterCallDao;
import com.magnum.coffe.waiterCall.model.WaiterCall;
import com.magnum.coffe.waiterCall.model.WaiterCallEvent;
import com.magnum.coffe.waiterCall.service.WaiterCallService;
import com.magnum.coffe.waiterCall.service.WaiterCallSseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class WaiterCallServiceImpl implements WaiterCallService {

    private static final Set<String> ALLOWED_STATUS = Set.of(
            "new",
            "seen",
            "on_the_way",
            "completed"
    );

    private final WaiterCallDao waiterCallDao;
    private final WaiterCallSseService waiterCallSseService;
    private final NotificationService notificationService;

    public WaiterCallServiceImpl(
            WaiterCallDao waiterCallDao,
            WaiterCallSseService waiterCallSseService,
            NotificationService notificationService
    ) {
        this.waiterCallDao = waiterCallDao;
        this.waiterCallSseService = waiterCallSseService;
        this.notificationService = notificationService;
    }

    @Override
    public List<WaiterCall> getAll() {
        return waiterCallDao.findAll();
    }

    @Override
    public WaiterCall create(WaiterCall payload) {
        LocalDateTime now = LocalDateTime.now();

        payload.setId(null);

        if (payload.getStatus() == null || payload.getStatus().isBlank()) {
            payload.setStatus("new");
        }

        validateStatus(payload.getStatus());

        payload.setCreated_at(now);
        payload.setUpdated_at(now);

        WaiterCall saved = waiterCallDao.save(payload);

        waiterCallSseService.broadcast(
                WaiterCallEvent.builder()
                        .type("WAITER_CALL_CREATED")
                        .waiterCallId(saved.getId())
                        .message("Nouvel appel serveur")
                        .waiterCall(saved)
                        .timestamp(System.currentTimeMillis())
                        .build()
        );

        notificationService.create(
                Notification.builder()
                        .type("WAITER_CALL_CREATED")
                        .title("Nouvel appel serveur")
                        .message(buildWaiterCallMessage(saved))
                        .route("/admin/waiter-calls")
                        .entityId(saved.getId())
                        .build()
        );

        return saved;
    }

    @Override
    public WaiterCall updateStatus(String id, String status) {
        validateStatus(status);

        WaiterCall existing = waiterCallDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Waiter call not found with id: " + id));

        existing.setStatus(status);
        existing.setUpdated_at(LocalDateTime.now());

        WaiterCall saved = waiterCallDao.save(existing);

        waiterCallSseService.broadcast(
                WaiterCallEvent.builder()
                        .type("WAITER_CALL_UPDATED")
                        .waiterCallId(saved.getId())
                        .message("Appel serveur mis à jour")
                        .waiterCall(saved)
                        .timestamp(System.currentTimeMillis())
                        .build()
        );

        notificationService.create(
                Notification.builder()
                        .type("WAITER_CALL_UPDATED")
                        .title("Appel serveur mis à jour")
                        .message(buildWaiterCallMessage(saved))
                        .route("/admin/waiter-calls")
                        .entityId(saved.getId())
                        .build()
        );

        return saved;
    }

    @Override
    public WaiterCall update(String id, WaiterCall payload) {
        WaiterCall existing = waiterCallDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Waiter call not found with id: " + id));

        String status = payload.getStatus();
        if (status == null || status.isBlank()) {
            status = existing.getStatus();
        }

        validateStatus(status);

        existing.setTable_number(payload.getTable_number());
        existing.setCustomer_name(payload.getCustomer_name());
        existing.setNote(payload.getNote());
        existing.setStatus(status);
        existing.setUpdated_at(LocalDateTime.now());

        WaiterCall saved = waiterCallDao.save(existing);

        waiterCallSseService.broadcast(
                WaiterCallEvent.builder()
                        .type("WAITER_CALL_UPDATED")
                        .waiterCallId(saved.getId())
                        .message("Appel serveur mis à jour")
                        .waiterCall(saved)
                        .timestamp(System.currentTimeMillis())
                        .build()
        );

        notificationService.create(
                Notification.builder()
                        .type("WAITER_CALL_UPDATED")
                        .title("Appel serveur mis à jour")
                        .message(buildWaiterCallMessage(saved))
                        .route("/admin/waiter-calls")
                        .entityId(saved.getId())
                        .build()
        );

        return saved;
    }

    private void validateStatus(String status) {
        if (!ALLOWED_STATUS.contains(status)) {
            throw new RuntimeException("Invalid waiter call status: " + status);
        }
    }

    private String buildWaiterCallMessage(WaiterCall call) {
        String tableNumber = call.getTable_number() == null || call.getTable_number().isBlank()
                ? "—"
                : call.getTable_number().trim();

        String customerName = call.getCustomer_name() == null || call.getCustomer_name().isBlank()
                ? "Client"
                : call.getCustomer_name().trim();

        String note = call.getNote() == null ? "" : call.getNote().trim();

        StringBuilder sb = new StringBuilder();
        sb.append("Table ").append(tableNumber).append(" • ").append(customerName);

        if (!note.isBlank()) {
            sb.append(" • ").append(note);
        }

        return sb.toString();
    }
}