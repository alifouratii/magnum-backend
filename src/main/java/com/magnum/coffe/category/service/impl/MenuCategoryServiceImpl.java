package com.magnum.coffe.category.service.impl;

import com.magnum.coffe.category.dao.MenuCategoryDao;
import com.magnum.coffe.category.model.MenuCategory;
import com.magnum.coffe.category.model.MenuSubgroup;
import com.magnum.coffe.category.service.MenuCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        normalizeCategory(payload);
        return menuCategoryDao.save(payload);
    }

    @Override
    public List<MenuCategory> createMany(List<MenuCategory> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            return List.of();
        }

        List<MenuCategory> categories = new ArrayList<>();

        for (MenuCategory payload : payloads) {
            normalizeCategory(payload);
            categories.add(payload);
        }

        return menuCategoryDao.saveAll(categories);
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
        existing.setSubgroups(payload.getSubgroups() == null ? List.of() : payload.getSubgroups());
        existing.setItems(payload.getItems() == null ? List.of() : payload.getItems());

        return menuCategoryDao.save(existing);
    }

    @Override
    public void delete(String id) {
        if (!menuCategoryDao.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        menuCategoryDao.deleteById(id);
    }

    private void normalizeCategory(MenuCategory payload) {
        if (payload == null) {
            throw new RuntimeException("Category payload is null");
        }

        if (payload.getName() == null || payload.getName().isBlank()) {
            payload.setName(payload.getTitle());
        }

        if (payload.getSubgroups() == null) {
            payload.setSubgroups(List.of());
        }

        if (payload.getItems() == null) {
            payload.setItems(List.of());
        }

        if (payload.getIs_active() == null) {
            payload.setIs_active(true);
        }



        for (MenuSubgroup subgroup : payload.getSubgroups()) {
            if (subgroup.getItems() == null) {
                subgroup.setItems(List.of());
            }
            if (subgroup.getSort_order() == null) {
                subgroup.setSort_order(0);
            }
        }
    }
    @Override
    public void deleteAll() {
        menuCategoryDao.deleteAll();
    }
}