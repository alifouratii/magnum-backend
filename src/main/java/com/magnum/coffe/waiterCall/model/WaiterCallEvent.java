package com.magnum.coffe.waiterCall.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaiterCallEvent {
    private String type; // WAITER_CALL_CREATED, WAITER_CALL_UPDATED
    private String waiterCallId;
    private String message;
    private WaiterCall waiterCall;
    private long timestamp;
}