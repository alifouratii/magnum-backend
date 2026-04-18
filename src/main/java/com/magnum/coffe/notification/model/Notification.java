package com.magnum.coffe.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String type;
    private String title;
    private String message;
    private String route;
    private String entityId;
    private String scope;

    @Builder.Default
    private boolean read = false;

    private Instant createdAt;
}