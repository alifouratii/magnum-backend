package com.magnum.coffe.notification.api;

import com.magnum.coffe.notification.model.Notification;
import com.magnum.coffe.notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getAll(
            @RequestParam(value = "scope", defaultValue = "ALL") String scope
    ) {
        return notificationService.getAll(scope);
    }

    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCount(
            @RequestParam(value = "scope", defaultValue = "ALL") String scope
    ) {
        return Map.of("count", notificationService.getUnreadCount(scope));
    }

    @PatchMapping("/{id}/read")
    public Notification markAsRead(
            @PathVariable String id,
            @RequestParam(value = "scope", defaultValue = "ALL") String scope
    ) {
        return notificationService.markAsRead(id, scope);
    }

    @PatchMapping("/read-all")
    public Map<String, String> markAllAsRead(
            @RequestParam(value = "scope", defaultValue = "ALL") String scope
    ) {
        notificationService.markAllAsRead(scope);
        return Map.of("message", "All notifications marked as read");
    }
}