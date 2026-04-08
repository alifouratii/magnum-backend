package com.magnum.coffe.category.api;

import com.magnum.coffe.category.model.MenuCategory;
import com.magnum.coffe.category.service.MenuCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class MenuCategoryController {

    private final MenuCategoryService menuCategoryService;

    public MenuCategoryController(MenuCategoryService menuCategoryService) {
        this.menuCategoryService = menuCategoryService;
    }

    @GetMapping
    public List<MenuCategory> getAll() {
        return menuCategoryService.getAll();
    }

    @GetMapping("/{id}")
    public MenuCategory getById(@PathVariable String id) {
        return menuCategoryService.getById(id);
    }

    @PostMapping
    public MenuCategory create(@RequestBody MenuCategory payload) {
        return menuCategoryService.create(payload);
    }

    @PostMapping("/bulk")
    public List<MenuCategory> createMany(@RequestBody List<MenuCategory> payloads) {
        return menuCategoryService.createMany(payloads);
    }

    @PutMapping("/{id}")
    public MenuCategory update(@PathVariable String id, @RequestBody MenuCategory payload) {
        return menuCategoryService.update(id, payload);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        menuCategoryService.delete(id);
    }
    @DeleteMapping("/all")
    public void deleteAll() {
        menuCategoryService.deleteAll();
    }
}