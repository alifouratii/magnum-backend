package com.magnum.coffe.product.service;

import com.magnum.coffe.product.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    List<Product> getFiltered(String categoryId, Boolean isFeatured, Boolean isAvailable);

    Product getById(String id);

    Product create(Product payload);

    List<Product> createMany(List<Product> payloads);

    Product update(String id, Product payload);

    void delete(String id);

    void deleteAll();
}