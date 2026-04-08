package com.magnum.coffe.product.dao.impl;

import com.magnum.coffe.product.dao.ProductDao;
import com.magnum.coffe.product.model.Product;
import com.magnum.coffe.product.repositories.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class ProductDaoImpl implements ProductDao {

    private final ProductRepository productRepository;

    public ProductDaoImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        List<Product> list = productRepository.findAll();
        list.sort(Comparator.comparingInt(Product::getSortOrder));
        return list;
    }

    @Override
    public List<Product> findByCategoryId(String categoryId) {
        return productRepository.findByCategoryIdOrderBySortOrderAsc(categoryId);
    }

    @Override
    public List<Product> findFeaturedAndAvailable() {
        return productRepository.findByFeaturedTrueAndAvailableTrueOrderBySortOrderAsc();
    }

    @Override
    public List<Product> findAvailable() {
        return productRepository.findByAvailableTrueOrderBySortOrderAsc();
    }

    @Override
    public List<Product> findByCategoryIdAndAvailable(String categoryId) {
        return productRepository.findByCategoryIdAndAvailableTrueOrderBySortOrderAsc(categoryId);
    }

    @Override
    public List<Product> findByCategoryIdFeaturedAndAvailable(String categoryId) {
        return productRepository.findByCategoryIdAndFeaturedTrueAndAvailableTrueOrderBySortOrderAsc(categoryId);
    }

    @Override
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @Override
    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return productRepository.existsById(id);
    }
}