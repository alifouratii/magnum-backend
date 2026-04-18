package com.magnum.coffe.notification.service.impl;

import com.magnum.coffe.notification.dao.NotificationDao;
import com.magnum.coffe.notification.model.Notification;
import com.magnum.coffe.notification.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;

    public NotificationServiceImpl(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public List<Notification> getAll(String scope) {
        return notificationDao.findAll(scope);
    }

    @Override
    public long getUnreadCount(String scope) {
        return notificationDao.countUnread(scope);
    }

    @Override
    public Notification create(Notification notification) {
        notification.setId(null);
        notification.setCreatedAt(Instant.now());
        notification.setRead(false);
        notification.setScope(normalizeStoredScope(notification.getScope()));
        return notificationDao.save(notification);
    }

    @Override
    public Notification markAsRead(String id, String scope) {
        Notification notification = notificationDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        if (!canAccess(notification, scope)) {
            throw new RuntimeException("Notification not accessible for scope: " + scope);
        }

        notification.setRead(true);
        return notificationDao.save(notification);
    }

    @Override
    public void markAllAsRead(String scope) {
        List<Notification> list = notificationDao.findAll(scope);

        for (Notification notification : list) {
            if (!notification.isRead() && canAccess(notification, scope)) {
                notification.setRead(true);
                notificationDao.save(notification);
            }
        }
    }

    private boolean canAccess(Notification notification, String scope) {
        String requestScope = normalizeRequestScope(scope);

        if ("ALL".equals(requestScope)) {
            return true;
        }

        String notificationScope = normalizeStoredScope(notification.getScope());

        if ("ALL".equals(notificationScope)) {
            return true;
        }

        return requestScope.equals(notificationScope);
    }

    private String normalizeRequestScope(String scope) {
        if (scope == null) {
            return "ALL";
        }

        String normalized = scope.trim().toUpperCase();
        if ("SESSION1".equals(normalized) || "SESSION2".equals(normalized) || "ALL".equals(normalized)) {
            return normalized;
        }

        return "ALL";
    }

    private String normalizeStoredScope(String scope) {
        if (scope == null || scope.isBlank()) {
            return "ALL";
        }

        String normalized = scope.trim().toUpperCase();
        if ("SESSION1".equals(normalized) || "SESSION2".equals(normalized) || "ALL".equals(normalized)) {
            return normalized;
        }

        return "ALL";
    }
}