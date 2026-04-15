package com.magnum.coffe.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {

    private String product_id;
    private String product_name;
    private String category_id;
    private String category_name;
    private double unit_price;
    private int quantity;
    private double line_total;

}