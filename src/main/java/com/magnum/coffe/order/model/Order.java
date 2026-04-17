package com.magnum.coffe.order.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String order_number;

    private String customer_name;
    private String customer_phone;
    private String table_number;
    private String note;

    private String status = "pending";

    private double subtotal;
    private double total;

    private Instant created_at;
    private Instant updated_at;

    private List<OrderItem> items = new ArrayList<>();



}