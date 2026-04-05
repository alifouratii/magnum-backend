package com.magnum.coffe.waiterCall.dao;



import com.magnum.coffe.waiterCall.model.WaiterCall;

import java.util.List;
import java.util.Optional;

public interface WaiterCallDao {

    List<WaiterCall> findAll();

    Optional<WaiterCall> findById(String id);

    WaiterCall save(WaiterCall waiterCall);
}