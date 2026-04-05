package com.magnum.coffe.cart.model;


import com.magnum.coffe.document.dao.model.Doc;
import com.magnum.coffe.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItem {

    private Product product;
    private List<Doc> images = new ArrayList<>();
    private int quantity = 1;


}