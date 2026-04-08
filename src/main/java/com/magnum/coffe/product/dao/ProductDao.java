package com.magnum.coffe.product.dao;

import com.magnum.coffe.product.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

    List<Product> findAll();

    List<Product> findByCategoryId(String categoryId);

    List<Product> findFeaturedAndAvailable();

    List<Product> findAvailable();

    List<Product> findByCategoryIdAndAvailable(String categoryId);

    List<Product> findByCategoryIdFeaturedAndAvailable(String categoryId);

    Optional<Product> findById(String id);

    Product save(Product product);

    List<Product> saveAll(List<Product> products);

    void deleteById(String id);

    void deleteAll();

    boolean existsById(String id);
}