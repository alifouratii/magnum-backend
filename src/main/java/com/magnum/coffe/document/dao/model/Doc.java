package com.magnum.coffe.document.dao.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

@Document(collection = "doc")
@Data

public class Doc  implements Serializable {

    @Id
    String id;

    String idFile;

    //@Indexed
    String fileName;
    String contentType;
    String size;
    String title;//
    String reference;//
    byte[] file;
    Instant creationDate;
    Boolean BigData=false;

    @Indexed
    String userId;
    Instant dateTimeModification;
    String datatypeContainer;


    String idContainner;
    Boolean used;
    Instant dateTimeDelete;
    String deleteBy;
    DocumentType docType;
    Boolean isActif;

    public Boolean getBigData() {
        return BigData != null ? BigData : Boolean.FALSE;
    }
    public Boolean getUsed() {
        return used != null ? used : Boolean.FALSE;
    }

}
