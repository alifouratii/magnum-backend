package com.magnum.coffe.waiterCall.api;

import com.magnum.coffe.waiterCall.model.WaiterCall;
import com.magnum.coffe.waiterCall.model.request.UpdateWaiterCallStatusRequest;
import com.magnum.coffe.waiterCall.service.WaiterCallService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waiterCalls")
@CrossOrigin(origins = "*")
public class WaiterCallController {

    private final WaiterCallService waiterCallService;

    public WaiterCallController(WaiterCallService waiterCallService) {
        this.waiterCallService = waiterCallService;
    }

    @GetMapping
    public List<WaiterCall> getAll() {
        return waiterCallService.getAll();
    }

    @PostMapping
    public WaiterCall create(@RequestBody WaiterCall payload) {
        return waiterCallService.create(payload);
    }

    @PatchMapping("/{id}")
    public WaiterCall updateStatus(
            @PathVariable String id,
            @RequestBody UpdateWaiterCallStatusRequest request
    ) {
        return waiterCallService.updateStatus(id, request.getStatus());
    }

    @PutMapping("/{id}")
    public WaiterCall update(
            @PathVariable String id,
            @RequestBody WaiterCall payload
    ) {
        return waiterCallService.update(id, payload);
    }
}