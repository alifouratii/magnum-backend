package com.magnum.coffe.waiterCall.service;


import com.magnum.coffe.waiterCall.model.WaiterCall;

import java.util.List;

public interface WaiterCallService {

    List<WaiterCall> getAll();

    WaiterCall create(WaiterCall payload);

    WaiterCall updateStatus(String id, String status);

    WaiterCall update(String id, WaiterCall payload);
}