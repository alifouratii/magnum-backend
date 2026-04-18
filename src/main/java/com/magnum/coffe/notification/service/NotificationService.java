package com.magnum.coffe.notification.service;

import com.magnum.coffe.notification.model.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> getAll(String scope);

    long getUnreadCount(String scope);

    Notification create(Notification notification);

    Notification markAsRead(String id, String scope);

    void markAllAsRead(String scope);
}