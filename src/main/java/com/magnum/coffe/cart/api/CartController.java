package com.magnum.coffe.cart.api;


import com.magnum.coffe.cart.model.Cart;
import com.magnum.coffe.cart.model.request.AddCartItemRequest;
import com.magnum.coffe.cart.model.request.SyncCartRequest;
import com.magnum.coffe.cart.model.request.UpdateCartItemQuantityRequest;
import com.magnum.coffe.cart.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{cartKey}")
    public Cart getCart(@PathVariable String cartKey) {
        return cartService.getByCartKey(cartKey);
    }

    @PostMapping("/{cartKey}/items")
    public Cart addItem(@PathVariable String cartKey, @RequestBody AddCartItemRequest request) {
        return cartService.addItem(cartKey, request);
    }

    @PutMapping("/{cartKey}/items/{productId}")
    public Cart updateQuantity(
            @PathVariable String cartKey,
            @PathVariable String productId,
            @RequestBody UpdateCartItemQuantityRequest request
    ) {
        return cartService.updateItemQuantity(cartKey, productId, request.getQuantity());
    }

    @DeleteMapping("/{cartKey}/items/{productId}")
    public Cart removeItem(@PathVariable String cartKey, @PathVariable String productId) {
        return cartService.removeItem(cartKey, productId);
    }

    @PostMapping("/{cartKey}/sync")
    public Cart sync(@PathVariable String cartKey, @RequestBody SyncCartRequest request) {
        return cartService.sync(cartKey, request);
    }

    @DeleteMapping("/{cartKey}")
    public void clear(@PathVariable String cartKey) {
        cartService.clear(cartKey);
    }
}