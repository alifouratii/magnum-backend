package com.magnum.coffe.notification.dao;

import com.magnum.coffe.notification.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationDao {

    List<Notification> findAll(String scope);

    Optional<Notification> findById(String id);

    Notification save(Notification notification);

    long countUnread(String scope);
}