package com.magnum.coffe.waiterCall.service.impl;


import com.magnum.coffe.waiterCall.dao.WaiterCallDao;
import com.magnum.coffe.waiterCall.model.WaiterCall;
import com.magnum.coffe.waiterCall.service.WaiterCallService;
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

    public WaiterCallServiceImpl(WaiterCallDao waiterCallDao) {
        this.waiterCallDao = waiterCallDao;
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

        return waiterCallDao.save(payload);
    }

    @Override
    public WaiterCall updateStatus(String id, String status) {
        validateStatus(status);

        WaiterCall existing = waiterCallDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Waiter call not found with id: " + id));

        existing.setStatus(status);
        existing.setUpdated_at(LocalDateTime.now());

        return waiterCallDao.save(existing);
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

        return waiterCallDao.save(existing);
    }

    private void validateStatus(String status) {
        if (!ALLOWED_STATUS.contains(status)) {
            throw new RuntimeException("Invalid waiter call status: " + status);
        }
    }
}