package com.magnum.coffe.notification.service.impl;

import com.magnum.coffe.notification.dao.NotificationDao;
import com.magnum.coffe.notification.model.Notification;
import com.magnum.coffe.notification.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;

    public NotificationServiceImpl(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Override
    public List<Notification> getAll() {
        return notificationDao.findAll();
    }

    @Override
    public long getUnreadCount() {
        return notificationDao.countUnread();
    }

    @Override
    public Notification create(Notification notification) {
        notification.setId(null);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        return notificationDao.save(notification);
    }

    @Override
    public Notification markAsRead(String id) {
        Notification notification = notificationDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        notification.setRead(true);
        return notificationDao.save(notification);
    }

    @Override
    public void markAllAsRead() {
        List<Notification> list = notificationDao.findAll();
        for (Notification notification : list) {
            if (!notification.isRead()) {
                notification.setRead(true);
                notificationDao.save(notification);
            }
        }
    }
}