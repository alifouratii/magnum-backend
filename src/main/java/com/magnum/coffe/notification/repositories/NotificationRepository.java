package com.magnum.coffe.notification.repositories;

import com.magnum.coffe.notification.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    long countByReadFalse();
    long countByReadFalseAndScopeIn(List<String> scopes);
    List<Notification> findAllByOrderByCreatedAtDesc();
    List<Notification> findByScopeInOrderByCreatedAtDesc(List<String> scopes);
}