package com.magnum.coffe.category.dao;

import com.magnum.coffe.category.model.MenuCategory;

import java.util.List;
import java.util.Optional;

public interface MenuCategoryDao {

    List<MenuCategory> findAll();

    Optional<MenuCategory> findById(String id);

    MenuCategory save(MenuCategory category);

    List<MenuCategory> saveAll(List<MenuCategory> categories);

    void deleteById(String id);

    boolean existsById(String id);
    void deleteAll();
}