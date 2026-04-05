package com.magnum.coffe.product.repositories;



import com.magnum.coffe.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByCategoryIdOrderBySortOrderAsc(String categoryId);

    List<Product> findByFeaturedTrueAndAvailableTrueOrderBySortOrderAsc();

    List<Product> findByAvailableTrueOrderBySortOrderAsc();

    List<Product> findByCategoryIdAndAvailableTrueOrderBySortOrderAsc(String categoryId);

    List<Product> findByCategoryIdAndFeaturedTrueAndAvailableTrueOrderBySortOrderAsc(String categoryId);
}