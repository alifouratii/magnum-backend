package com.magnum.coffe.cart.service.impl;

import com.magnum.coffe.cart.dao.CartDao;
import com.magnum.coffe.cart.model.Cart;
import com.magnum.coffe.cart.model.CartItem;
import com.magnum.coffe.cart.model.request.AddCartItemRequest;
import com.magnum.coffe.cart.model.request.SyncCartRequest;
import com.magnum.coffe.cart.service.CartService;
import com.magnum.coffe.document.dao.model.Doc;
import com.magnum.coffe.product.model.Product;
import com.magnum.coffe.product.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartDao cartDao;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartDao cartDao, ProductRepository productRepository) {
        this.cartDao = cartDao;
        this.productRepository = productRepository;
    }

    @Override
    public Cart getByCartKey(String cartKey) {
        return getOrCreateCart(cartKey);
    }

    @Override
    public Cart addItem(String cartKey, AddCartItemRequest request) {
        Cart cart = getOrCreateCart(cartKey);

        if (request == null || request.getProductId() == null || request.getProductId().isBlank()) {
            throw new RuntimeException("productId is required");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.getProductId()));

        int safeQuantity = Math.max(request.getQuantity() != null ? request.getQuantity() : 1, 1);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item ->
                        item.getProduct() != null &&
                                item.getProduct().getId() != null &&
                                item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + safeQuantity);
        } else {
            CartItem item = new CartItem();
            item.setProduct(product);

            List<Doc> images = product.getImages() != null ? product.getImages() : new ArrayList<>();
            item.setImages(images);

            item.setQuantity(safeQuantity);
            cart.getItems().add(item);
        }

        cart.setUpdated_at(Instant.now());
        return cartDao.save(cart);
    }

    @Override
    public Cart updateItemQuantity(String cartKey, String productId, int quantity) {
        Cart cart = getOrCreateCart(cartKey);

        if (quantity <= 0) {
            cart.getItems().removeIf(item ->
                    item.getProduct() != null &&
                            productId.equals(item.getProduct().getId()));
        } else {
            cart.getItems().forEach(item -> {
                if (item.getProduct() != null && productId.equals(item.getProduct().getId())) {
                    item.setQuantity(quantity);
                }
            });
        }

        cart.setUpdated_at(Instant.now());
        return cartDao.save(cart);
    }

    @Override
    public Cart removeItem(String cartKey, String productId) {
        Cart cart = getOrCreateCart(cartKey);

        cart.getItems().removeIf(item ->
                item.getProduct() != null &&
                        productId.equals(item.getProduct().getId()));

        cart.setUpdated_at(Instant.now());
        return cartDao.save(cart);
    }

    @Override
    public Cart sync(String cartKey, SyncCartRequest request) {
        Cart cart = getOrCreateCart(cartKey);

        cart.setUserId(request.getUserId());
        cart.setItems(request.getItems() != null ? request.getItems() : new ArrayList<>());
        cart.setUpdated_at(Instant.now());

        return cartDao.save(cart);
    }

    @Override
    public void clear(String cartKey) {
        if (cartDao.existsByCartKey(cartKey)) {
            cartDao.deleteByCartKey(cartKey);
        }
    }

    private Cart getOrCreateCart(String cartKey) {
        return cartDao.findByCartKey(cartKey).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setCartKey(cartKey);
            cart.setItems(new ArrayList<>());
            cart.setCreated_at(Instant.now());
            cart.setUpdated_at(Instant.now());
            return cartDao.save(cart);
        });
    }
}