package com.magnum.coffe.product.service.impl;

import com.magnum.coffe.product.dao.ProductDao;
import com.magnum.coffe.product.model.Product;
import com.magnum.coffe.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> getAll() {
        return productDao.findAll();
    }

    @Override
    public List<Product> getFiltered(String categoryId, Boolean isFeatured, Boolean isAvailable) {
        boolean hasCategory = categoryId != null && !categoryId.isBlank();
        boolean featured = Boolean.TRUE.equals(isFeatured);
        boolean available = Boolean.TRUE.equals(isAvailable);

        if (hasCategory && featured && available) {
            return productDao.findByCategoryIdFeaturedAndAvailable(categoryId);
        }

        if (hasCategory && available) {
            return productDao.findByCategoryIdAndAvailable(categoryId);
        }

        if (hasCategory) {
            return productDao.findByCategoryId(categoryId);
        }

        if (featured && available) {
            return productDao.findFeaturedAndAvailable();
        }

        if (available) {
            return productDao.findAvailable();
        }

        return productDao.findAll();
    }

    @Override
    public Product getById(String id) {
        return productDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public Product create(Product payload) {
        LocalDateTime now = LocalDateTime.now();

        normalize(payload);

        payload.setId(null);
        payload.setCreatedAt(now);
        payload.setUpdatedAt(now);

        return productDao.save(payload);
    }

    @Override
    public List<Product> createMany(List<Product> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            return List.of();
        }

        List<Product> products = new ArrayList<>();

        for (Product payload : payloads) {
            normalize(payload);

            if (payload.getCreatedAt() == null) {
                payload.setCreatedAt(LocalDateTime.now());
            }

            if (payload.getUpdatedAt() == null) {
                payload.setUpdatedAt(payload.getCreatedAt());
            }

            products.add(payload);
        }

        return productDao.saveAll(products);
    }

    @Override
    public Product update(String id, Product payload) {
        Product existing = productDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        normalize(payload);

        existing.setCategoryId(payload.getCategoryId());
        existing.setSubgroupId(payload.getSubgroupId());
        existing.setName(payload.getName());
        existing.setSlug(payload.getSlug());
        existing.setShortDescription(payload.getShortDescription());
        existing.setLongDescription(payload.getLongDescription());
        existing.setPrice(payload.getPrice());
        existing.setBadge(payload.getBadge());
        existing.setFeatured(payload.getFeatured());
        existing.setAvailable(payload.getAvailable());
        existing.setSortOrder(payload.getSortOrder());
        existing.setImages(payload.getImages());
        existing.setUpdatedAt(LocalDateTime.now());

        return productDao.save(existing);
    }

    @Override
    public void delete(String id) {
        if (!productDao.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productDao.deleteById(id);
    }

    private void normalize(Product payload) {
        if (payload.getCategoryId() != null) {
            payload.setCategoryId(payload.getCategoryId().trim());
        }

        if (payload.getSubgroupId() != null) {
            payload.setSubgroupId(payload.getSubgroupId().trim());
        }

        payload.setName(payload.getName() == null ? "" : payload.getName().trim());
        payload.setSlug(payload.getSlug() == null ? "" : payload.getSlug().trim());
        payload.setShortDescription(payload.getShortDescription() == null ? "" : payload.getShortDescription().trim());
        payload.setLongDescription(payload.getLongDescription() == null ? "" : payload.getLongDescription().trim());
        payload.setBadge(payload.getBadge() == null ? "" : payload.getBadge().trim());

        if (payload.getPrice() == null) {
            payload.setPrice(0.0);
        }

        if (payload.getFeatured() == null) {
            payload.setFeatured(false);
        }

        if (payload.getAvailable() == null) {
            payload.setAvailable(true);
        }

        if (payload.getSortOrder() == null) {
            payload.setSortOrder(0);
        }

        if (payload.getImages() == null) {
            payload.setImages(new ArrayList<>());
        }
    }
}