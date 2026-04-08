package com.magnum.coffe.product.api;

import com.magnum.coffe.product.model.Product;
import com.magnum.coffe.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAll(
            @RequestParam(value = "category_id", required = false) String categoryId,
            @RequestParam(value = "is_featured", required = false) Boolean isFeatured,
            @RequestParam(value = "is_available", required = false) Boolean isAvailable
    ) {
        return productService.getFiltered(categoryId, isFeatured, isAvailable);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable String id) {
        return productService.getById(id);
    }

    @PostMapping
    public Product create(@RequestBody Product payload) {
        return productService.create(payload);
    }

    @PostMapping("/bulk")
    public List<Product> createMany(@RequestBody List<Product> payloads) {
        return productService.createMany(payloads);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable String id, @RequestBody Product payload) {
        return productService.update(id, payload);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        productService.delete(id);
    }
}