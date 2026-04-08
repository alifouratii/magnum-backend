package com.magnum.coffe.category.service;

import com.magnum.coffe.category.model.MenuCategory;

import java.util.List;

public interface MenuCategoryService {

    List<MenuCategory> getAll();

    MenuCategory getById(String id);

    MenuCategory create(MenuCategory payload);

    List<MenuCategory> createMany(List<MenuCategory> payloads);

    MenuCategory update(String id, MenuCategory payload);

    void delete(String id);
}