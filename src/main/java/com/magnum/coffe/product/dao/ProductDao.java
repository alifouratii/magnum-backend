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

    void deleteById(String id);

    boolean existsById(String id);
}