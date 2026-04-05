package com.magnum.coffe.waiterCall.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "waiterCalls")
public class WaiterCall {

    @Id
    private String id;

    private String table_number;
    private String customer_name;
    private String note;
    private String status = "new";

    private LocalDateTime created_at;
    private LocalDateTime updated_at;


}