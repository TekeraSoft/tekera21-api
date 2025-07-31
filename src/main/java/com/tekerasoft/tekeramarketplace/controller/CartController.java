package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.request.AddToCartRequest;
import com.tekerasoft.tekeramarketplace.model.redisdocument.Cart;
import com.tekerasoft.tekeramarketplace.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/addToCart")
    public Cart addToCart(@RequestBody List<AddToCartRequest> req) {
        return cartService.addToCart(req);
    }

    @GetMapping("/getCart")
    public Cart getCart() {
        return cartService.getCart();
    }

    @DeleteMapping("/removeFromCart")
    public Cart removeItemFromCart(@RequestParam String attributeId) {
        return cartService.removeFromCart(attributeId);
    }

    @DeleteMapping("/clearCart")
    public void clearCart() {cartService.clearCart();}

}
