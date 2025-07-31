package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.CartDto;
import com.tekerasoft.tekeramarketplace.dto.request.AddToCartRequest;
import com.tekerasoft.tekeramarketplace.model.redisdocument.Cart;
import com.tekerasoft.tekeramarketplace.model.redisdocument.CartItem;
import com.tekerasoft.tekeramarketplace.repository.jparepository.CartRepository;
import com.tekerasoft.tekeramarketplace.utils.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Service
public class CartService {

    private final ProductService productService;
    @Value("${app.guest-cart-ttl-days}")
    private long guestCartTtlDays;

    private final CartRepository cartRepository;
    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthenticationFacade  authenticationFacade;

    public CartService(CartRepository cartRepository, UserService userService,
                       RedisTemplate<String, Object> redisTemplate, ProductService productService, AuthenticationFacade authenticationFacade) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.redisTemplate = redisTemplate;
        this.productService = productService;
        this.authenticationFacade = authenticationFacade;
    }

    private String getCartKey(String userId) {
        return "cart:" + userId;
    }

    public Cart getCart() {
        String userId = authenticationFacade.getCurrentUserId();
        Object value = redisTemplate.opsForValue().get(getCartKey(userId));
        return (value instanceof Cart) ? (Cart) value : null;
    }

    public void saveCart(String userId, Cart cart) {
        redisTemplate.opsForValue().set(getCartKey(userId), cart, Duration.ofDays(guestCartTtlDays));
    }

    // Yeni ürün(ler) ekleme
    public Cart addToCart(List<AddToCartRequest> req) {
        String userId = authenticationFacade.getCurrentUserId();
        Cart existingCart = getCart();
        if (existingCart == null) {
            existingCart = productService.toCartItem(req, userId);
        } else {
            Cart newItemsCart = productService.toCartItem(req, userId);
            for (CartItem newItem : newItemsCart.getCartItems()) {
                CartItem existingItem = existingCart.getCartItems().stream()
                        .filter(ci -> ci.getAttributeId().equals(newItem.getAttributeId()))
                        .findFirst()
                        .orElse(null);

                if (existingItem != null) {
                    existingItem.setQuantity(newItem.getQuantity());
                } else {
                    existingCart.getCartItems().add(newItem);
                }
            }
            updateCartTotals(existingCart);
        }
        saveCart(userId, existingCart);
        return existingCart; // Güncel sepeti dön
    }

    public Cart removeFromCart(String attributeId) {
        String userId = authenticationFacade.getCurrentUserId();
        Cart cart = getCart();
        if (cart != null) {
            if(cart.getCartItems().isEmpty()){
                clearCart();
            }
            cart.getCartItems().removeIf(item -> item.getAttributeId().equals(attributeId));
            updateCartTotals(cart);
            saveCart(userId, cart);
        }
        return cart; // Güncel sepeti dön
    }

    public void clearCart() {
        String userId = authenticationFacade.getCurrentUserId();
        redisTemplate.delete(getCartKey(userId));
    }

    // Toplam fiyat ve itemCount güncelleme
    private void updateCartTotals(Cart cart) {
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPrice);
        cart.setItemCount(cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum());
    }
}
