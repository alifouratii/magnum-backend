package com.magnum.coffe.category.dao.impl;

import com.magnum.coffe.category.dao.MenuCategoryDao;
import com.magnum.coffe.category.model.MenuCategory;
import com.magnum.coffe.category.repositories.MenuCategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MenuCategoryDaoImpl implements MenuCategoryDao {

    private final MenuCategoryRepository menuCategoryRepository;

    public MenuCategoryDaoImpl(MenuCategoryRepository menuCategoryRepository) {
        this.menuCategoryRepository = menuCategoryRepository;
    }

    @Override
    public List<MenuCategory> findAll() {
        return menuCategoryRepository.findAll();
    }

    @Override
    public Optional<MenuCategory> findById(String id) {
        return menuCategoryRepository.findById(id);
    }

    @Override
    public MenuCategory save(MenuCategory category) {
        return menuCategoryRepository.save(category);
    }

    @Override
    public void deleteById(String id) {
        menuCategoryRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return menuCategoryRepository.existsById(id);
    }
}