package com.magnum.coffe.category.service.impl;

import com.magnum.coffe.category.service.MenuCategoryService;
import com.magnum.coffe.category.dao.MenuCategoryDao;
import com.magnum.coffe.category.model.MenuCategory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MenuCategoryServiceImpl implements MenuCategoryService {

    private final MenuCategoryDao menuCategoryDao;

    public MenuCategoryServiceImpl(MenuCategoryDao menuCategoryDao) {
        this.menuCategoryDao = menuCategoryDao;
    }

    @Override
    public List<MenuCategory> getAll() {
        List<MenuCategory> list = menuCategoryDao.findAll();
        list.sort(Comparator.comparingInt(MenuCategory::getSort_order));
        return list;
    }

    @Override
    public MenuCategory getById(String id) {
        return menuCategoryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Override
    public MenuCategory create(MenuCategory payload) {
        payload.setId(null);

        if (payload.getName() == null || payload.getName().isBlank()) {
            payload.setName(payload.getTitle());
        }

        if (payload.getSubgroups() == null) {
            payload.setSubgroups(List.of());
        }

        if (payload.getItems() == null) {
            payload.setItems(List.of());
        }

        return menuCategoryDao.save(payload);
    }

    @Override
    public MenuCategory update(String id, MenuCategory payload) {
        MenuCategory existing = menuCategoryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        existing.setTitle(payload.getTitle());
        existing.setName(
                payload.getName() == null || payload.getName().isBlank()
                        ? payload.getTitle()
                        : payload.getName()
        );
        existing.setSlug(payload.getSlug());
        existing.setIcon(payload.getIcon());
        existing.setIs_active(payload.getIs_active());
        existing.setSort_order(payload.getSort_order());
        existing.setSubgroups(payload.getSubgroups());
        existing.setItems(payload.getItems());

        return menuCategoryDao.save(existing);
    }

    @Override
    public void delete(String id) {
        if (!menuCategoryDao.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        menuCategoryDao.deleteById(id);
    }
}