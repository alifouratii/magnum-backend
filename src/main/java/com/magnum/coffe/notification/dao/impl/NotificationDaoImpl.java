package com.magnum.coffe.notification.dao.impl;

import com.magnum.coffe.notification.dao.NotificationDao;
import com.magnum.coffe.notification.model.Notification;
import com.magnum.coffe.notification.repositories.NotificationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class NotificationDaoImpl implements NotificationDao {

    private final NotificationRepository notificationRepository;

    public NotificationDaoImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> findAll(String scope) {
        String normalizedScope = normalizeScope(scope);

        if ("ALL".equals(normalizedScope)) {
            return notificationRepository.findAllByOrderByCreatedAtDesc();
        }

        return notificationRepository.findByScopeInOrderByCreatedAtDesc(
                List.of(normalizedScope, "ALL")
        );
    }

    @Override
    public Optional<Notification> findById(String id) {
        return notificationRepository.findById(id);
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public long countUnread(String scope) {
        String normalizedScope = normalizeScope(scope);

        if ("ALL".equals(normalizedScope)) {
            return notificationRepository.countByReadFalse();
        }

        return notificationRepository.countByReadFalseAndScopeIn(
                List.of(normalizedScope, "ALL")
        );
    }

    private String normalizeScope(String scope) {
        if (scope == null) {
            return "ALL";
        }

        String normalized = scope.trim().toUpperCase();
        if ("SESSION1".equals(normalized) || "SESSION2".equals(normalized) || "ALL".equals(normalized)) {
            return normalized;
        }

        return "ALL";
    }
}