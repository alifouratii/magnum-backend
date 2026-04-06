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
    public List<Notification> findAll() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
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
    public long countUnread() {
        return notificationRepository.countByReadFalse();
    }
}