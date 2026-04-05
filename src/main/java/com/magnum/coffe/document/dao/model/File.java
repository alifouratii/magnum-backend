package com.magnum.coffe.document.dao.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document(collection = "file")
@Data


@FieldDefaults(level = AccessLevel.PRIVATE)
public class File implements Serializable {
    @Id
    String id;
    byte[] fileByte;
    Binary fileBinary;
    String userId;
    LocalDateTime dateTimeModification;
}
