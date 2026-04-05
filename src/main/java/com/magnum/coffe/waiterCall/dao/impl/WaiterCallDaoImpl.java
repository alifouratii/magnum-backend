package com.magnum.coffe.waiterCall.dao.impl;


import com.magnum.coffe.waiterCall.dao.WaiterCallDao;
import com.magnum.coffe.waiterCall.model.WaiterCall;
import com.magnum.coffe.waiterCall.repositories.WaiterCallRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class WaiterCallDaoImpl implements WaiterCallDao {

    private final WaiterCallRepository waiterCallRepository;

    public WaiterCallDaoImpl(WaiterCallRepository waiterCallRepository) {
        this.waiterCallRepository = waiterCallRepository;
    }

    @Override
    public List<WaiterCall> findAll() {
        List<WaiterCall> list = waiterCallRepository.findAll();
        list.sort((a, b) -> {
            if (a.getCreated_at() == null && b.getCreated_at() == null) return 0;
            if (a.getCreated_at() == null) return 1;
            if (b.getCreated_at() == null) return -1;
            return b.getCreated_at().compareTo(a.getCreated_at());
        });
        return list;
    }

    @Override
    public Optional<WaiterCall> findById(String id) {
        return waiterCallRepository.findById(id);
    }

    @Override
    public WaiterCall save(WaiterCall waiterCall) {
        return waiterCallRepository.save(waiterCall);
    }
}