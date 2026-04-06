package com.magnum.coffe.notification.service;

import com.magnum.coffe.notification.model.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> getAll();

    long getUnreadCount();

    Notification create(Notification notification);

    Notification markAsRead(String id);

    void markAllAsRead();
}