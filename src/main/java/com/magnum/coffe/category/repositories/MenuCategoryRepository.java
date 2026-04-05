package com.magnum.coffe.category.repositories;

import com.magnum.coffe.category.model.MenuCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuCategoryRepository extends MongoRepository<MenuCategory, String> {
}